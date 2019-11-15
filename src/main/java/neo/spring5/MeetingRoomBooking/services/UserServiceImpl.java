package neo.spring5.MeetingRoomBooking.services;

import java.util.*;

import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.Token;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.repositories.TokenRepository;
import neo.spring5.MeetingRoomBooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenRepository tokenRepository;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(0);
        Role userRole = roleRepository.findByRole("USER").orElse(null);
        user.setRole(userRole);
		userRepository.save(user);

		Token token = new Token();
		token.setToken(UUID.randomUUID().toString());
		token.setUser(user);
		tokenRepository.save(token);

		String appUrl = "http://localhost:8080";
		String subject= "Email Verification";
		String body = "Verify your email id, click the link below:\n" +"<a href='"+ appUrl
				+ "/verifyEmail?token=" + token.getToken()+"'>Click here</a>";
		emailService.sendEmail(user.getEmail(), subject, body);
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

	@Override
	public void editSave(User user) {
		userRepository.save(user);
	}

	@Override
	public Page<User> getPaginatedUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

}
