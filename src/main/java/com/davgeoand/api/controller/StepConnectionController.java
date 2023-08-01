package com.davgeoand.api.controller;

import com.davgeoand.api.model.StepConnection;
import com.davgeoand.api.service.StepConnectionService;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.javalin.apibuilder.ApiBuilder.post;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StepConnectionController {
    private static final StepConnectionService stepConnectionService = new StepConnectionService();

    public static EndpointGroup getStepConnectionsEndpoints() {
        log.info("Returning stepConnections endpoints");
        return () -> {
            post(StepConnectionController::postStepConnectionRequest);
        };
    }

    private static void postStepConnectionRequest(Context context) {
        log.info("Starting creating stepConnection request");
        StepConnection bodyStepConnection = context.bodyAsClass(StepConnection.class);
        context.json(stepConnectionService.createStepConnection(bodyStepConnection));
        context.status(HttpStatus.CREATED);
        log.info("Finished creating stepConnection request");
    }
}
