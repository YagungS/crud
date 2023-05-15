package com.pract.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDataFoundException extends ResponseStatusException {
    public NoDataFoundException(String element){
        super(HttpStatus.NOT_FOUND, element);
    }
}
