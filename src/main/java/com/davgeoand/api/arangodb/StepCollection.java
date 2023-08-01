package com.davgeoand.api.arangodb;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.DocumentCreateEntity;
import com.davgeoand.api.model.Step;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StepCollection {
    private static ArangoCollection arangoCollection;

    public static void init() {
        log.info("Initializing step collection");
        arangoCollection = FitnessDataDB.getArangoCollection("steps");
        log.info("Successfully initialized step collection");
    }

    public static DocumentCreateEntity<Void> insertStep(Step step) {
        log.info("Inserting step into collection");
        DocumentCreateEntity<Void> stepDocumentCreateEntity = arangoCollection.insertDocument(step);
        log.info("Successfully inserted step into collection");
        return stepDocumentCreateEntity;
    }
}
