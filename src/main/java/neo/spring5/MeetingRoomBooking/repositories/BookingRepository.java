package neo.spring5.MeetingRoomBooking.repositories;

import neo.spring5.MeetingRoomBooking.models.BookingDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<BookingDetails, Long> {
    BookingDetails findByDate(LocalDate date);
    Page<BookingDetails> findAll(Pageable pageable);
}
