package com.pract.crud.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse(int offset, int max_record, Object user_data, Object user_settings) {
}
