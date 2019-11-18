package neo.spring5.MeetingRoomBooking.controllers;

import javax.validation.Valid;

import neo.spring5.MeetingRoomBooking.models.Department;
import neo.spring5.MeetingRoomBooking.models.Token;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.DepartmentRepository;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.repositories.TokenRepository;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(ModelAndView modelAndView){
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(ModelAndView modelAndView){
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult,
									  @RequestParam(name = "department") Long dept_id,
									  ModelAndView modelAndView) {
		if(bindingResult.hasErrors() || dept_id == null){
			modelAndView.setViewName("registration");
			return modelAndView;
		}
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
			modelAndView.setViewName("registration");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/access-denied")
	public ModelAndView accessDenied(ModelAndView modelAndView){
		modelAndView.addObject("accessDeniedMessage", "Access Denied.");
		modelAndView.setViewName("access-denied");
		return modelAndView;
	}

	@RequestMapping(value = "/homepage", method = RequestMethod.GET)
	public ModelAndView homepage(ModelAndView modelAndView){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("id", user.getId());
		modelAndView.addObject("role", user.getRole().getRole());
		modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.setViewName("homepage");
		return modelAndView;
	}

	@RequestMapping(value = "/verifyEmail")
	public ModelAndView verifyEmail(ModelAndView modelAndView,
									@RequestParam("token") String token1){
		Token token = tokenRepository.findByToken(token1);
		User user = userService.findById(token.getUser().getId()).orElse(null);
		if(user.equals(null)){
			modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
			modelAndView.setViewName("login");
			return modelAndView;
		} else {
			user.setActive(1);
			userService.editSave(user);
			tokenRepository.delete(token);
			modelAndView.addObject("successMessage", "Email Verified Successfully, you can now login with your credentials");
			modelAndView.setViewName("login");
			return modelAndView;
		}
	}
}
