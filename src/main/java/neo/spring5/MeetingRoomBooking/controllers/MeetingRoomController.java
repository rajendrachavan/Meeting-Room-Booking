package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.services.MeetingRoomService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private UserService userService;

    @RequestMapping({"/meeting-room-details"})
    public ModelAndView meetingRoom(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("meetingRoomDetails", "Meeting-Room Details");
        modelAndView.addObject("meetingRooms", meetingRoomService.findAll());
        modelAndView.setViewName("meeting-room-details");
        return modelAndView;
    }

    @RequestMapping(value="/admin/add-room", method = RequestMethod.GET)
    public ModelAndView addRoom(){
        ModelAndView modelAndView = new ModelAndView();
        MeetingRoom meetingRoom = new MeetingRoom();
        modelAndView.addObject("meetingRoom", meetingRoom);
        modelAndView.setViewName("admin/add-room");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/add-room", method = RequestMethod.POST)
    public ModelAndView createNewRoom(@Valid MeetingRoom meetingRoom) {
        ModelAndView modelAndView = new ModelAndView();
        MeetingRoom meetingRoom1 = meetingRoomService.findMeetingRoomByName(meetingRoom.getName());
        if (meetingRoom1 != null) {
            modelAndView.setViewName("admin/add-room");
        } else {
            meetingRoomService.save(meetingRoom);
            modelAndView.addObject("successMessage", "Room has been added successfully");
            modelAndView.addObject("meetingRoom", new MeetingRoom());
            modelAndView.setViewName("admin/add-room");
        }
        return modelAndView;
    }
}
