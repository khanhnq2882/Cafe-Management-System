package project.cms.cafemanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.cms.cafemanagementsystem.dto.UserDTO;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:8888", maxAge = 3600, allowCredentials="true")
public interface UserController {

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get-all-user")
    public ResponseEntity<List<UserDTO>> getAllUser();

    @PostMapping("/update-user")
    public ResponseEntity<String> updateUser(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String, String> requestMap);
}
