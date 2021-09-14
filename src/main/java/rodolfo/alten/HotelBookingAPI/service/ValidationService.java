package rodolfo.alten.HotelBookingAPI.service;

import rodolfo.alten.HotelBookingAPI.domain.Booking;

public interface ValidationService {
    void validateReservation(Booking booking);
}
