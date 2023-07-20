package project.cms.cafemanagementsystem.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId")
    private Integer addressId;

    @Column(name = "specificAddress")
    private String specificAddress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wardId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ward ward;

}
