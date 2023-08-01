package com.davgeoand.api.arangodb;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.davgeoand.api.ServiceProperties;
import com.davgeoand.api.exception.MissingPropertyException;
import com.davgeoand.api.model.user.UserStepConnection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FitnessDataDB {
    private static ArangoDatabase arangoDatabase;

    public static void init() {
        log.info("Initializing fitness data arangodb database");
        connect();
        UserCollection.init();
        StepCollection.init();
        StepConnectionCollection.init();
        log.info("Successfully initialized fitness data arangodb database");
    }

    private static void connect() {
        log.info("Connecting to fitness data arangodb database");
        ArangoDB arangoDB = new ArangoDB.Builder()
                .host(ServiceProperties.getProperty("fitness.data.arangodb.host").orElseThrow(() -> new MissingPropertyException("fitness.data.arangodb.host")),
                        Integer.parseInt(ServiceProperties.getProperty("fitness.data.arangodb.port").orElseThrow(() -> new MissingPropertyException("fitness.data.arangodb.port"))))
                .user(ServiceProperties.getProperty("fitness.data.arangodb.username").orElseThrow(() -> new MissingPropertyException("fitness.data.arangodb.username")))
                .password(ServiceProperties.getProperty("fitness.data.arangodb.password").orElseThrow(() -> new MissingPropertyException("fitness.data.arangodb.password")))
                .build();
        arangoDatabase = arangoDB.db(ServiceProperties.getProperty("fitness.data.arangodb.name").orElseThrow(() -> new MissingPropertyException("fitness.data.arangodb.name")));
        log.info("Version of ArangoDB database: " + arangoDatabase.getVersion().getVersion());
        log.info("Successfully connected to fitness data arangodb database");
    }

    public static ArangoCollection getArangoCollection(String collectionName) {
        log.info("Getting arangodb collection for " + collectionName);
        ArangoCollection arangoCollection = arangoDatabase.collection(collectionName);
        log.info("ArangoDB collection: " + collectionName + " has id: " + arangoCollection.getInfo().getId());
        log.info("Successfully got arangodb collection for " + collectionName);
        return arangoCollection;
    }

    public static ArangoCursor<UserStepConnection> getUserStepConnectionsForUser(String userKey) {
        log.info("Getting all userStepConnections for user");
        String query = "FOR sc IN stepConnections FILTER sc._from == @userId FOR s IN steps FILTER s._id == sc._to RETURN { type: sc.type, key:sc._key, step: s}";
        Map<String, Object> bindVars = Collections.singletonMap("userId", "users/" + userKey);
        ArangoCursor<UserStepConnection> userStepConnectionArangoCursor = arangoDatabase.query(query, UserStepConnection.class, bindVars);
        log.info("Successfully got all userStepConnections for user");
        return userStepConnectionArangoCursor;
    }
}
