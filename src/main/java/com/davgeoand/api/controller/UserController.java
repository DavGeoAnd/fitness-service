package com.davgeoand.api.controller;

import com.davgeoand.api.helper.Constants;
import com.davgeoand.api.model.User;
import com.davgeoand.api.service.UserService;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.javalin.apibuilder.ApiBuilder.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {
    private static final UserService userService = new UserService();

    public static EndpointGroup getUsersEndpoints() {
        log.info("Returning users endpoints");
        return () -> {
            post(UserController::postUserRequest);
            path(Constants.BASE_PATH_PARAM_ENDPOINT, () -> {
                get(UserController::getUserByKeyRequest);
                path("stepConnections", () -> {
                    get(UserController::getStepConnectionsForUserRequest);
                });
            });
        };
    }

    private static void getStepConnectionsForUserRequest(Context context) {
        log.info("Starting getting stepConnections for user request");
        String userKey = context.pathParam(Constants.BASE_PATH_PARAM);
        context.json(userService.getUserStepConnectionsForUser(userKey));
        context.status(HttpStatus.OK);
        log.info("Finished getting stepConnections for user request");
    }

    private static void getUserByKeyRequest(Context context) {
        log.info("Starting getting user by key request");
        String userKey = context.pathParam(Constants.BASE_PATH_PARAM);
        context.json(userService.getUser(userKey));
        context.status(HttpStatus.OK);
        log.info("Finished getting user by key request");
    }

    private static void postUserRequest(Context context) {
        log.info("Starting creating user request");
        User bodyUser = context.bodyAsClass(User.class);
        context.json(userService.createUser(bodyUser));
        context.status(HttpStatus.CREATED);
        log.info("Finished creating user request");
    }
}
