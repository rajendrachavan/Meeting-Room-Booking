package neo.spring5.MeetingRoomBooking.bootstrap;

import neo.spring5.MeetingRoomBooking.models.BookingDetails;
import neo.spring5.MeetingRoomBooking.models.Facilities;
import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.FacilitiesRepository;
import neo.spring5.MeetingRoomBooking.services.BookingService;
import neo.spring5.MeetingRoomBooking.services.MeetingRoomService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//@Component
public class DataLoader implements CommandLineRunner {

    private final MeetingRoomService meetingRoomService;
    private final UserService userService;
    private final BookingService bookingService;
    private final FacilitiesRepository facilitiesRepository;

    public DataLoader(MeetingRoomService meetingRoomService, UserService userService, BookingService bookingService, FacilitiesRepository facilitiesRepository) {
        this.meetingRoomService = meetingRoomService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.facilitiesRepository = facilitiesRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        LocalDate localDate= LocalDate.now();
        Set<Facilities> facilitiesList = new HashSet<>();
        Facilities facilities = facilitiesRepository.findFacilityById(1L);
        facilitiesList.add(facilities);


        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setLocation("Dadar");
        meetingRoom.setName("Black Room");
        meetingRoom.setStatus("Available");
        meetingRoom.setFacilities(facilitiesList);

        User user = new User();
        user.setFirstName("laxman");

        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setDate(localDate);
        bookingDetails.setStatus("Pending");
        bookingDetails.setMeetingRoom(meetingRoom);
        bookingDetails.setUser(user);
        bookingService.save(bookingDetails);

        System.out.println("------------------Booking Details Added------------------");

    }
}
