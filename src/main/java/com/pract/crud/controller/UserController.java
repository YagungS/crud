package com.pract.crud.controller;

import com.pract.crud.dto.UserDto;
import com.pract.crud.dto.UserSettingDto;
import com.pract.crud.service.UserService;
import com.pract.crud.service.UserSettingService;
import com.pract.crud.util.ErrorCodes;
import com.pract.crud.util.ObjectValidator;
import com.pract.crud.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    private final UserSettingService userSettingService;

    private final ObjectValidator validator;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                          @RequestParam(required = false, defaultValue = "5") Integer limit) {
        List<String> errors = new ArrayList<>();
        if (limit <= 0)
            errors.add(String.format(ErrorCodes.MSG_BAD_REQUEST, "limit", limit));
        if (offset < 0)
            errors.add(String.format(ErrorCodes.MSG_BAD_REQUEST, "offset", offset));
        if (!errors.isEmpty()) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.name(),
                    ErrorCodes.CODE_BAD_REQUEST,
                    errors);
        }
        try {
            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    userService.findAll(offset, limit),
                    null,
                    offset,
                    limit);
        } catch (Exception ex) {
            ex.printStackTrace();
            return generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserDto user) {
        List<String> errors = new ArrayList<>();
        errors = validator.validate(user);
        try {
            if (errors != null && !errors.isEmpty()) {
                return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.name(),
                        ErrorCodes.CODE_BAD_REQUEST,
                        errors);
            }

            if (userService.isExist(user.getSsn())) {
                return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT,
                        HttpStatus.CONFLICT.name(),
                        ErrorCodes.CODE_IS_EXIST,
                        errors.add(String.format(ErrorCodes.MSG_IS_EXIST, user.getSsn())));
            }

            UserDto result = userService.save(user);
            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    result,
                    userSettingService.findByUserId(result.getId()),
                    -1,
                    0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        List<String> errors = new ArrayList<>();
        try {
            UserDto user = userService.findById(id);
            if (user == null)
                return notFound(errors.add(String.format(ErrorCodes.MSG_NOT_FOUND, user.getSsn())));

            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    user,
                    userSettingService.findByUserId(user.getId()),
                    -1,
                    0);

        } catch (Exception ex) {
            ex.printStackTrace();
            return generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editUser(@RequestBody UserDto user) {
        List<String> errors = validator.validate(user);
        try {
            if (errors != null && !errors.isEmpty()) {
                return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.name(),
                        ErrorCodes.CODE_BAD_REQUEST,
                        errors);
            }

            UserDto result = userService.save(user);

            if (result == null) {
                return notFound(errors.add(String.format(ErrorCodes.MSG_NOT_FOUND, user.getId())));
            }

            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    result,
                    userSettingService.findByUserId(result.getId()),
                    -1,
                    0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<Object> editUserSetting(@PathVariable long id, @RequestBody List<UserSettingDto> userSettings) {
        List<String> errors = new ArrayList<>();
        try {
            if (!errors.isEmpty()) {
                return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.name(),
                        ErrorCodes.CODE_BAD_REQUEST,
                        errors);
            }

            if (!userService.isExist(id))
                return notFound(errors.add(String.format(ErrorCodes.MSG_NOT_FOUND, id)));

            UserDto result = userService.findById(id);
            List<UserSettingDto> settings = userSettingService.update(userSettings, id);
            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    result,
                    settings,
                    -1,
                    0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    @DeleteMapping("/{id}")
    public <T> ResponseEntity<T> deleteUser(@PathVariable long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (!userService.isExist(id))
                return (ResponseEntity<T>) notFound(errors.add(String.format(ErrorCodes.MSG_NOT_FOUND, id)));

            userService.delete(id);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return (ResponseEntity<T>) generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    @PutMapping("/{id}/refresh")
    public ResponseEntity<Object> refreshUser(@PathVariable long id) {

        List<String> errors = new ArrayList<>();
        try{
            if (!userService.isExist(id))
                return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.name(),
                        ErrorCodes.CODE_NOT_FOUND,
                        errors.add(String.format(ErrorCodes.MSG_NOT_FOUND, id)));

            UserDto user = userService.refresh(id);

            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    user,
                    userSettingService.findByUserId(user.getId()),
                    -1,
                    0);

        } catch (Exception ex) {
            ex.printStackTrace();
            return generalError(errors.add(ErrorCodes.MSG_INTERNAL_ERROR));
        }
    }

    public ResponseEntity<Object> generalError(Object errors) {
        return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ErrorCodes.CODE_INTERNAL_ERROR,
                errors);
    }

    public ResponseEntity<Object> notFound(Object errors) {
        return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.name(),
                ErrorCodes.CODE_NOT_FOUND,
               errors);
    }

}
