package project.cms.cafemanagementsystem.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.cms.cafemanagementsystem.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get-all-user")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping("/update-user")
    public ResponseEntity<String> updateUser(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String, String> requestMap);
}
