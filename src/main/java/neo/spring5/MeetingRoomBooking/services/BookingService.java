package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.BookingDetails;
import neo.spring5.MeetingRoomBooking.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    void save(BookingDetails bookingDetails);
    List<BookingDetails> findAll();
    Optional<BookingDetails> findById(Long id);
    void deleteById(Long id);
    Page<BookingDetails> getPaginatedBookingDetails(Pageable pageable);
    Page<BookingDetails> getPaginatedBookingDetails(User user, Pageable pageable);
    List<BookingDetails> findAllByStatus(String status);
    List<BookingDetails> findAllByUser(User user);
    List<BookingDetails> filterByMonth(YearMonth month, User user);
}
