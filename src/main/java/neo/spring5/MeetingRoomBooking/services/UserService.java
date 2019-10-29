package neo.spring5.MeetingRoomBooking.services;


import neo.spring5.MeetingRoomBooking.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveUser(User user);
	public List<User> findAll();
	public Optional<User> findById(Long id);
	public void deleteById(Long id);

	public void editSave(User user);
}
