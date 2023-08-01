package com.davgeoand.api;

import com.davgeoand.api.arangodb.FitnessDataDB;
import com.davgeoand.api.controller.ApiController;
import com.davgeoand.api.controller.StepConnectionController;
import com.davgeoand.api.controller.StepController;
import com.davgeoand.api.controller.UserController;
import com.davgeoand.api.exception.MissingPropertyException;
import com.davgeoand.api.monitor.event.handler.ServiceEventHandler;
import com.davgeoand.api.monitor.event.type.Audit;
import com.davgeoand.api.monitor.event.type.ServiceStart;
import com.davgeoand.api.monitor.metric.ServiceMetrics;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.config.JavalinConfig;
import io.javalin.event.EventListener;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.path;

@Slf4j
public class JavalinService {
    private final Javalin service;
    private long startServiceTime;

    public JavalinService() {
        log.info("Initializing javalin web service");
        ServiceProperties.init("service.properties", "build.properties");
        service = createJavalinService();
        log.info("Successfully initialized javalin web service");
    }

    public void start() {
        log.info("Starting javalin web service");
        service.start(Integer.parseInt(ServiceProperties.getProperty("service.port").orElseThrow(() -> new MissingPropertyException("service.port"))));
        log.info("Successfully started javalin web service");
    }

    private Javalin createJavalinService() {
        return Javalin.create()
                .events(serverStartingSteps())
                .events(serviceStartedEvent())
                .routes(routes())
                .updateConfig(setupRequestAuditEvent())
                .updateConfig(metrics());
    }

    private Consumer<JavalinConfig> metrics() {
        log.info("Setting up metrics");
        return (javalinConfig -> {
            javalinConfig.plugins.register(ServiceMetrics.getMicrometerPlugin());
        });
    }

    private Consumer<JavalinConfig> setupRequestAuditEvent() {
        log.info("Setting up request audit event");
        return (javalinConfig -> javalinConfig.requestLogger.http((ctx, ms) -> {
            String requestPath = ctx.endpointHandlerPath();
            HttpStatus httpStatus = ctx.status();
            HandlerType method = ctx.method();
            String response;
            if (ctx.status().getCode() < 400) {
                response = "OK";
            } else {
                response = ctx.result();
            }
            ServiceEventHandler.addEvent(new Audit(requestPath, String.valueOf(httpStatus.getCode()), method.toString(), response, ms));
        }));
    }

    private Consumer<EventListener> serverStartingSteps() {
        log.info("Setting up server starting steps");
        return (eventListener -> eventListener.serverStarting(() -> {
            try {
                startServiceTime = System.currentTimeMillis();
                ServiceEventHandler.init();
                FitnessDataDB.init();
            } catch (Exception e) {
                log.error("Issue during startup", e);
                System.exit(1);
            }
        }));
    }

    private Consumer<EventListener> serviceStartedEvent() {
        log.info("Setting up server started steps");
        return (eventListener -> eventListener.serverStarted(() -> {
            long serviceStartDuration = System.currentTimeMillis() - startServiceTime;
            try {
                String buildVersion = ServiceProperties.getProperty("service.version").orElseThrow(() -> new MissingPropertyException("service.version"));
                String gitBranch = ServiceProperties.getProperty("git.branch").orElseThrow(() -> new MissingPropertyException("git.branch"));
                String gitCommitId = ServiceProperties.getProperty("git.commit.id").orElseThrow(() -> new MissingPropertyException("git.commit.id"));
                long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
                ServiceEventHandler.addEvent(new ServiceStart(buildVersion, gitBranch, gitCommitId, startTime, serviceStartDuration));
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

