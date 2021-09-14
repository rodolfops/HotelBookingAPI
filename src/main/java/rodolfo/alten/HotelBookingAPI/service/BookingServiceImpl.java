package rodolfo.alten.HotelBookingAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rodolfo.alten.HotelBookingAPI.constants.ErrorMessages;
import rodolfo.alten.HotelBookingAPI.domain.Booking;
import rodolfo.alten.HotelBookingAPI.exception.BookingException;
import rodolfo.alten.HotelBookingAPI.exception.BookingNotFoundException;
import rodolfo.alten.HotelBookingAPI.repository.BookingRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private ValidationService validationService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              ValidationService validationService) {
        this.bookingRepository = bookingRepository;
        this.validationService = validationService;
    }

    @Transactional
    @Override
    public Booking placeReservation(Booking booking) {
        validationService.validateReservation(booking);
        if(!isBookingAvailable(booking)){
            throw new BookingException(ErrorMessages.BOOKING_NOT_AVAILABLE);
        }
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public void cancelReservation(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<Booking> checkAvailability() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking modifyReservation(Long bookingId, Booking booking) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(!optionalBooking.isPresent()) {
            throw new BookingNotFoundException(ErrorMessages.BOOKING_NOT_FOUND);
        }

        Booking actualBooking = optionalBooking.get();
        validationService.validateReservation(booking);
        if(!isBookingAvailable(booking)){
            throw new BookingException(ErrorMessages.BOOKING_NOT_AVAILABLE);
        }

        actualBooking.update(booking.getFrom(), booking.getTo());
        bookingRepository.save(actualBooking);
        return actualBooking;
    }

    private boolean isBookingAvailable(Booking booking){
        List<Booking> bookings = bookingRepository.findBookingByPeriod(booking.getFrom(), booking.getTo());
        if(Objects.nonNull(booking.getId())){
            bookings.removeIf(booking1 -> booking1.getId().equals(booking.getId()));
        }
        return bookings.isEmpty();
    }
}
