package neo.spring5.MeetingRoomBooking.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role")
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="role_id")
	private Long id;
	@Column(name="role")
	private String role;
}
