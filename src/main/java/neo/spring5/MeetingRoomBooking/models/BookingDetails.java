package neo.spring5.MeetingRoomBooking.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "booking_details")
public class BookingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MeetingRoom meetingRoom;
    @ManyToOne
    private User user;

}
