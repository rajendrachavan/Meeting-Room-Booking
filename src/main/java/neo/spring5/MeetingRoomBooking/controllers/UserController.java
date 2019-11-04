package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/user-profile/{id}")
    public ModelAndView userProfile(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findById(id).orElse(null);
        if(user == null){
            modelAndView.addObject("successMessage", "User Not Found.");
            modelAndView.setViewName("/homepage");
            return modelAndView;
        }
        modelAndView.addObject("user", user);
        modelAndView.addObject("userProfile", "User Profile");
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.setViewName("user/user-profile");
        return modelAndView;
    }
}
