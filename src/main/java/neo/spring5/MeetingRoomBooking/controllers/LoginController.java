package neo.spring5.MeetingRoomBooking.controllers;

import javax.validation.Valid;

import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
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
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, ModelAndView modelAndView) {
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
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
}
