package com.ropulva.calendar.application.layer.controller;


import com.ropulva.calendar.business.layer.exception.BusinessException;
import com.ropulva.calendar.business.layer.dtos.SignUpRequestDTO;
import com.ropulva.calendar.business.layer.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
@CommonsLog
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO){
        try {
            log.info("Enter signUp api with request body" + signUpRequestDTO.toString());
            return success(userService.signUp(signUpRequestDTO));
        }catch (BusinessException e){
            return success(e.getErrorObject());

        }
    }

    @PostMapping("/verify/{username}")
    public ResponseEntity<?> verify(@PathVariable String username){
        try {
            log.info("Enter verify api with PathVariable" + username);
            return success(userService.verifyUser(username));
        }catch (BusinessException e){
            return success(e.getErrorObject());
        }
    }
}
