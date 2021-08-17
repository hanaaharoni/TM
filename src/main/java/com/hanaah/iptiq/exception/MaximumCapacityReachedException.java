package com.hanaah.iptiq.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MaximumCapacityReachedException extends Exception {

    public MaximumCapacityReachedException(int capacity) {
        super(String.format("Maximum capacity of [%d] has been reached", capacity));
    }
}
