package com.example.taskconsumer.exception;

public class TaskProcessingException extends RuntimeException {
    public TaskProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
