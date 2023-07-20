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
@Table(name = "ward")
public class Ward{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wardId")
    private Integer wardId;

    @Column(name = "wardName")
    private String wardName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "districtId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private District district;

}
