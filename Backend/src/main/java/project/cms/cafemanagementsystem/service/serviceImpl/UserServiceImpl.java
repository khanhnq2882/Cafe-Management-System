package project.cms.cafemanagementsystem.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.cms.cafemanagementsystem.constants.CafeConstants;
import project.cms.cafemanagementsystem.entity.User;
import project.cms.cafemanagementsystem.jwt.CustomerUsersDetailsService;
import project.cms.cafemanagementsystem.jwt.JwtFilter;
import project.cms.cafemanagementsystem.jwt.JwtUtil;
import project.cms.cafemanagementsystem.repository.UserRepository;
import project.cms.cafemanagementsystem.service.UserService;
import project.cms.cafemanagementsystem.utils.CafeUtils;
import project.cms.cafemanagementsystem.utils.EmailUtils;
import project.cms.cafemanagementsystem.utils.RandomPasswordUtils;
import project.cms.cafemanagementsystem.dto.UserDTO;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private CustomerUsersDetailsService customerUsersDetailsService;
    private JwtUtil jwtUtil;
    private JwtFilter jwtFilter;
    private EmailUtils emailUtils;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           CustomerUsersDetailsService customerUsersDetailsService,
                           JwtUtil jwtUtil, JwtFilter jwtFilter,
                           EmailUtils emailUtils,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customerUsersDetailsService = customerUsersDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.emailUtils = emailUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<String> register(Map<String, String> requestMap) {
        log.info("Inside register {}", requestMap);
        try{
            if(validateRegisterMap(requestMap)){
                User user = userRepository.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Register successfully!", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("Email is already exist. Try again!", HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if(authentication.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>(
                            "{\"token\":\""+ jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                    customerUsersDetailsService.getUserDetail().getRole()) +"\"}",
                            HttpStatus.OK);
                }else{
                    return new ResponseEntity<String>("{\"message\":\"Wait for admin approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\"Bad credentials"+"\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
               Optional<User> user = userRepository.findById(Integer.parseInt(requestMap.get("id")));
               if(!user.isEmpty()){
                   userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                   sendMailToAllAdmin(requestMap.get("status"), user.get().getEmail(), userRepository.getAllAdmin());
                   return CafeUtils.getResponseEntity("User status updated successfully!", HttpStatus.OK);
               }else{
                   CafeUtils.getResponseEntity("User is not exist", HttpStatus.OK);
               }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            if (validateChangePasswordMap(requestMap)) {
                if(!Objects.isNull(getCurrentUser())){
                    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                    if(bCryptPasswordEncoder.matches(requestMap.get("currentPassword"), getCurrentUser().getPassword())){
                        if(requestMap.get("newPassword").equals(requestMap.get("confirmPassword"))){
                            getCurrentUser().setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                            userRepository.save(getCurrentUser());
                            return CafeUtils.getResponseEntity("Change password successfully!", HttpStatus.OK);
                        }else{
                            return CafeUtils.getResponseEntity("Confirm password is not match with new password. Try again!", HttpStatus.BAD_REQUEST);
                        }
                    }else{
                        return CafeUtils.getResponseEntity("Current password is wrong. Try again!", HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return CafeUtils.getResponseEntity("User is not exist. Try again!", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity("Data is invalid. Try again!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user = userRepository.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user)){
                RandomPasswordUtils randomPasswordUtils = new RandomPasswordUtils();
                String newPassword = randomPasswordUtils.generatePassword("^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$", 8, 25);
                emailUtils.sendRandomPasswordToEmail(
                        requestMap.get("email"),
                        "Your password has been changed "+requestMap.get("email"),
                        "Your password has changed."+"\n"+"New password is "+newPassword
                );
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return CafeUtils.getResponseEntity("New password has send to your email!", HttpStatus.OK);
            }else{
                return CafeUtils.getResponseEntity("Your email is not exist!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }
        String email = authentication.getName();
        if(!authentication.isAuthenticated() || email == null){
            return null;
        }
        User user = userRepository.findByEmail(email);
        return user;
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:-"+user+" \n is approved by \nADMIN:-"+jwtFilter.getCurrentUser(), allAdmin);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Rejected", "USER:-"+user+" \n is rejected by \nADMIN:-"+jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    private boolean validateRegisterMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    private boolean validateChangePasswordMap(Map<String, String> requestMap){
        if(requestMap.containsKey("currentPassword") && requestMap.containsKey("newPassword") && requestMap.containsKey("confirmPassword")){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

}
