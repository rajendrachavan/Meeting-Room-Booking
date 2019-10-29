package neo.spring5.MeetingRoomBooking.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

@Getter
@Setter

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@Transient
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "department")
	private String department;

	@Column(name = "active")
	private int active;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Role role;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", gender='" + gender + '\'' +
				", mobileNo='" + mobileNo + '\'' +
				", department='" + department + '\'' +
				", active=" + active +
				", role=" + role +
				'}';
	}
}
