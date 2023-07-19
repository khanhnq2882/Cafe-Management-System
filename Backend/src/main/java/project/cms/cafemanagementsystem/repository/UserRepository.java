package project.cms.cafemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.cms.cafemanagementsystem.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    Boolean isEmailExist(String email);

    Boolean isPhoneNumberExist(String phoneNumber);


}
