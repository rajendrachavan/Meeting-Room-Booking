package neo.spring5.MeetingRoomBooking.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "department")
    private List<User> users;
}
