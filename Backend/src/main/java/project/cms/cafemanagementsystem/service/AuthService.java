package project.cms.cafemanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.cms.cafemanagementsystem.constants.CafeConstants;
import project.cms.cafemanagementsystem.entity.ERole;
import project.cms.cafemanagementsystem.entity.Role;
import project.cms.cafemanagementsystem.entity.User;
import project.cms.cafemanagementsystem.repository.UserRepository;
import project.cms.cafemanagementsystem.request.LoginRequest;
import project.cms.cafemanagementsystem.request.RegisterRequest;
import project.cms.cafemanagementsystem.response.ResponseMessage;
import project.cms.cafemanagementsystem.utils.CafeUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;


    public ResponseEntity<String> register(RegisterRequest registerRequest) {
        try{
            if(userRepository.isEmailExist(registerRequest.getEmail())){
                return ResponseMessage.getResponseEntity(CafeConstants.EMAIL_EXISTED, HttpStatus.BAD_REQUEST);
            }
            if(userRepository.isPhoneNumberExist(registerRequest.getPhoneNumber())){
                return ResponseMessage.getResponseEntity(CafeConstants.PHONE_NUMBER_EXISTED, HttpStatus.BAD_REQUEST);
            }
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setStatus("false");
            user.setRoles(registerRequest.getRoles());
            userRepository.save(user);
            return ResponseMessage.getResponseEntity("Register new account successfully!", HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        return null;
    }




}
