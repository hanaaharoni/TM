package com.hanaah.taskmanager.exceptions;

import java.util.UUID;

public class ProcessAlreadyExistsException extends Exception {

    public ProcessAlreadyExistsException(UUID processId) {
        super(String.format("Process [%s] already exists!", processId.toString()));
    }
}
