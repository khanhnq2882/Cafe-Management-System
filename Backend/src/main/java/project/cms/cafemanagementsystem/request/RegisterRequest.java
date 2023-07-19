package project.cms.cafemanagementsystem.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.validation.constraints.Pattern;
import lombok.*;
import project.cms.cafemanagementsystem.entity.Role;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name;

    @Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}$", flags = Pattern.Flag.UNICODE_CASE)
    private String email;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]).{8,25}$", flags = Pattern.Flag.UNICODE_CASE)
    private String password;

    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", flags = Pattern.Flag.UNICODE_CASE)
    private String phoneNumber;

    private Set<Role> roles;

}
