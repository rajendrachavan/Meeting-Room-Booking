package neo.spring5.MeetingRoomBooking.services;


import neo.spring5.MeetingRoomBooking.models.User;

import java.util.List;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveUser(User user);
	public List<User> findAll();
	public User findById(Long id);
	public void deleteById(Long id);
}
