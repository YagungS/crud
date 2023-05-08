package com.pract.crud.util;

import jakarta.validation.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private final Validator validator = factory.getValidator();

    public <T> List<String> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(tConstraintViolation ->
                        String.format( "Invalid value for field %s, rejected value : %s",
                                tConstraintViolation.getPropertyPath().toString(),
                                tConstraintViolation.getInvalidValue().toString())
                    ).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
