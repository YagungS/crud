package com.pract.crud.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateErrorResponse(HttpStatus status, String statuscode, int errorcode, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", statuscode);
        map.put("code", errorcode);
        map.put("message", responseObj);

        return new ResponseEntity<Object>(map,status);
    }

    public static ResponseEntity<Object> generateSuccessResponse(HttpStatus status, String statuscode,Object userData, Object usersettings, int offset, int max_record) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_data", userData);
        map.put("user_settings", usersettings);
        map.put("max_record", max_record == 0? null:max_record);
        map.put("offset", offset == -1? null:offset);

        return new ResponseEntity<Object>(map,status);
    }
}
