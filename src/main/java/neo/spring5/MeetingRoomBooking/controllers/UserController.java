package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.*;
import neo.spring5.MeetingRoomBooking.repositories.ChangeRequestRepository;
import neo.spring5.MeetingRoomBooking.repositories.DepartmentRepository;
import neo.spring5.MeetingRoomBooking.repositories.FeedbackRepository;
import neo.spring5.MeetingRoomBooking.repositories.NotificationRepository;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @RequestMapping(value = "/user-profile")
    public ModelAndView userProfile(ModelAndView modelAndView,
                                    @ModelAttribute("successMessage") String successMessage,
                                    @ModelAttribute("errorMessage") String errorMessage){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", user);
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("userProfile", "User Profile");
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("successMessage", successMessage);
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("user/user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/edit-user-profile")
    public ModelAndView editProfilepage(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/edit-user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/edit-user-profile/{id}", method = RequestMethod.PUT)
    public ModelAndView updateUserProfile(ModelAndView modelAndView,
                                          @PathVariable("id") Long id,
                                          @Valid @ModelAttribute("user") User user,
                                          RedirectAttributes redirectAttributes){
        User userDB = userService.findById(id).orElse(null);
        userDB.setFirstName(user.getFirstName());
        userDB.setLastName(user.getLastName());
        userDB.setGender(user.getGender());
        userDB.setMobileNo(user.getMobileNo());
        userService.editSave(userDB);
        redirectAttributes.addFlashAttribute("successMessage", "User Updated Successfully.");
        modelAndView.setViewName("redirect:/user/user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword")
    public ModelAndView changePassword(ModelAndView modelAndView){
        modelAndView.addObject("temp", 1);
        modelAndView.setViewName("/forgot-password");
        return modelAndView;
    }

    @RequestMapping(value = "/changeEmail", method = RequestMethod.GET)
    public ModelAndView changeEmail(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("previousEmail",user.getEmail());
        modelAndView.addObject("temp", 1);
        modelAndView.setViewName("user/change-request");
        return modelAndView;
    }

    @RequestMapping(value = "/changeEmail", method = RequestMethod.POST)
    public ModelAndView processChangeEmail(ModelAndView modelAndView,
                                           @RequestParam("email") String userEmail,
                                           RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setType(Type.Email_ChangeRequest);
        changeRequest.setOldValue(user.getEmail());
        changeRequest.setNewValue(userEmail);
        changeRequest.setUser(user);
        changeRequest.setStatus(Status.Pending);
        changeRequestRepository.save(changeRequest);

        String description = user.getFirstName()+" "+user.getLastName()+" has requested for change in email.";
        Notification notification = null;
        if(user.getParent() == null)
            notification = new Notification(userService.getAdmin(), description, Type.Email_ChangeRequests, Status.Unread);
        else
            notification = new Notification(user.getParent(), description, Type.Email_ChangeRequests, Status.Unread);
        notificationRepository.save(notification);

        redirectAttributes.addFlashAttribute("successMessage", "Change Email Request sent");
        modelAndView.setViewName("redirect:/user/user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/changeDepartment", method = RequestMethod.GET)
    public ModelAndView changeDepartment(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("previousDepartment",user.getDepartment().getName());
        modelAndView.addObject("temp", 2);
        modelAndView.setViewName("user/change-request");
        return modelAndView;
    }

    @RequestMapping(value = "/changeDepartment", method = RequestMethod.POST)
    public ModelAndView processChangeDepartment(ModelAndView modelAndView,
                                                @RequestParam("department") Long userDept,
                                                RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setType(Type.Department_ChangeRequest);
        changeRequest.setOldValue(user.getDepartment().getName());
        changeRequest.setNewValue(departmentRepository.findById(userDept).orElse(null).getName());
        changeRequest.setUser(user);
        changeRequest.setStatus(Status.Pending);
        changeRequestRepository.save(changeRequest);

        String description = user.getFirstName()+" "+user.getLastName()+" has requested for change in department.";
        Notification notification = null;
        if(user.getParent() == null)
            notification = new Notification(userService.getAdmin(), description, Type.Department_ChangeRequests, Status.Unread);
        else
            notification = new Notification(user.getParent(), description, Type.Department_ChangeRequests, Status.Unread);
        notificationRepository.save(notification);

        redirectAttributes.addFlashAttribute("successMessage", "Change Department Request Sent");
        modelAndView.setViewName("redirect:/user/user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/profile-change-requests")
    public ModelAndView profileChangeRequestStatus(ModelAndView modelAndView,
                                                   @ModelAttribute("successMessage") String successMessage,
                                                   @ModelAttribute("errorMessage") String errorMessage){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("requests", user.getChangeRequests());
        modelAndView.addObject("successMessage", successMessage);
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("user/profile-change-requests");
        return modelAndView;
    }

    @RequestMapping(value = "/cancelChangeRequest/{id}", method = RequestMethod.POST)
    public ModelAndView cancelChangeRequest(ModelAndView modelAndView,
                                            @PathVariable("id") Long id,
                                            RedirectAttributes redirectAttributes){
        changeRequestRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Request Cancelled.");
        modelAndView.setViewName("user/profile-change-requests");
        return modelAndView;
    }

    @RequestMapping("/feedback")
    public ModelAndView feedback(ModelAndView modelAndView){
        modelAndView.addObject("feedback", new Feedback());
        modelAndView.setViewName("user/feedback-form");
        return modelAndView;
    }

    @PostMapping("/feedback")
    public ModelAndView processFeedback(ModelAndView modelAndView,
                                        Feedback feedback,
                                        RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        feedback.setUser(user);
        feedbackRepository.save(feedback);
        redirectAttributes.addFlashAttribute("successMessage", "Feedback Submitted.");
        modelAndView.setViewName("redirect:/user/feedback");
        return modelAndView;
    }
}
