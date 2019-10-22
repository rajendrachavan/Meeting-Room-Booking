package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = {"/","/homepage"}, method = RequestMethod.GET)
    public ModelAndView homepage(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("homepage");
        return modelAndView;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView loginpage(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public ModelAndView registrationpage(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user){
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findByEmail(user.getEmail());
        if(userExists != null){
            modelAndView.setViewName("registration");
        }else{
            userService.save(user);
            modelAndView.addObject("successMessage", "User has been registered Successfully!");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/dashboard"}, method = RequestMethod.GET)
    public ModelAndView adminDashboard(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }
}
