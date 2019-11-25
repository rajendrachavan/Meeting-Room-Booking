package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.BookingDetails;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void save(BookingDetails bookingDetails) {
        bookingRepository.save(bookingDetails);
    }

    @Override
    public List<BookingDetails> findAll() {
        List<BookingDetails> bookingDetails = bookingRepository.findAll();
        return bookingDetails;
    }

    @Override
    public Optional<BookingDetails> findById(Long id) {
        Optional<BookingDetails> bookingDetails = bookingRepository.findById(id);
        return bookingDetails;
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Page<BookingDetails> getPaginatedBookingDetails(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Page<BookingDetails> getPaginatedBookingDetails(User user, Pageable pageable) {
        return bookingRepository.findAllByUser(user, pageable);
    }

    @Override
    public List<BookingDetails> findAllByStatus(String status) {
        return bookingRepository.findAllByStatus(status);
    }

    @Override
    public List<BookingDetails> findAllByUser(User user) {
        return bookingRepository.findAllByUser(user);
    }

    @Override
    public List<BookingDetails> filterByMonth(YearMonth month) {
        Month month1 = month.getMonth();
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        for (BookingDetails bookingDetails :bookingRepository.findAll()) {
            if(bookingDetails.getStartTime().getYear() == month.getYear()){
                if(bookingDetails.getStartTime().getMonth() == (month1)){
                    bookingDetailsList.add(bookingDetails);
                }
            }
        }
        return bookingDetailsList;
    }
}
