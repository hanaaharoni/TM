package com.hanaah.iptiq.exception.handler;

import com.hanaah.iptiq.dto.ErrorDto;
import com.hanaah.iptiq.taskmanager.exceptions.MaximumCapacityReachedException;
import com.hanaah.iptiq.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.iptiq.taskmanager.exceptions.ProcessNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
        MaximumCapacityReachedException.class,
        IllegalArgumentException.class,
        ProcessNotFoundException.class,
        ProcessAlreadyExistsException.class
    })
    protected ResponseEntity<Object> handleConflict(Exception exception,
        WebRequest request) {

        if (exception instanceof ProcessNotFoundException) {
            ErrorDto errorDto = ErrorDto.builder().cause(exception.getMessage()).build();
            return handleExceptionInternal(exception, errorDto, new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
        }
        if (exception instanceof MaximumCapacityReachedException
            || exception instanceof ProcessAlreadyExistsException
            || exception instanceof IllegalArgumentException) {
            ErrorDto errorDto = ErrorDto.builder().cause(exception.getMessage()).build();
            return handleExceptionInternal(exception, errorDto, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
        }

        log.error("Unhandled exception occurred: {}", exception.getMessage(), exception);
        ErrorDto errorDto = ErrorDto.builder().cause("Internal Server Error").build();
        return handleExceptionInternal(exception, errorDto, new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
