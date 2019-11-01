package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MeetingRoomService {

    void save(MeetingRoom meetingRoom);
    List<MeetingRoom> findAll();
    Optional<MeetingRoom> findById(Long id);
    void deleteById(Long id);
    MeetingRoom findMeetingRoomByName(String name);
    Page<MeetingRoom> getPaginatedMeetingRooms(Pageable pageable);
}
