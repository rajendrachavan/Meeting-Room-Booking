package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    void deleteById(Long id);
    User findByEmail(String email);
}
