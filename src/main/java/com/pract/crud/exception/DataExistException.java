package com.pract.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataExistException extends ResponseStatusException {
    public DataExistException(String element) {
        super(HttpStatus.CONFLICT, element);
    }
}

