package com.davgeoand.api;

import com.davgeoand.api.arangodb.FitnessDataDB;
import com.davgeoand.api.controller.ApiController;
import com.davgeoand.api.controller.StepConnectionController;
import com.davgeoand.api.controller.StepController;
import com.davgeoand.api.controller.UserController;
import com.davgeoand.api.exception.DocumentNotFoundException;
import com.davgeoand.api.exception.MissingPropertyException;
import com.davgeoand.api.monitor.event.handler.ServiceEventHandler;
import com.davgeoand.api.monitor.event.type.Audit;
import com.davgeoand.api.monitor.event.type.ServiceStart;
import com.davgeoand.api.monitor.metric.ServiceMetricRegistry;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.event.EventListener;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.http.RequestLogger;
import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.path;

@Slf4j
public class JavalinService {
    private Javalin service;
    private final long serviceInitDuration;
    private long startServiceTime;

    public JavalinService() {
        log.info("Initializing javalin web service");
        long serviceInitTime = System.currentTimeMillis();
        createJavalinService();
        serviceInitDuration = System.currentTimeMillis() - serviceInitTime;
        log.info("Successfully initialized javalin web service");
    }

    public void start() {
        log.info("Starting javalin web service");
        startServiceTime = System.currentTimeMillis();
        service.start(Integer.parseInt(ServiceProperties.getProperty("service.port").orElseThrow(() -> new MissingPropertyException("service.port"))));
        log.info("Successfully started javalin web service");
    }

    private void createJavalinService() {
        try {
            ServiceProperties.init("service.properties", "build.properties");
            ServiceMetricRegistry.init();
            ServiceEventHandler.init();
            FitnessDataDB.init();
            service = Javalin.create((javalinConfig -> {
                        javalinConfig.plugins.register(ServiceMetricRegistry.getMicrometerPlugin());
                        javalinConfig.requestLogger.http(setupRequestAuditEvent());
                    }))
                    .events(serviceStartedEvent())
                    .routes(routes())
                    .exception(DocumentNotFoundException.class, documentNotFoundHandler());
        } catch (Exception e) {
            log.error("Issue during startup", e);
            System.exit(1);
        }
    }

    private ExceptionHandler<? super DocumentNotFoundException> documentNotFoundHandler() {
        return (e, ctx) -> {
            ctx.json(e.getMessage());
            ctx.status(HttpStatus.NOT_FOUND);
        };
    }

    private RequestLogger setupRequestAuditEvent() {
        log.info("Setting up request audit event");
        return (ctx, ms) -> {
            String requestPath = ctx.endpointHandlerPath();
            HttpStatus httpStatus = ctx.status();
            HandlerType method = ctx.method();
            String response;
            if (ctx.status().getCode() < 400) {
                response = "OK";
            } else {
                response = ctx.result();
            }
            ServiceEventHandler.addEvent(new Audit(requestPath, String.valueOf(httpStatus.getCode()), method.toString(), response, ms, Span.current().getSpanContext().getTraceId()));
        };
    }

    private Consumer<EventListener> serviceStartedEvent() {
        log.info("Setting up server started steps");
        return (eventListener -> eventListener.serverStarted(() -> {
            long serviceStartDuration = System.currentTimeMillis() - startServiceTime;
            try {
                String buildVersion = ServiceProperties.getProperty("service.version").orElseThrow(() -> new MissingPropertyException("service.version"));
                String gitBranch = ServiceProperties.getProperty("git.branch").orElseThrow(() -> new MissingPropertyException("git.branch"));
                String gitCommitId = ServiceProperties.getProperty("git.commit.id.abbrev").orElseThrow(() -> new MissingPropertyException("git.commit.id.abbrev"));
                String javaVersion = ServiceProperties.getProperty("process.runtime.version").orElseThrow(() -> new MissingPropertyException("process.runtime.version"));
                long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
                ServiceEventHandler.addEvent(new ServiceStart(buildVersion, gitBranch, gitCommitId, startTime, serviceInitDuration, serviceStartDuration, javaVersion));
            } catch (MissingPropertyException missingPropertyException) {
                log.error("Not able to add ServiceStart event", missingPropertyException);
            }
        }));
    }

    private EndpointGroup routes() {
        log.info("Setting up routes");
        return () -> {
            path("api", ApiController.getApiEndpoints());
            path("data", () -> {
                path("users", UserController.getUsersEndpoints());
                path("steps", StepController.getStepsEndpoints());
                path("stepConnections", StepConnectionController.getStepConnectionsEndpoints());
            });
        };
    }
}

