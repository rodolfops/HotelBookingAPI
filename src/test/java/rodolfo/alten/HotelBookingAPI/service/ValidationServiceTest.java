package rodolfo.alten.HotelBookingAPI.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rodolfo.alten.HotelBookingAPI.constants.ErrorMessages;
import rodolfo.alten.HotelBookingAPI.domain.Booking;
import rodolfo.alten.HotelBookingAPI.exception.BookingException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    @InjectMocks
    ValidationServiceImpl validationService;

    @Test
    public void shouldValidate(){
        Booking booking = new Booking(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        validationService.validateReservation(booking);
    }

    @Test
    public void reservationShouldStartNextDay(){
        Booking booking = new Booking(LocalDate.now(), LocalDate.now().plusDays(3));
        Exception exception = assertThrows(BookingException.class, () -> {
            validationService.validateReservation(booking);
        });
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(ErrorMessages.STAY_START_INVALID));
    }

    @Test
    public void reservationFromShouldBeBeforeThanToDate(){
        Booking booking = new Booking(LocalDate.now().plusDays(5), LocalDate.now().plusDays(3));
        Exception exception = assertThrows(BookingException.class, () -> {
            validationService.validateReservation(booking);
        });
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(ErrorMessages.FROM_DATE_AFTER_TO_DATE));
    }

    @Test
    public void reservationShouldNotExceedMaximunStay(){
        Booking booking = new Booking(LocalDate.now().plusDays(1), LocalDate.now().plusDays(6));
        Exception exception = assertThrows(BookingException.class, () -> {
            validationService.validateReservation(booking);
        });
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(ErrorMessages.STAY_LONGER_THAN_LIMIT));
    }

    @Test
    public void reservationShouldNotBeMoreThan30DaysInAdvance(){
        Booking booking = new Booking(LocalDate.now().plusDays(32), LocalDate.now().plusDays(34));
        Exception exception = assertThrows(BookingException.class, () -> {
            validationService.validateReservation(booking);
        });
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(ErrorMessages.DAYS_IN_ADVANCE_LONGER_THAN_LIMIT));
    }

}
