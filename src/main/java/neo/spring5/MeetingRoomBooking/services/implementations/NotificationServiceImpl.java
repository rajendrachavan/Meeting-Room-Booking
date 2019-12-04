package neo.spring5.MeetingRoomBooking.services.implementations;

import neo.spring5.MeetingRoomBooking.models.Notification;
import neo.spring5.MeetingRoomBooking.models.Status;
import neo.spring5.MeetingRoomBooking.repositories.NotificationRepository;
import neo.spring5.MeetingRoomBooking.services.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public <Optional> Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteExpiredNotifications() {
        LocalDateTime today = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification notification: notifications) {
            if(notification.getExpiryDate().isBefore(today) && notification.getStatus() == Status.Read)
                notificationRepository.delete(notification);
        }
    }
}
