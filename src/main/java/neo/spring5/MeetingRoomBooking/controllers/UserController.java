package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user-profile")
    public ModelAndView userProfile(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", user);
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
}
