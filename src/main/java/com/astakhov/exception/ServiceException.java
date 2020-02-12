package com.astakhov.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Base exception for all transfer service-related exceptions.
 */
public class ServiceException extends RuntimeException {
    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final String message, final JsonProcessingException cause) {
        super(message, cause);
    }
}
