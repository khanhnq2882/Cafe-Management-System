package project.cms.cafemanagementsystem.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;

    private String name;

    private String email;

    private String contactNumber;

    private String status;


}
