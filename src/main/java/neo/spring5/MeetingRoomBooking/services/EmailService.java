package neo.spring5.MeetingRoomBooking.services;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
/*
    public void sendEmail(SimpleMailMessage email);*/

    public void sendEmail(String to, String subject, String body);
}
