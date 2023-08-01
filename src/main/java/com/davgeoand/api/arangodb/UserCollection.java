package com.davgeoand.api.arangodb;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.DocumentCreateEntity;
import com.davgeoand.api.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCollection {
    private static ArangoCollection arangoCollection;

    public static void init() {
        log.info("Initializing user collection");
        arangoCollection = FitnessDataDB.getArangoCollection("users");
        log.info("Successfully initialized user collection");
    }

    public static DocumentCreateEntity<Void> insertUser(User user) {
        log.info("Inserting user into collection");
        DocumentCreateEntity<Void> userDocumentCreateEntity = arangoCollection.insertDocument(user);
        log.info("Successfully inserted user into collection");
        return userDocumentCreateEntity;
    }

    public static Optional<User> getUserByKey(String key) {
        log.info("Getting user");
        User userFound = arangoCollection.getDocument(key, User.class);
        log.info("Successfully got user");
        return Optional.ofNullable(userFound);
    }
}
