package com.hanaah.iptiq.exception;

import java.util.UUID;

public class ProcessAlreadyExistsException extends Exception {

    public ProcessAlreadyExistsException(UUID processId) {
        super(String.format("Process [%s] already exists!", processId.toString()));
    }
}
