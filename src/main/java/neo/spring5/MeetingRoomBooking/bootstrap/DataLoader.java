package neo.spring5.MeetingRoomBooking.bootstrap;

import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.boot.CommandLineRunner;

//@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;

    public DataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setFirstName("test");
        //user.se

        System.out.println("------------------Booking Details Added------------------");

    }
}
