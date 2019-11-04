package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.Facilities;
import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.FacilitiesRepository;
import neo.spring5.MeetingRoomBooking.services.MeetingRoomService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private UserService userService;

    @Autowired
    private FacilitiesRepository facilitiesRepository;

    //----------------------------= COMMON =------------------------------------

    @RequestMapping("/meeting-room-details/{page}")
    public ModelAndView meetingRoom(@PathVariable(value = "page") int page,
                                    @RequestParam(defaultValue = "id") String sortBy){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("meetingRoomDetails", "Meeting-Room Details");

        PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
        Page<MeetingRoom> meetingRoomPage = meetingRoomService.getPaginatedMeetingRooms(pageable);
        int totalPages = meetingRoomPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("activeRoomsList", true);
        modelAndView.addObject("meetingRooms", meetingRoomPage.getContent());
        modelAndView.setViewName("meeting-room-details");
        return modelAndView;
    }

    //--------------------------------= ADMIN =---------------------------------------------

    @RequestMapping(value="/admin/add-room", method = RequestMethod.GET)
    public ModelAndView addRoom(){
        ModelAndView modelAndView = new ModelAndView();
        MeetingRoom meetingRoom = new MeetingRoom();
        modelAndView.addObject("meetingRoom", meetingRoom);
        modelAndView.addObject("facilities", facilitiesRepository.findAll());
        modelAndView.setViewName("admin/add-room");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/add-room", method = RequestMethod.POST)
    public ModelAndView createNewRoom(@Valid MeetingRoom meetingRoom,
                                      @RequestParam("facilities") Long[] id) {
        ModelAndView modelAndView = new ModelAndView();
        MeetingRoom meetingRoom1 = meetingRoomService.findMeetingRoomByName(meetingRoom.getName());
        if (meetingRoom1 != null) {
            modelAndView.addObject("successMessage","Room already exists!!!");
            modelAndView.setViewName("admin/add-room");
        } else {
            if(id != null){
                Set<Facilities> facilitiesSet = new HashSet<>();
                Facilities facility;
                for (Long i : id){
                    facility = facilitiesRepository.findFacilityById(i);
                    if(facility != null){
                        facilitiesSet.add(facility);
                    }
                }
                meetingRoom.setFacilities(facilitiesSet);
            }
            meetingRoomService.save(meetingRoom);
            modelAndView.addObject("successMessage", "Room has been added successfully");
            modelAndView.addObject("meetingRoom", new MeetingRoom());
            modelAndView.setViewName("redirect:/meeting-room-details");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/admin/updateMeetingRoom/{id}", method = RequestMethod.GET)
    public ModelAndView updatepage(@PathVariable(value="id") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        MeetingRoom meetingRoom = meetingRoomService.findById(id).orElse(null);
        if(meetingRoom == null){
            System.out.println("MeetingRoom Not Found");
            modelAndView.addObject("successMessage","MeetingRoom Not Found");
            modelAndView.setViewName("admin/updateMeetingRoom");
            return modelAndView;
        }
        modelAndView.addObject("meetingRoom", meetingRoom);
        modelAndView.addObject("facilities", facilitiesRepository.findAll());
        modelAndView.addObject("id", id);
        modelAndView.setViewName("admin/updateMeetingRoom");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/updateMeetingRoom/{id}", method = RequestMethod.PUT)
    public ModelAndView editUser(@PathVariable(value="id") Long id,
                                 @Valid @ModelAttribute("meetingRoom") MeetingRoom meetingRoomData){
        ModelAndView modelAndView = new ModelAndView();
        meetingRoomData.setName(meetingRoomData.getName());
        meetingRoomData.setLocation(meetingRoomData.getLocation());
        meetingRoomData.setFacilities(meetingRoomData.getFacilities());
        meetingRoomData.setStatus(meetingRoomData.getStatus());
        meetingRoomService.save(meetingRoomData);
        modelAndView.addObject("successMessage", "MeetingRoom has been Updated successfully");
        modelAndView.addObject("meetingRoom", meetingRoomData);
        modelAndView.setViewName("redirect:/meeting-room-details");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/deleteMeetingRoom/{id}")
    public ModelAndView delete(@PathVariable(value = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        meetingRoomService.deleteById(id);
        modelAndView.addObject("successMessage", "MeetingRoom Deleted Successfully.");
        modelAndView.setViewName("redirect:/meeting-room-details");
        return modelAndView;
    }
}
