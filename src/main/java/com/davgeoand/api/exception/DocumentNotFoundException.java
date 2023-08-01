package com.davgeoand.api.exception;

import com.arangodb.ArangoDBException;

public class DocumentNotFoundException extends ArangoDBException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
