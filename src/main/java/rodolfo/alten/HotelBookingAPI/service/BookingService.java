package rodolfo.alten.HotelBookingAPI.service;

import rodolfo.alten.HotelBookingAPI.domain.Booking;

import java.util.List;

public interface BookingService {

    Booking placeReservation(Booking booking);
    void cancelReservation(Long bookingId);
    List<Booking> checkAvailability();
    Booking modifyReservation(Long bookingId, Booking booking);

}
