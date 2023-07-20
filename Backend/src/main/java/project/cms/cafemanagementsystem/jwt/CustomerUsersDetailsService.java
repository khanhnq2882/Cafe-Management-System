package project.cms.cafemanagementsystem.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.cms.cafemanagementsystem.entity.User;
import project.cms.cafemanagementsystem.repository.UserRepository;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private User userDetail;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userRepository.findByEmail(username);
        if(!Objects.isNull(userDetail)){
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not found!");
        }
    }

    public User getUserDetail(){
        return userDetail;
    }
}
