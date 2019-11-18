package neo.spring5.MeetingRoomBooking.controllers;

import lombok.val;
import neo.spring5.MeetingRoomBooking.models.ChangeRequest;
import neo.spring5.MeetingRoomBooking.models.Department;
import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.ChangeRequestRepository;
import neo.spring5.MeetingRoomBooking.repositories.DepartmentRepository;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.services.EmailService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ChangeRequestRepository changeRequestRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @RequestMapping(value="/user-management/{page}", method = RequestMethod.GET)
    public ModelAndView userManagement(ModelAndView modelAndView,
                                       @PathVariable(value = "page") int page,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @ModelAttribute("successMessage") String successMessage,
                                       @ModelAttribute("errorMessage") String errorMessage){

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
        modelAndView.addObject("successMessage", successMessage);
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setViewName("admin/user-management");
        return modelAndView;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public ModelAndView updatepage(ModelAndView modelAndView,
                                   @PathVariable(value="id") Long id) throws Exception {

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
    public ModelAndView editUser(ModelAndView modelAndView,
                                 @PathVariable(value="id") Long id,
                                 @RequestParam(name = "role") Long role,
                                 @RequestParam(name = "department") Long dept_id,
                                 @Valid @ModelAttribute("user") User user,
                                 RedirectAttributes redirectAttributes){
        User userDataDB = userService.findById(id).orElse(null);
        Department department = departmentRepository.findById(dept_id).orElse(null);
        user.setDepartment(department);
        user.setPassword(userDataDB.getPassword());
        userService.editSave(user);
        redirectAttributes.addFlashAttribute("successMessage", "User has been Updated successfully");
        modelAndView.setViewName("redirect:/admin/user-management/1");
        return modelAndView;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView delete(ModelAndView modelAndView,
                               @PathVariable Long id,
                               RedirectAttributes redirectAttributes){
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "User Deleted Successfully.");
        modelAndView.setViewName("redirect:/admin/user-management/1");
        return modelAndView;
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userRole = userService.findUserByEmail(auth.getName());
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("role", userRole.getRole().getRole());
        modelAndView.setViewName("admin/add-user");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user,
                                      @RequestParam(name = "department") Long dept_id,
                                      ModelAndView modelAndView) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            modelAndView.addObject("errorMessage", "User Already Exists.");
            modelAndView.setViewName("registration");
        } else {
            Department department = departmentRepository.findById(dept_id).orElse(null);
            user.setDepartment(department);
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("admin/add-user");
        }
        return modelAndView;
    }
}

