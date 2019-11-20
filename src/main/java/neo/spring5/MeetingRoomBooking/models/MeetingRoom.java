package neo.spring5.MeetingRoomBooking.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

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

    @ManyToMany
    private List<Facility> facilities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meetingRoom")
    private List<BookingDetails> bookingDetails;
}
