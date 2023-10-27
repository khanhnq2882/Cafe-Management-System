package project.cms.cafemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import project.cms.cafemanagementsystem.entity.User;
import project.cms.cafemanagementsystem.dto.UserDTO;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    List<UserDTO> getAllUser();

    List<String> getAllAdmin();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);
}
