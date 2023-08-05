package com.davgeoand.api.controller;

import com.davgeoand.api.ServiceProperties;
import com.davgeoand.api.monitor.metric.ServiceMetricRegistry;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.micrometer.core.instrument.Meter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static io.javalin.apibuilder.ApiBuilder.get;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiController {
    public static EndpointGroup getApiEndpoints() {
        log.info("Returning api endpoints");
        return () -> {
            get("health", ApiController::health);
            get("jars", ApiController::jars);
            get("info", ApiController::info);
            get("metrics", ApiController::metrics);
        };
    }

    private static void metrics(Context context) {
        log.info("Retrieving metrics");
        ArrayList<Meter> meterArrayList = new ArrayList<>();
        ServiceMetricRegistry.getMeterRegistry().forEachMeter((meterArrayList::add));
        context.json(meterArrayList);
        context.status(HttpStatus.OK);
        log.info("Successfully retrieved metrics");
    }

    private static void info(Context context) {
        log.info("Retrieving info properties");
        context.json(ServiceProperties.getInfoPropertiesMap());
        context.status(HttpStatus.OK);
        log.info("Successfully retrieved info properties");
    }

    private static void jars(Context context) {
        log.info("Retrieving all jars used in service");
        String classpath = System.getProperty("java.class.path");
        String[] classPathValues = classpath.split(File.pathSeparator);
        ArrayList<String> jarsList = new ArrayList<>(Arrays.asList(classPathValues));
        context.json(jarsList);
        context.status(HttpStatus.OK);
        log.info("Successfully retrieved all jars used in service");
    }

    private static void health(Context context) {
        log.info("Performing healthcheck");
        context.status(HttpStatus.OK);
        log.info("Successfully performed healthcheck");
    }
}
