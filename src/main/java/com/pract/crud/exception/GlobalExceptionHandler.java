package com.pract.crud.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pract.crud.util.ErrorCodes;
import com.pract.crud.util.ErrorMsg;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String TRACE = "trace";
    @Value("${reflectoring.trace:true}")
    private boolean printStackTrace;

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ErrorCodes.CODE_BAD_REQUEST);

        getRejectedJsonProperties(ex).entrySet()
                .forEach(entrySet ->
                    errorResponse.addError(String.format(ErrorMsg.MSG_BAD_REQUEST,entrySet.getKey(), entrySet.getValue()))
                );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /*@Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ErrorCodes.CODE_BAD_REQUEST);
        errorResponse.addError(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }*/

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoDataFoundException itemNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.name(), ErrorCodes.CODE_NOT_FOUND);
        errorResponse.addError(String.format(ErrorMsg.MSG_NOT_FOUND, itemNotFoundException.getMessage()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletResponse response = servletWebRequest.getResponse();
            if (response != null && response.isCommitted()) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Response already committed. Ignoring: " + ex);
                }

                return null;
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), ErrorCodes.CODE_INTERNAL_ERROR);

        if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            errorResponse.addError(String.format(ErrorMsg.MSG_INTERNAL_ERROR));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (body == null && ex instanceof org.springframework.web.ErrorResponse errorResponses) {
            body = errorResponses.updateAndGetBody(super.getMessageSource(), LocaleContextHolder.getLocale());
        }

        return this.createResponseEntity(body, headers, statusCode, request);
    }

    private Map<String, Object> getRejectedJsonProperties(MethodArgumentNotValidException e) {

        Map<String, Object> rejectedProps = new HashMap<>();
                e.getBindingResult().getFieldErrors()
                .forEach(field -> {
                    rejectedProps.put(field.getField(), field.getRejectedValue());
                });

        Arrays.stream(e.getParameter().getParameterType().getDeclaredFields())
                .filter(f -> rejectedProps.containsKey(f.getName()))
                .filter(f -> f.isAnnotationPresent(JsonProperty.class) && !f.getAnnotation(JsonProperty.class).value().isEmpty())
                .forEach(f -> rejectedProps.put(f.getAnnotation(JsonProperty.class).value(), rejectedProps.remove(f.getName())));

        return rejectedProps;
    }


}
