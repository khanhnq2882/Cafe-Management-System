package project.cms.cafemanagementsystem.serviceImpl;

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
import project.cms.cafemanagementsystem.entity.User;
import project.cms.cafemanagementsystem.jwt.CustomerUsersDetailsService;
import project.cms.cafemanagementsystem.jwt.JwtFilter;
import project.cms.cafemanagementsystem.jwt.JwtUtil;
import project.cms.cafemanagementsystem.repository.UserRepository;
import project.cms.cafemanagementsystem.service.UserService;
import project.cms.cafemanagementsystem.utils.CafeUtils;
import project.cms.cafemanagementsystem.utils.EmailUtils;
import project.cms.cafemanagementsystem.utils.RandomPasswordUtils;
import project.cms.cafemanagementsystem.wrapper.UserWrapper;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                    return CafeUtils.getResponseEntity("Email is already exist!", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<List<UserWrapper>> getAllUser() {
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
            User user = getCurrenUser();
            if(!Objects.isNull(user)){
                if(requestMap.get("oldPassword").equals(user.getPassword())){
                    if(requestMap.get("newPassword").equals(requestMap.get("confirmPassword"))){
                        user.setPassword(requestMap.get("newPassword"));
                        userRepository.save(user);
                        return CafeUtils.getResponseEntity("Change password successfully!", HttpStatus.OK);
                    }else{
                        return CafeUtils.getResponseEntity("Confirm password is not match with new password!", HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return CafeUtils.getResponseEntity("Password is incorrect!", HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity("User is not exist!", HttpStatus.BAD_REQUEST);
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
                String pattern = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$";
                String newPassword = randomPasswordUtils.generatePassword(pattern, 8, 25);
                emailUtils.sendRandomPasswordToEmail(
                        requestMap.get("email"),
                        "Change password to email "+requestMap.get("email"),
                        "Your password has changed."+"\n"+"New password is "+newPassword
                );
                user.setPassword(newPassword);
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

    public User getCurrenUser(){
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

}
