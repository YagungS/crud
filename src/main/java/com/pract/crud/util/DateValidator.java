package com.pract.crud.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.pract.crud.dto.UserDto;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator extends StdDeserializer {
    public DateValidator() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctx) {
        UserDto user = new UserDto();
        BindingResult binding_R = new BeanPropertyBindingResult(user, "user");
        try {
            String value = p.readValueAs(String.class);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            Date birthDate = format.parse(value);
            if (isGreaterOrEqual100Year(birthDate) == true) {
                binding_R.rejectValue("birthDate", "30002");
                throw new MethodArgumentNotValidException(new MethodParameter(
                        this.getClass().getDeclaredMethod("setDate", UserDto.class), 0), binding_R);

            }

            return birthDate;
        } catch (Exception e) {
            binding_R.rejectValue("birthDate", "30002");
            try {
                throw new MethodArgumentNotValidException(new MethodParameter(
                        this.getClass().getDeclaredMethod("setDate", UserDto.class), 0), binding_R);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private Boolean isGreaterOrEqual100Year(Date date) {
        return Util.getAge(date).getYears() >= 100;
    }
}

