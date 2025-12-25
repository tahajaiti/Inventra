package com.hnaya.inventra.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Class<?> clazz, Long id) {
        super(clazz.getSimpleName() + " not found with id: " + id);
    }

    public ResourceNotFoundException(Class<?> clazz, String message) {
        super(clazz.getSimpleName() + " not found with field: " + message);
    }
}
