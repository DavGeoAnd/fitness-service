package com.davgeoand.api.arangodb;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.DocumentCreateEntity;
import com.davgeoand.api.model.StepConnection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StepConnectionCollection {
    private static ArangoCollection arangoCollection;

    public static void init() {
        log.info("Initializing stepConnection collection");
        arangoCollection = FitnessDataDB.getArangoCollection("stepConnections");
        log.info("Successfully initialized stepConnection collection");
    }
    public static DocumentCreateEntity<Void> insertStepConnection(StepConnection stepConnection) {
        log.info("Inserting stepConnection into collection");
        DocumentCreateEntity<Void> stepConnectionDocumentCreateEntity = arangoCollection.insertDocument(stepConnection);
        log.info("Successfully inserted stepConnection into collection");
        return stepConnectionDocumentCreateEntity;
    }
}
