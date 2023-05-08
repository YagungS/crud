package com.pract.crud.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pract.crud.dto.UserDto;
import com.pract.crud.service.UserService;
import com.pract.crud.util.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    private final ObjectValidator validator;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public String save(@RequestBody UserDto user) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        List<String> violations = validator.validate(user);

        try {
            if(violations!=null && !violations.isEmpty()) {
                return mapper.writeValueAsString(violations);
            }
            UserDto result = userService.save(user);
            return mapper.writeValueAsString(result);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "error 500";
        }
    }

    @DeleteMapping()
    public String deleteUser(){
        return null;
    }

    /*@RequestMapping(value = "/api/something", method = RequestMethod.GET)
    public ResponseEntity something() {
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }*/
}
