package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.addObject("users", userService.findAll());
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public ModelAndView updatepage(@PathVariable(value="id") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        User user1 = userService.findById(id).orElse(null);
        if(user1 == null){
            System.out.println("User Not Found");
            modelAndView.addObject("successMessage","User Not Found");
            modelAndView.setViewName("admin/home");
            return modelAndView;
        }
        modelAndView.addObject("user", user1);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("admin/update");
        return modelAndView;
    }

    @RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT)
    public ModelAndView editUser(@PathVariable(value="id") Long id,
                                 @RequestParam(name = "role") Long role,
                                 @Valid @ModelAttribute("user") User user){
        ModelAndView modelAndView = new ModelAndView();
        Role role1 = roleRepository.findById(role).orElse(null);
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setGender(user.getGender());
        user.setMobileNo(user.getMobileNo());
        user.setDepartment(user.getDepartment());
        user.setActive(user.getActive());
        user.setRole(role1);
        userService.editSave(user);
        modelAndView.addObject("successMessage", "User has been Updated successfully");
        modelAndView.addObject("user", user);
        System.out.println(user);
        modelAndView.setViewName("redirect:/admin/home");
        return modelAndView;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        userService.deleteById(id);
        modelAndView.addObject("successMessage", "User Deleted Successfully.");
        modelAndView.setViewName("redirect:/admin/home");
        return modelAndView;
    }
}

