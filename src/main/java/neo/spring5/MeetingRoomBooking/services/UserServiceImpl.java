package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.repositories.UserRepository;
import org.apache.catalina.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
