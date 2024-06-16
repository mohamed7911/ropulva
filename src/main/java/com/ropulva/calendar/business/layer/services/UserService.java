package com.ropulva.calendar.business.layer.services;

import com.ropulva.calendar.business.layer.dtos.BaseDTO;
import com.ropulva.calendar.business.layer.dtos.SignUpRequestDTO;
import com.ropulva.calendar.business.layer.exception.BusinessException;
import com.ropulva.calendar.data.layer.model.User;

public interface UserService {

    BaseDTO<String> signUp(SignUpRequestDTO signUpRequestDTO) throws BusinessException;

    User validateUser(String username) throws BusinessException;

    BaseDTO<String> verifyUser(String username) throws BusinessException;
}
