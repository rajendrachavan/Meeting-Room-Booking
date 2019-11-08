package neo.spring5.MeetingRoomBooking.controllers;

import neo.spring5.MeetingRoomBooking.models.Token;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.TokenRepository;
import neo.spring5.MeetingRoomBooking.services.EmailService;
import neo.spring5.MeetingRoomBooking.services.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Controller
public class PasswordController {

    private final UserService userService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenRepository tokenRepository;

    public PasswordController(UserService userService,
                              EmailService emailService,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              TokenRepository tokenRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public ModelAndView forgotPassword(ModelAndView modelAndView){
        modelAndView.setViewName("forgot-password");
        return modelAndView;
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ModelAndView forgotPasswordProcess(ModelAndView modelAndView,
                                       @RequestParam("email") String email) throws MessagingException {
        User user = userService.findUserByEmail(email);
        if(user == null){
            modelAndView.addObject("successMessage", "We didn't find an account for that email address.");
        } else{
            Token token = new Token();
            token.setToken(UUID.randomUUID().toString());
            token.setUser(user);
            tokenRepository.save(token);

            String appUrl = "http://localhost:8080";
            String subject= "Password Reset Request";
            String body = "To reset your password, click the link below:\n" +"<a href='"+ appUrl
                    + "/reset-password?token=" + token.getToken()+"'>Reset link</a>";
            emailService.sendEmail("rajendra.chavan@neosofttech.com", subject, body);
            modelAndView.addObject("successMessage", "A password reset link has been sent to " + email);
        }
        modelAndView.setViewName("forgot-password");
        return modelAndView;
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.GET)
    public ModelAndView resetPassword(ModelAndView modelAndView, @RequestParam("token") String token){
        Token token1 = tokenRepository.findByToken(token);
        User user = userService.findById(token1.getUser().getId()).orElse(null);
        if(user == null){
            modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
        } else {
            modelAndView.addObject("resetToken", token1.getToken());
        }
        modelAndView.setViewName("reset-password");
        return modelAndView;
    }

    @RequestMapping(value = "/reset-password/{resetToken}", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView,
                                       @PathVariable("resetToken") String token,
                                       @RequestParam("password") String password){
        Token token1 = tokenRepository.findByToken(token);
        User user = userService.findById(token1.getUser().getId()).orElse(null);
        if(user == null){
            modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
            modelAndView.setViewName("reset-password");
        } else{
            token1.setToken(UUID.randomUUID().toString());
            tokenRepository.save(token1);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userService.editSave(user);
            modelAndView.addObject("successMessage", "You have successfully reset your password.  You may now login.");
            modelAndView.setViewName("reset-password");
        }
        return modelAndView;
    }
}