package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public void save(User user);
    public List<User> findAll();
    public Optional<User> findById(Long id);
    public void deleteById(Long id);
    public User findUserByEmail(String email);
}
