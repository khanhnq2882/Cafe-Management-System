package project.cms.cafemanagementsystem.service;

import org.springframework.http.ResponseEntity;
import project.cms.cafemanagementsystem.wrapper.UserWrapper;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> register(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<String> updateUser(Map<String, String> requestMap);
}
