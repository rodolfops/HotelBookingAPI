package rodolfo.alten.HotelBookingAPI.service;

import org.springframework.stereotype.Service;
import rodolfo.alten.HotelBookingAPI.domain.Booking;
import rodolfo.alten.HotelBookingAPI.exception.BookingException;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static rodolfo.alten.HotelBookingAPI.constants.Constants.*;
import static rodolfo.alten.HotelBookingAPI.constants.ErrorMessages.*;

@Service
public class ValidationServiceImpl implements ValidationService{

    @Override
    public void validateReservation(Booking booking) {
        if(!booking.getFrom().isAfter(LocalDate.now())){
            throw new BookingException(STAY_START_INVALID);
        }
        if(booking.getFrom().isAfter(booking.getTo())){
            throw new BookingException(FROM_DATE_AFTER_TO_DATE);
        }
        if(DAYS.between(booking.getFrom(), booking.getTo()) > MAXIMUM_STAY_DAYS){
            throw new BookingException(STAY_LONGER_THAN_LIMIT);
        }

        if(DAYS.between(LocalDate.now(), booking.getFrom()) > MAXIMUM_DAYS_IN_ADVANCE){
            throw new BookingException(DAYS_IN_ADVANCE_LONGER_THAN_LIMIT);
        }
    }
}
