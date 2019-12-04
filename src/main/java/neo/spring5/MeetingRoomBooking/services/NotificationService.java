package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.Notification;

public interface NotificationService {
    Notification save(Notification notification);
    <Optional> Notification findById(Long id);
    void deleteExpiredNotifications();
}
