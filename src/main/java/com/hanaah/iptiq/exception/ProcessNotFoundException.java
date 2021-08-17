package com.hanaah.iptiq.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProcessNotFoundException extends RuntimeException {

    public ProcessNotFoundException(UUID processId) {
        super(String.format("Process with id [%s] was not found", processId.toString()));
    }
}
