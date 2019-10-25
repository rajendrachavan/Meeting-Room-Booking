package neo.spring5.MeetingRoomBooking.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="role")
	private String role;

	@OneToMany(mappedBy = "role")
	private Set<User> users;
}
