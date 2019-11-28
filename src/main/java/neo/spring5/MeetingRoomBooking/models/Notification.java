package neo.spring5.MeetingRoomBooking.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User to;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Notification() {
    }

    public Notification(User to, String description, Type type, Status status) {
        this.to = to;
        this.description = description;
        this.type = type;
        this.status = status;
    }
}
