package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.BookingDetails;
import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import neo.spring5.MeetingRoomBooking.repositories.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Override
    public void save(MeetingRoom meetingRoom) {
        meetingRoomRepository.save(meetingRoom);
    }

    @Override
    public List<MeetingRoom> findAll() {
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        return meetingRooms;
    }

    @Override
    public Optional<MeetingRoom> findById(Long id) {
        Optional<MeetingRoom> meetingRoom = meetingRoomRepository.findById(id);
        return meetingRoom;
    }

    @Override
    public void deleteById(Long id) {
        meetingRoomRepository.deleteById(id);
    }

    @Override
    public MeetingRoom findMeetingRoomByName(String name) {
        return meetingRoomRepository.findMeetingRoomByName(name);
    }

    @Override
    public Page<MeetingRoom> getPaginatedMeetingRooms(Pageable pageable) {
        return meetingRoomRepository.findAll(pageable);
    }

    @Override
    public List<MeetingRoom> filterByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime) {

        List<MeetingRoom> meetingRooms = new ArrayList<>();
        for (MeetingRoom meetingRoom : meetingRoomRepository.findAll()) {
            boolean flag = true;
            if (meetingRoom.getBookingDetails().isEmpty()) meetingRooms.add(meetingRoom);
            else {
                for (BookingDetails bookingDetail : meetingRoom.getBookingDetails()) {
                    if (bookingDetail.getDate().isEqual(date) && bookingDetail.getStatus().equals("Confirmed")) {
                        if (startTime.equals(bookingDetail.getStartTime()) && endTime.equals(bookingDetail.getEndTime())) {
                            flag = false;
                            break;
                        } else if (startTime.isBefore(bookingDetail.getStartTime()) && endTime.isBefore(bookingDetail.getStartTime())) {
                            flag = true;
                            break;
                        } else if (startTime.isAfter(bookingDetail.getEndTime()) && endTime.isAfter(startTime)) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                            break;
                        }
                    } else
                        flag = true;
                }
                if (flag == true)
                    meetingRooms.add(meetingRoom);
            }
        }
        return meetingRooms;
    }
}