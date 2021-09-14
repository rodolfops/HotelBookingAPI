package rodolfo.alten.HotelBookingAPI.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rodolfo.alten.HotelBookingAPI.domain.Booking;
import rodolfo.alten.HotelBookingAPI.repository.BookingRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    BookingServiceImpl bookingService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ValidationServiceImpl validationService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingServiceImpl(bookingRepository,validationService);
    }

    @Test
    public void shouldPlaceAReservation(){
        //given our mocked Booking
        Booking booking = new Booking(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        //when call place a reservation
        Booking reservation = bookingService.placeReservation(booking);
        //then should work
        assertThat(reservation).isEqualTo(booking);
    }
}
