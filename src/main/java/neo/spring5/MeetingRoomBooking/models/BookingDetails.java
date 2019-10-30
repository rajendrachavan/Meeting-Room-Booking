package neo.spring5.MeetingRoomBooking.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "booking_details")
public class BookingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate localDate;
    private String status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MeetingRoom meetingRoom;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

}
