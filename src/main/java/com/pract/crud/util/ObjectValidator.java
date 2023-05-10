package com.pract.crud.util;

import jakarta.validation.*;
import org.springframework.stereotype.Component;

import java.util.*;
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
                    ).toList();
        }

        return Collections.emptyList();
    }
    public List<String> validateLOV(Map<String, String> source, Map<String, String> input) {
        List<String> errors = new ArrayList<>();

        for (String key : source.keySet()) {
            if (!input.containsKey(key)){
                errors.add("Invalid value for field " + key + ", rejected value : " + "null");
            }

            String value = input.get(key);
            if (key.equals(Constant.WIDGEDT_ORDER)){
                if(value.matches("^[1-5](,[1-5])*$")){
                    List<String> order = Arrays.stream(value.split(",")).toList();
                    if(order.size()!=5 || isDuplicated(order)) {
                        errors.add("Invalid value for field " + key + ", rejected value : " + value);
                    }
                }
            } else {
                if (!value.equals("false") && !value.equals("true")){
                    errors.add("Invalid value for field " + key + ", rejected value : " + value);
                }
            }
        }
        return errors;
    }

    private boolean isDuplicated(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            for (int j = i + 1; j < input.size(); j++) {
                if (input.get(i).equals(input.get(j)) ) {
                    System.out.println("DUPLICATION "+input.get(i));
                    return true;
                }
            }
        }
        return false;
    }

}
