package project.cms.cafemanagementsystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email = :email")
@NamedQuery(name = "User.getAllUser", query = "select new project.cms.cafemanagementsystem.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) from User u where u.role = 'user'")
@NamedQuery(name = "User.updateStatus", query = "update User u set u.status = :status where u.id = :id")
@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role = 'admin'")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, allowSetters = true)
    private Address address;

}
