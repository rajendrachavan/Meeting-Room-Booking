package neo.spring5.MeetingRoomBooking.services;


import neo.spring5.MeetingRoomBooking.models.User;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveUser(User user);
}
