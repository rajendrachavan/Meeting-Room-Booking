package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.BookingDetails;
import neo.spring5.MeetingRoomBooking.models.Facilities;
import neo.spring5.MeetingRoomBooking.models.MeetingRoom;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.services.BookingService;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @RequestMapping(value = "/admin/booking-requests/{page}")
    public ModelAndView bookingRequests(@PathVariable(value = "page") int page,
                                        @RequestParam(defaultValue = "id") String sortBy){
        ModelAndView modelAndView = new ModelAndView();

        PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
        Page<BookingDetails> bookingPage = bookingService.getPaginatedBookingDetails(pageable);
        int totalPages = bookingPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("activeBookingList", true);
        modelAndView.addObject("bookingDetails", bookingPage.getContent());
        modelAndView.setViewName("admin/booking-requests");
        return modelAndView;
    }

    @RequestMapping("/user/booking-status")
    public ModelAndView bookingStatus(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("bookingDetails", bookingService.findAll());
        modelAndView.setViewName("user/booking-status");
        return modelAndView;
    }

    @RequestMapping(value={"/bookRoom/{id}"}, method = RequestMethod.GET)
    public ModelAndView bookRoom(@PathVariable(value="id") Long id) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        modelAndView.addObject("bookingDetails", new BookingDetails());
        modelAndView.addObject("userId", user.getId());
        modelAndView.addObject("id", id);
        modelAndView.setViewName("user/bookRoom");
        return modelAndView;
    }

    @PostMapping(value = "/bookRoom/{id}/{userId}")
    public ModelAndView NewBookingRequest(@PathVariable(value="id") Long id,
                                 @PathVariable(value="userId") Long userId,
                                 @Valid BookingDetails bookingDetails){
        ModelAndView modelAndView = new ModelAndView();
        MeetingRoom meetingRoom = meetingRoomService.findById(id).orElse(null);
        bookingDetails.setStatus("Pending");
        bookingDetails.setMeetingRoom(meetingRoom);
        bookingDetails.setUser(userService.findById(userId).orElse(null));
        bookingService.save(bookingDetails);

        modelAndView.addObject("successMessage", "Room Booked successfully");
        modelAndView.addObject("bookingDetails", bookingDetails);
        modelAndView.setViewName("redirect:/user/booking-status");
        return modelAndView;
    }

    @RequestMapping(value = "/deleteRequest/{id}")
    public ModelAndView deleteRequest(@PathVariable(value="id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        BookingDetails bookingDetails = bookingService.findById(id).orElse(null);
        if(bookingDetails == null){
            modelAndView.addObject("successMessage", "BookingDetails Not Found.");
            modelAndView.setViewName("redirect:/user/booking-status");
        }
        else{
            bookingService.deleteById(id);
            modelAndView.addObject("successMessage", "BookingDetails Deleted Successfully.");
            modelAndView.setViewName("redirect:/user/booking-status");
        }
        return modelAndView;
    }

    @PostMapping(value = "/admin/confirmRequest/{id}")
    public ModelAndView confirmRequest(@PathVariable(value = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        BookingDetails bookingDetails = bookingService.findById(id).orElse(null);
        if(bookingDetails == null){
            modelAndView.addObject("successMessage", "BookingDetails Not Found.");
            modelAndView.setViewName("redirect:/admin/booking-requests");
        }
        else{
            bookingDetails.setStatus("Confirmed");
            bookingDetails.setDate(bookingDetails.getDate());
            bookingDetails.setMeetingRoom(bookingDetails.getMeetingRoom());
            bookingDetails.setUser(bookingDetails.getUser());
            bookingService.save(bookingDetails);
        }
        modelAndView.addObject("successMessage", "BookingDetails Confirmed.");
        modelAndView.setViewName("redirect:/admin/booking-requests");
        return modelAndView;
    }

    @PostMapping(value = "/admin/cancelRequest/{id}")
    public ModelAndView cancelRequest(@PathVariable(value = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        BookingDetails bookingDetails = bookingService.findById(id).orElse(null);
        if(bookingDetails == null){
            modelAndView.addObject("successMessage", "BookingDetails Not Found.");
            modelAndView.setViewName("redirect:/admin/booking-requests");
        }
        else{
            bookingDetails.setStatus("Confirmed");
            bookingDetails.setDate(bookingDetails.getDate());
            bookingDetails.setMeetingRoom(bookingDetails.getMeetingRoom());
            bookingDetails.setUser(bookingDetails.getUser());
            bookingService.save(bookingDetails);
        }
        modelAndView.addObject("successMessage", "BookingDetails Cancelled.");
        modelAndView.setViewName("redirect:/admin/booking-requests");
        return modelAndView;
    }
}
