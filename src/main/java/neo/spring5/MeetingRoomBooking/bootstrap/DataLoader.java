package neo.spring5.MeetingRoomBooking.bootstrap;

import neo.spring5.MeetingRoomBooking.models.Facilities;
import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import neo.spring5.MeetingRoomBooking.repositories.FacilitiesRepository;
import neo.spring5.MeetingRoomBooking.services.MeetingRoomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

//@Component
public class DataLoader implements CommandLineRunner {

    private final MeetingRoomService meetingRoomService;
    private final FacilitiesRepository facilitiesRepository;

    public DataLoader(MeetingRoomService meetingRoomService, FacilitiesRepository facilitiesRepository) {
        this.meetingRoomService = meetingRoomService;
        this.facilitiesRepository = facilitiesRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Set<Facilities> facilitiesList = new HashSet<>();
        Facilities facilities = new Facilities();
        facilities.setFacility("AC");
        facilitiesList.add(facilities);

        Facilities facilities1 = new Facilities();
        facilities1.setFacility("Projector");
        facilitiesList.add(facilities1);

        Facilities facilities2 = new Facilities();
        facilities2.setFacility("WhiteBoard");
        facilitiesList.add(facilities2);

        Facilities facilities3 = new Facilities();
        facilities3.setFacility("Markers");
        facilitiesList.add(facilities3);

        Facilities facilities4 = new Facilities();
        facilities4.setFacility("Large Monitor");
        facilitiesList.add(facilities4);

        Facilities facilities5 = new Facilities();
        facilities5.setFacility("WiFi");
        facilitiesList.add(facilities5);

        Facilities facilities6 = new Facilities();
        facilities6.setFacility("Speakers");
        facilitiesList.add(facilities6);

        Facilities facilities7 = new Facilities();
        facilities7.setFacility("Phone");
        facilitiesList.add(facilities7);

        facilitiesRepository.saveAll(facilitiesList);
        System.out.println("------------------Facilities Added------------------");

        /*MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setName("Green Room");
        meetingRoom.setLocation("Parel");
        meetingRoom.setFacilities(facilitiesList);
        meetingRoom.setStatus("Available");
        meetingRoomService.save(meetingRoom);*/
        System.out.println("------------------Meeting Rooms Added---------------");
    }
}
