package neo.spring5.MeetingRoomBooking.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "facilities")
public class Facilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String facilities;

    @ManyToMany(mappedBy = "facilities", cascade = CascadeType.PERSIST)
    private List<MeetingRoom> meetingRoom;
}
