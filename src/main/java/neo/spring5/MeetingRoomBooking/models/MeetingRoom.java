package neo.spring5.MeetingRoomBooking.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "meeting_room")
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String status;

    @ManyToMany
    private Set<Facilities> facilities;

    @OneToMany(mappedBy = "meetingRoom")
    private Set<BookingDetails> bookingDetails;
}
