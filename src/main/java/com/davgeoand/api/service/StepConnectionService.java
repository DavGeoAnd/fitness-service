package com.davgeoand.api.service;

import com.arangodb.entity.DocumentCreateEntity;
import com.davgeoand.api.arangodb.StepConnectionCollection;
import com.davgeoand.api.model.StepConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepConnectionService {
    public StepConnectionService() {
        log.info("Initializing stepConnection service");
        log.info("Successfully initialized stepConnection service");
    }

    public String createStepConnection(StepConnection stepConnection) {
        log.info("Creating stepConnection");
        DocumentCreateEntity<Void> documentCreateEntity = StepConnectionCollection.insertStepConnection(stepConnection);
        String createdStepConnectionKey = documentCreateEntity.getKey();
        log.info("Successfully created stepConnection");
        return createdStepConnectionKey;
    }
}
