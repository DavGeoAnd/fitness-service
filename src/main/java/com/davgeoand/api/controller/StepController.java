package com.davgeoand.api.controller;

import com.davgeoand.api.model.Step;
import com.davgeoand.api.service.StepService;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.javalin.apibuilder.ApiBuilder.post;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StepController {
    private static final StepService stepService = new StepService();

    public static EndpointGroup getStepsEndpoints() {
        log.info("Returning steps endpoints");
        return () -> {
            post(StepController::postStepRequest);
        };
    }

    private static void postStepRequest(Context context) {
        log.info("Starting creating step request");
        Step bodyStep = context.bodyAsClass(Step.class);
        context.json(stepService.createStep(bodyStep));
        context.status(HttpStatus.CREATED);
        log.info("Finished creating step request");
    }
}
