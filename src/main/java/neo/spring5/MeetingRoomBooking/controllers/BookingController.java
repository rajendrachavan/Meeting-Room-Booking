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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

    //------------------------------------= ADMIN =-------------------------------------------------------------

    @RequestMapping(value = "/admin/booking-requests/{page}")
    public ModelAndView bookingRequests(@PathVariable(value = "page") int page,
                                        @RequestParam(defaultValue = "id") String sortBy){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
        Page<BookingDetails> bookingPage = bookingService.getPaginatedBookingDetails(pageable);
        int totalPages = bookingPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("activeBookingList", true);
        modelAndView.addObject("bookingDetails", bookingPage.getContent());
        modelAndView.setViewName("admin/booking-requests");
        return modelAndView;
    }

    //======================Filter with room status====================================================

    @RequestMapping("/admin/booking-requests/pending")
    public String roomAllocationPending(Model model){
        model.addAttribute("bookingDetails", bookingService.findAllByStatus("Pending"));
        return "admin/booking-requests";
    }

    @RequestMapping("/admin/booking-requests/confirmed")
    public String roomAllocationConfirmed(Model model){
        model.addAttribute("bookingDetails", bookingService.findAllByStatus("Confirmed"));
        return "admin/booking-requests";
    }

    @RequestMapping("/admin/booking-requests/rejected")
    public String roomAllocationCancelled(Model model){
        model.addAttribute("bookingDetails", bookingService.findAllByStatus("Rejected"));
        return "admin/booking-requests";
    }
    //===============================================================================================

    @PostMapping(value = "/admin/confirmRequest/{id}")
    public ModelAndView confirmRequest(@PathVariable(value = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        BookingDetails bookingDetails = bookingService.findById(id).orElse(null);
        if(bookingDetails == null){
            modelAndView.addObject("successMessage", "BookingDetails Not Found.");
            modelAndView.setViewName("redirect:/admin/booking-requests/1");
        }
        else{
            bookingDetails.setStatus("Confirmed");
            bookingService.save(bookingDetails);
        }
        modelAndView.addObject("successMessage", "BookingDetails Confirmed.");
        modelAndView.setViewName("redirect:/admin/booking-requests/1");
        return modelAndView;
    }

    @PostMapping(value = "/admin/rejectRequest/{id}")
    public ModelAndView rejectRequest(@PathVariable(value = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        BookingDetails bookingDetails = bookingService.findById(id).orElse(null);
        if(bookingDetails == null){
            modelAndView.addObject("successMessage", "BookingDetails Not Found.");
            modelAndView.setViewName("redirect:/admin/booking-requests");
        }
        else{
            bookingDetails.setStatus("Rejected");
            bookingService.save(bookingDetails);
        }
        modelAndView.addObject("successMessage", "BookingDetails Rejected.");
        modelAndView.setViewName("redirect:/admin/booking-requests/1");
        return modelAndView;
    }

    //-----------------------------------= USER =----------------------------------------------------------------

    @RequestMapping("/user/booking-status/{page}")
    public ModelAndView bookingStatus(@PathVariable(value = "page") int page,
                                      @RequestParam(defaultValue = "id") String sortBy){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        modelAndView.addObject("bookingStatus","Booking status");
        PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
        Page<BookingDetails> bookingDetailsPage = bookingService.getPaginatedBookingDetails(user, pageable);

        int totalPages = bookingDetailsPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("temp", 1);
        modelAndView.addObject("activeBookingsList", true);
        modelAndView.addObject("bookingDetails", bookingDetailsPage.getContent());
        modelAndView.setViewName("user/booking-status");
        return modelAndView;
    }

    //======================Filter with room status====================================================

    @RequestMapping("/user/booking-status/pending")
    public String pending(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<BookingDetails> bookingDetails = new ArrayList<>();
        for (BookingDetails bookingDetail: bookingService.findAllByUser(user)) {
            if(bookingDetail.getStatus().equals("Pending")){
                bookingDetails.add(bookingDetail);
            }
        }
        model.addAttribute("temp", 1);
        model.addAttribute("bookingDetails", bookingDetails);
        return "user/booking-status";
    }

    @RequestMapping("/user/booking-status/confirmed")
    public String confirmed(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<BookingDetails> bookingDetails = new ArrayList<>();
        for (BookingDetails bookingDetail: bookingService.findAllByUser(user)) {
            if(bookingDetail.getStatus().equals("Confirmed")){
                bookingDetails.add(bookingDetail);
            }
        }
        model.addAttribute("temp", 1);
        model.addAttribute("bookingDetails", bookingDetails);
        return "user/booking-status";
    }

    @RequestMapping("/user/booking-status/rejected")
    public String rejected(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<BookingDetails> bookingDetails = new ArrayList<>();
        for (BookingDetails bookingDetail: bookingService.findAllByUser(user)) {
            if(bookingDetail.getStatus().equals("Rejected")){
                bookingDetails.add(bookingDetail);
            }
        }
        model.addAttribute("temp", 1);
        model.addAttribute("bookingDetails", bookingDetails);
        return "user/booking-status";
    }
    //==================================================================================================

    //============================Booking History===============================================

    @RequestMapping(value = "/booking-history", method = RequestMethod.GET)
    public ModelAndView bookingHistory(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<BookingDetails> bookingDetails = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        for (BookingDetails bookingDetail: user.getBookingDetails()) {
            if(bookingDetail.getStartTime().isBefore(today)){
                bookingDetails.add(bookingDetail);
            }
        }
        modelAndView.addObject("temp", 0);
        modelAndView.addObject("bookingDetails", bookingDetails);
        modelAndView.setViewName("user/booking-status");
        return modelAndView;
    }
    //============================================================================================

    @PostMapping("/bookRoom/{id}/{startTime}/{endTime}")
    public ModelAndView bookRoom(@PathVariable(value="id") Long id,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PathVariable("startTime") LocalDateTime startTime,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PathVariable("endTime") LocalDateTime endTime) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        //modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        BookingDetails bookingDetails = new BookingDetails();
        MeetingRoom meetingRoom = meetingRoomService.findById(id).orElse(null);
        bookingDetails.setStatus("Pending");
        bookingDetails.setMeetingRoom(meetingRoom);
        bookingDetails.setUser(user);
        bookingDetails.setStartTime(startTime);
        bookingDetails.setEndTime(endTime);
        bookingService.save(bookingDetails);
        modelAndView.addObject("successMessage", "Room Booked successfully");
        modelAndView.setViewName("redirect:/meeting-room-details/1");
        return modelAndView;
    }

    @RequestMapping(value = "/deleteRequest/{id}")
    public ModelAndView deleteRequest(@PathVariable(value="id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        BookingDetails bookingDetails = bookingService.findById(id).orElse(null);
        if(bookingDetails == null){
            modelAndView.addObject("successMessage", "BookingDetails Not Found.");
            modelAndView.setViewName("redirect:/user/booking-status/1");
        }
        else{
            bookingService.deleteById(id);
            modelAndView.addObject("successMessage", "BookingDetails Deleted Successfully.");
            modelAndView.setViewName("redirect:/user/booking-status/1");
        }
        return modelAndView;
    }
}
