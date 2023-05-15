package com.pract.crud.controller;

import com.pract.crud.dto.UserDto;
import com.pract.crud.dto.UserSettingDto;
import com.pract.crud.exception.NoDataFoundException;
import com.pract.crud.service.UserService;
import com.pract.crud.service.UserSettingService;
import com.pract.crud.util.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    private final UserSettingService userSettingService;

    private final ObjectValidator validator;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
                                          @RequestParam(required = false, defaultValue = "5") @Min(1) Integer limit) {
        List<String> errors = new ArrayList<>();
        List<UserDto> users = userService.findAll(offset, limit);

        return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    userService.findAll(offset, limit),
                    null,
                    offset,
                    limit);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid UserDto user) {

        UserDto result = userService.save(user);
        return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                HttpStatus.OK.name(),
                result,
                Util.userSettingtoMap(userSettingService.findByUserId(result.getId())).entrySet().toArray(),
                -1,
                0);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        List<String> errors = new ArrayList<>();
        UserDto user = userService.findById(id);
        if (user == null)
            throw new NoDataFoundException(String.valueOf(id));

        return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                HttpStatus.OK.name(),
                user,
                Util.userSettingtoMap(userSettingService.findByUserId(user.getId())).entrySet().toArray(),
                -1,
                0);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editUser(@RequestBody @Valid UserDto user) {
            UserDto result = userService.save(user);

            return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                    HttpStatus.OK.name(),
                    result,
                    userSettingService.findByUserId(result.getId()),
                    -1,
                    0);
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<Object> editUserSetting(@PathVariable long id, @RequestBody String userSettingsString) throws JSONException {
        //TODO : custom validation
        List<String> errors = new ArrayList<>();
        JSONArray userSettings = new JSONArray(userSettingsString);

        Map<String, String> map = Util.jsonToUserSetting(userSettings);

        errors = validator.validateLOV(Constant.INITIAL_SETTINGS, map);

        if (!errors.isEmpty()) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.name(),
                    ErrorCodes.CODE_BAD_REQUEST,
                    errors);
        }

        if (!userService.isExist(id))
                throw new NoDataFoundException(String.valueOf(id));

        UserDto result = userService.findById(id);
        List<UserSettingDto> settings = userSettingService.update(Util.mapToUserSetting(map), id);

        return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                HttpStatus.OK.name(),
                result,
                settings,
                -1,
                0);
    }

    @DeleteMapping("/{id}")
    public <T> ResponseEntity<T> deleteUser(@PathVariable(value = "id") long id) {
        UserDto user = userService.findById(id);
        if (user == null)
            throw new NoDataFoundException(String.valueOf(id));

        userService.delete(user);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/refresh")
    public ResponseEntity<Object> refreshUser(@PathVariable long id) {
        UserDto user = userService.refresh(id);
        if (user == null) {
            throw new NoDataFoundException(String.valueOf(id));
        }

        return ResponseHandler.generateSuccessResponse(HttpStatus.OK,
                HttpStatus.OK.name(),
                user,
                Util.userSettingtoMap(userSettingService.findByUserId(user.getId())).entrySet().toArray(),
                -1,
                0);
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
