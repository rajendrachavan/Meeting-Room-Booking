package neo.spring5.MeetingRoomBooking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {

        JavaMailSenderImpl j = null;
        Session session = null;
        Properties props = System.getProperties();

        // props.put("http.proxyHost", "10.0.60.33");
        props.put("http.proxyPort", "465");
        props.put("http.proxyUser", "rajendra.chavan@neosofttech.com");
        props.put("http.proxyPassword", "rajendrac12");
        props.put("mail.smtp.host", "webmail.wwindia.com");

        try {

            Message message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            message.setFrom(new InternetAddress("rajendra.chavan@neosofttech.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setContent(body, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            message.setContent(multipart);
            Transport.send(message);
        }catch(SendFailedException e) {
            e.printStackTrace();
        }catch (MessagingException e) {
            System.out.println("Mail send error");
            throw new RuntimeException(e);
        }
    }



    /*@Autowired
    private JavaMailSender mailSender;*/

    /*@Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }*/
}