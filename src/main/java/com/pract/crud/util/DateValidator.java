package com.pract.crud.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.pract.crud.dto.UserDto;
import lombok.SneakyThrows;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator extends StdDeserializer<Date> {
    public DateValidator() {
        super(Date.class);
    }

    @Override
    @SneakyThrows
    public Date deserialize(JsonParser p, DeserializationContext ctx) {
        UserDto user = new UserDto();
        BindingResult binding_R = new BeanPropertyBindingResult(user, "user");
        try {
            String value = p.readValueAs(String.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            Date birthDate = format.parse(value);
            if (isGreaterOrEqual100Year(birthDate) == true) {
                System.out.println("Throw #1");
                binding_R.rejectValue("birthDate", "30002");
                throw new MethodArgumentNotValidException(new MethodParameter(
                        UserDto.class.getDeclaredMethod("setBirthDate"), 0), binding_R);

            }

            return birthDate;
        } catch (Exception e) {
            System.out.println("Throw #2");
            throw new RuntimeException("test");
        }
    }

    private Boolean isGreaterOrEqual100Year(Date date) {
        return Util.getAge(date).getYears() >= 100;
    }
}

