package com.hanaah.iptiq.taskmanager.exceptions;

import java.util.UUID;

public class ProcessNotFoundException extends RuntimeException {

    public ProcessNotFoundException(UUID processId) {
        super(String.format("Process with id [%s] was not found", processId.toString()));
    }
}
