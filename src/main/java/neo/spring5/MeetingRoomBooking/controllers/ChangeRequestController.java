package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.ChangeRequest;
import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.ChangeRequestRepository;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.services.EmailService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChangeRequestController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChangeRequestRepository changeRequestRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/change-requests", method = RequestMethod.GET)
    public ModelAndView changeRequests(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Role role = null;
        switch (user.getRole().getRole()){
            case "ADMIN":
                role = roleRepository.findByRole("PM").orElse(null);
                break;
            case "PM":
                role = roleRepository.findByRole("TL").orElse(null);
                break;
            case "TL":
                role = roleRepository.findByRole("USER").orElse(null);
                break;
        }
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("requests", changeRequestRepository
                .findAllByUserRole(role));
        modelAndView.setViewName("/change-requests");
        return modelAndView;
    }

    @RequestMapping(value="/confirmChangeRequest/{id}", method = RequestMethod.POST)
    public ModelAndView confirmChangeRequest(ModelAndView modelAndView,
                                             @PathVariable("id") Long id){
        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElse(null);
        User user = userService.findById(changeRequest.getUser().getId()).orElse(null);

        if(changeRequest.getType().equals("email")){
            user.setEmail(changeRequest.getNewValue());
            userService.editSave(user);
            String appUrl = "http://localhost:8080";
            String subject= "Email Change Request";
            String body = "Your Request for Change in Email Address is Confirmed!\n" +
                    "Your can now login with your new Email ID: "+changeRequest.getNewValue();
            emailService.sendEmail(changeRequest.getOldValue(), subject, body);
        } else {
            user.setDepartment(changeRequest.getNewValue());
            userService.editSave(user);
        }
        changeRequest.setStatus("Confirmed");
        changeRequestRepository.save(changeRequest);
        modelAndView.setViewName("redirect:/change-requests");
        return modelAndView;
    }

    @RequestMapping(value="/rejectChangeRequest/{id}", method = RequestMethod.POST)
    public ModelAndView rejectChangeRequest(ModelAndView modelAndView,
                                            @PathVariable("id") Long id){
        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElse(null);
        User user = userService.findById(changeRequest.getUser().getId()).orElse(null);
        if(changeRequest.getType().equals("email")){
            user.setEmail(changeRequest.getOldValue());
            userService.editSave(user);

            String appUrl = "http://localhost:8080";
            String subject= "Email Change Request";
            String body = "Your Request for Change in Email Address is Rejected!\n";
            emailService.sendEmail(changeRequest.getOldValue(), subject, body);
        } else {
            user.setDepartment(changeRequest.getOldValue());
            userService.editSave(user);
        }
        changeRequest.setStatus("Rejected");
        changeRequestRepository.save(changeRequest);
        modelAndView.setViewName("redirect:/change-requests");
        return modelAndView;
    }
}
