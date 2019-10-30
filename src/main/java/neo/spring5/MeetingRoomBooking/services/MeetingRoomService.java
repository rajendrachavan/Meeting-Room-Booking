package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.MeetingRoom;

import java.util.List;
import java.util.Optional;

public interface MeetingRoomService {

    public void save(MeetingRoom meetingRoom);
    public List<MeetingRoom> findAll();
    public Optional<MeetingRoom> findById(Long id);
    public void deleteById(Long id);
    public MeetingRoom findMeetingRoomByName(String name);
}
