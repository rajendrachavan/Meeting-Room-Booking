package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.ChangeRequest;
import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;

import java.util.List;

public interface ChangeRequestService {
    <Optional> ChangeRequest findByUser(User user);
    List<ChangeRequest> findAllByUserRole(Role role);
    ChangeRequest save(ChangeRequest changeRequest);
    List<ChangeRequest> findAll();
    <Optional> ChangeRequest findById(Long id);
    void deleteById(Long id);
}
