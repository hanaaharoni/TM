package com.hanaah.iptiq.exception;

import java.util.UUID;

public class ProcessNotFoundException extends Exception {

    public ProcessNotFoundException(UUID processId) {
        super(String.format("Process with id [%s] was not found", processId.toString()));
    }
}
