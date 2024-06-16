package com.ropulva.calendar.business.layer.services.Impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.ropulva.calendar.business.layer.dtos.BaseDTO;
import com.ropulva.calendar.business.layer.dtos.SignUpRequestDTO;
import com.ropulva.calendar.business.layer.enums.ResponseStatusEnum;
import com.ropulva.calendar.business.layer.services.UserService;
import com.ropulva.calendar.business.layer.exception.BusinessException;
import com.ropulva.calendar.data.layer.model.User;
import com.ropulva.calendar.data.layer.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ropulva.calendar.business.layer.services.Impl.TasksQuickstart.getCredentials;

@Service
@CommonsLog
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public BaseDTO<String> signUp(SignUpRequestDTO signUpRequestDTO) throws BusinessException {
        try {
            log.debug("entering signUp func");

            if(userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()){
                throw new BusinessException("Email Already exists");
            }

            if(userRepository.findByEmail(signUpRequestDTO.getUsername()).isPresent()){
                throw new BusinessException("Username Already exists");
            }

            User user = new User();

            user.setEmail(signUpRequestDTO.getEmail());
            user.setUsername(signUpRequestDTO.getUsername());

            userRepository.save(user);

            BaseDTO<String> baseDTO = new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), "User Created Successfully", true);
            log.debug("Success signUp func");

            return baseDTO;
        }catch (BusinessException e){
            throw e;
        }
        catch (Exception e){
            throw new BusinessException("General Error");
        }
    }

    @Override
    public User validateUser(String username) throws BusinessException {
        log.debug("entering validateUser func");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User doesn't exist"));
        log.debug("Success validateUser func");

        return user;
    }

    @Override
    public BaseDTO<String> verifyUser(String username) throws BusinessException {
        try {
            log.debug("entering verifyUser func");

            User user = validateUser(username);
            if (user.isVerified()){
                throw new BusinessException("User is already verified");
            }
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentials(HTTP_TRANSPORT,username);
            user.setVerified(true);
            userRepository.save(user);
            log.debug("Success verifyUser func");

            return new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), "Success verified",true);
            }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
    }
}
