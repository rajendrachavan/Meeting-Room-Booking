package neo.spring5.MeetingRoomBooking.repositories;

import neo.spring5.MeetingRoomBooking.models.ChangeRequest;
import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long> {
    <Optional> ChangeRequest findByUser(User user);
    List<ChangeRequest> findAllByUserRole(Role role);
}
