package com.davgeoand.api.service;

import com.arangodb.entity.DocumentCreateEntity;
import com.davgeoand.api.arangodb.StepCollection;
import com.davgeoand.api.model.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepService {
    public StepService() {
        log.info("Initializing step service");
        log.info("Successfully initialized step service");
    }
    public String createStep(Step step) {
        log.info("Creating step");
        DocumentCreateEntity<Void> documentCreateEntity = StepCollection.insertStep(step);
        String createdStepKey = documentCreateEntity.getKey();
        log.info("Successfully created step");
        return createdStepKey;
    }
}
