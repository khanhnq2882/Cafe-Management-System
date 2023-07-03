package project.cms.cafemanagementsystem.wrapper;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {

    private Integer id;

    private String name;

    private String email;

    private String contactNumber;

    private String status;


}
