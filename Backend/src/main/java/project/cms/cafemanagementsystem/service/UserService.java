package project.cms.cafemanagementsystem.service;

import org.springframework.http.ResponseEntity;
import project.cms.cafemanagementsystem.entity.User;
import project.cms.cafemanagementsystem.dto.UserDTO;
import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> register(Map<String, String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);
    ResponseEntity<List<UserDTO>> getAllUser();
    ResponseEntity<String> updateUser(Map<String, String> requestMap);
    ResponseEntity<String> changePassword(Map<String, String> requestMap);
    ResponseEntity<String> forgotPassword(Map<String, String> requestMap);
    User getCurrentUser();
}
