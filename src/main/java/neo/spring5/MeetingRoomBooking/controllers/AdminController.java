package neo.spring5.MeetingRoomBooking.controllers;

import lombok.val;
import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value="/user-management/{page}", method = RequestMethod.GET)
    public ModelAndView userManagement(@PathVariable(value = "page") int page,
                                       @RequestParam(defaultValue = "id") String sortBy){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
        Page<User> userPage = userService.getPaginatedUsers(pageable);
        int totalPages = userPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("activeUserList", true);
        modelAndView.addObject("userManagement","User Management");
        modelAndView.addObject("users", userPage.getContent());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.setViewName("admin/user-management");
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
        User userDataDB = userService.findById(id).orElse(null);
        Role role1 = roleRepository.findById(role).orElse(null);
        user.setPassword(userDataDB.getPassword());
        userService.editSave(user);
        modelAndView.addObject("successMessage", "User has been Updated successfully");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("redirect:/admin/user-management/1");
        return modelAndView;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        userService.deleteById(id);
        modelAndView.addObject("successMessage", "User Deleted Successfully.");
        modelAndView.setViewName("redirect:/admin/user-management/1");
        return modelAndView;
    }
}

