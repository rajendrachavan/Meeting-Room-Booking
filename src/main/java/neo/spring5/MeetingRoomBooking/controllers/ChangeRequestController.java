package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.*;
import neo.spring5.MeetingRoomBooking.repositories.ChangeRequestRepository;
import neo.spring5.MeetingRoomBooking.repositories.DepartmentRepository;
import neo.spring5.MeetingRoomBooking.repositories.NotificationRepository;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.services.EmailService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChangeRequestController {

    private final UserService userService;
    private final ChangeRequestRepository changeRequestRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final DepartmentRepository departmentRepository;
    private final NotificationRepository notificationRepository;

    public ChangeRequestController(UserService userService, ChangeRequestRepository changeRequestRepository,
                                   RoleRepository roleRepository, EmailService emailService,
                                   DepartmentRepository departmentRepository,
                                   NotificationRepository notificationRepository) {
        this.userService = userService;
        this.changeRequestRepository = changeRequestRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.departmentRepository = departmentRepository;
        this.notificationRepository = notificationRepository;
    }

    //===========================Display Change Requests=========================================
    @RequestMapping(value="/change-requests", method = RequestMethod.GET)
    public ModelAndView changeRequests(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<ChangeRequest> changeRequests = new ArrayList<>();
        Role role = null;
        switch (user.getRole().getRole()){
            case "ADMIN":
                role = roleRepository.findByRole("PM").orElse(null);
                for (ChangeRequest changeRequest: changeRequestRepository.findAll()) {
                    if(changeRequest.getUser().getParent() == null) changeRequests.add(changeRequest);
                }
                changeRequests.addAll(changeRequestRepository.findAllByUserRole(role));
                break;
            case "PM":
                role = roleRepository.findByRole("TL").orElse(null);
                changeRequests.addAll(changeRequestRepository.findAllByUserRole(role));
                break;
            case "TL":
                role = roleRepository.findByRole("USER").orElse(null);
                changeRequests.addAll(changeRequestRepository.findAllByUserRole(role));
                break;
        }

        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("requests", changeRequests);
        modelAndView.addObject("noRecords", "No Records Found!");
        modelAndView.setViewName("/change-requests");
        return modelAndView;
    }

    //===========================Confirm Changes=========================================
    @RequestMapping(value="/confirmChangeRequest/{id}", method = RequestMethod.POST)
    public ModelAndView confirmChangeRequest(ModelAndView modelAndView,
                                             @PathVariable("id") Long id){
        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElse(null);
        User user = userService.findById(changeRequest.getUser().getId()).orElse(null);

        if(changeRequest.getType().equals(Type.Email_ChangeRequest)){
            user.setEmail(changeRequest.getNewValue());
            userService.editSave(user);

            String description = "Your Request for Change in Email Address is Confirmed!";
            Notification notification = new Notification(user, description, Type.Email_ChangeRequest, Status.Unread);
            notificationRepository.save(notification);

            String subject= "Email Change Request";
            String body = "Your Request for Change in Email Address is Confirmed!\n" +
                    "Your can now login with your new Email ID: "+changeRequest.getNewValue();
            emailService.sendEmail(changeRequest.getOldValue(), subject, body);
        } else {
            Department department = departmentRepository.findByName(changeRequest.getNewValue());
            user.setDepartment(department);
            userService.editSave(user);

            String description = "Your Request for Change in Department is Confirmed!";
            Notification notification = new Notification(user, description, Type.Department_ChangeRequest, Status.Unread);
            notificationRepository.save(notification);
        }
        changeRequest.setStatus(Status.Confirmed);
        changeRequestRepository.save(changeRequest);
        modelAndView.setViewName("redirect:/change-requests");
        return modelAndView;
    }

    //===========================Reject Changes=========================================
    @RequestMapping(value="/rejectChangeRequest/{id}", method = RequestMethod.POST)
    public ModelAndView rejectChangeRequest(ModelAndView modelAndView,
                                            @PathVariable("id") Long id){
        ChangeRequest changeRequest = changeRequestRepository.findById(id).orElse(null);
        User user = userService.findById(changeRequest.getUser().getId()).orElse(null);
        if(changeRequest.getType().equals(Type.Email_ChangeRequest)){
            user.setEmail(changeRequest.getOldValue());
            userService.editSave(user);

            String description = "Your Request for Change in Email Address is Rejected!";
            Notification notification = new Notification(user, description, Type.Email_ChangeRequest, Status.Unread);
            notificationRepository.save(notification);

            String subject= "Email Change Request";
            String body = "Your Request for Change in Email Address is Rejected!\n";
            emailService.sendEmail(changeRequest.getOldValue(), subject, body);
        } else {
            Department department = departmentRepository.findByName(changeRequest.getOldValue());
            user.setDepartment(department);
            userService.editSave(user);

            String description = "Your Request for Change in Department is Rejected!";
            Notification notification = new Notification(user, description, Type.Department_ChangeRequest, Status.Unread);
            notificationRepository.save(notification);
        }
        changeRequest.setStatus(Status.Rejected);
        changeRequestRepository.save(changeRequest);
        modelAndView.setViewName("redirect:/change-requests");
        return modelAndView;
    }
}
