package rodolfo.alten.HotelBookingAPI.exception;

public class BookingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookingException(final String message) {
        super(message);
    }

}
