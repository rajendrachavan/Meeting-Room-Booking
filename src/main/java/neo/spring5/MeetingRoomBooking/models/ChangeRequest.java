package neo.spring5.MeetingRoomBooking.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Type;
    private String oldValue;
    private String newValue;
    private String status;

    @ManyToOne
    private User user;
}