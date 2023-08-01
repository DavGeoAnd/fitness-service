package com.davgeoand.api.service;

import com.arangodb.entity.DocumentCreateEntity;
import com.davgeoand.api.arangodb.FitnessDataDB;
import com.davgeoand.api.arangodb.UserCollection;
import com.davgeoand.api.exception.DocumentNotFoundException;
import com.davgeoand.api.model.User;
import com.davgeoand.api.model.list.UserStepConnectionList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {
    public UserService() {
        log.info("Initializing user service");
        log.info("Successfully initialized user service");
    }

    public String createUser(User user) {
        log.info("Creating user");
        DocumentCreateEntity<Void> documentCreateEntity = UserCollection.insertUser(user);
        String createdUserKey = documentCreateEntity.getKey();
        log.info("Successfully created user");
        return createdUserKey;
    }

    public User getUser(String userKey) {
        log.info("Getting user");
        User foundUser = UserCollection.getUserByKey(userKey).orElseThrow(() -> new DocumentNotFoundException("User does not exist: " + userKey));
        log.info("Successfully got user");
        return foundUser;
    }

    public UserStepConnectionList getUserStepConnectionsForUser(String userKey) {
        log.info("Getting userStepConnections of user");
        UserStepConnectionList userStepConnectionList = new UserStepConnectionList();
        FitnessDataDB.getUserStepConnectionsForUser(userKey).forEach(userStepConnectionList::add);
        log.info("Successfully got userStepConnections of user");
        return userStepConnectionList;
    }
}
