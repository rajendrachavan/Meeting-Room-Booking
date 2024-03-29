package neo.spring5.MeetingRoomBooking.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Set;


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
    @Length(max = 10, min = 10, message = "*Invalid mobile number")
	private String mobileNo;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Department department;

	@Column(name = "active")
	private int active;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<BookingDetails> bookingDetails;


	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ChangeRequest> changeRequests;

	@ManyToOne
	private User parent;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Feedback> feedback;

	@OneToMany(mappedBy = "to", cascade = CascadeType.ALL)
	private List<Notification> notifications;
}
