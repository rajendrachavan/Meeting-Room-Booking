package neo.spring5.MeetingRoomBooking.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRole(userRole);
		userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		return users;
	}

	@Override
	public User findById(Long id) {
		User user = userRepository.findById(id).get();
		return user;
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
}
