package com.davgeoand.api.exception;

public class InvalidEventHandlerType extends Exception {
    public InvalidEventHandlerType(String type) {
        super(type + " is not a valid EventHandler type");
    }
}
