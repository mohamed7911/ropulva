package com.ropulva.calendar.application.layer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    public <D> ResponseEntity success(D results) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(results);
    }

    public ResponseEntity success(String results) {

        Map<String,String> successObject = new HashMap<>();
        successObject.put("status","0");
        successObject.put("description",results);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(successObject);
    }

}
