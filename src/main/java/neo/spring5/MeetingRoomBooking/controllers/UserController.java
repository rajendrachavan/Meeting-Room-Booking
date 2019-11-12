package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.ChangeRequest;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.ChangeRequestRepository;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @RequestMapping(value = "/user-profile")
    public ModelAndView userProfile(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", user);
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("userProfile", "User Profile");
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
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
                                          @Valid @ModelAttribute("user") User user){
        User userDB = userService.findById(id).orElse(null);
        user.setEmail(userDB.getEmail());
        user.setPassword(userDB.getPassword());
        user.setDepartment(userDB.getDepartment());
        user.setActive(userDB.getActive());
        user.setRole(userDB.getRole());
        userService.editSave(user);
        modelAndView.addObject("successMessage", "User Updated Successfully.");
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
                                           @RequestParam("email") String userEmail){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setType("email");
        changeRequest.setOldValue(user.getEmail());
        changeRequest.setNewValue(userEmail);
        changeRequest.setUser(user);
        changeRequest.setStatus("Pending");
        changeRequestRepository.save(changeRequest);
        modelAndView.addObject("successMessage", "Change Email Request Successful");
        modelAndView.setViewName("redirect:/user/user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/changeDepartment", method = RequestMethod.GET)
    public ModelAndView changeDepartment(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("previousDepartment",user.getDepartment());
        modelAndView.addObject("temp", 2);
        modelAndView.setViewName("user/change-request");
        return modelAndView;
    }

    @RequestMapping(value = "/changeDepartment", method = RequestMethod.POST)
    public ModelAndView processChangeDepartment(ModelAndView modelAndView,
                                           @RequestParam("department") String userDept){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setType("department");
        changeRequest.setOldValue(user.getDepartment());
        changeRequest.setNewValue(userDept);
        changeRequest.setUser(user);
        changeRequest.setStatus("Pending");
        changeRequestRepository.save(changeRequest);
        modelAndView.addObject("successMessage", "Change Email Request Successful");
        modelAndView.setViewName("redirect:/user/user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/profile-change-requests")
    public ModelAndView profileChangeRequestStatus(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("role", user.getRole().getRole());
        modelAndView.addObject("requests", user.getChangeRequests());
        modelAndView.setViewName("user/profile-change-requests");
        return modelAndView;
    }

    @RequestMapping(value = "/cancelChangeRequest/{id}", method = RequestMethod.POST)
    public ModelAndView cancelChangeRequest(ModelAndView modelAndView,
                                            @PathVariable("id") Long id){
        changeRequestRepository.deleteById(id);
        modelAndView.addObject("successMessage", "Request Cancelled.");
        modelAndView.setViewName("user/profile-change-requests");
        return modelAndView;
    }
}
