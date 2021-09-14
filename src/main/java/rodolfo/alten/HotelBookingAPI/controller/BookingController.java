package rodolfo.alten.HotelBookingAPI.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rodolfo.alten.HotelBookingAPI.domain.Booking;
import rodolfo.alten.HotelBookingAPI.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ApiOperation(value = "Delete a booking by its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Booking not found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @ApiParam(value = "The booking id") @NotNull final String id) {
        log.info("Deleting bookingId={}.", id);
        bookingService.cancelReservation(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Save a new booking")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Booking> save(@RequestBody @ApiParam(value = "The booking data") @Valid final Booking bookingRequest) {
        log.info("Inserting booking: {}.", bookingRequest);

        final Booking bookingResponse = bookingService.placeReservation(bookingRequest);

        return ResponseEntity.created(URI.create("/v1/bookings/" + bookingResponse.getId())).
                body(bookingResponse);
    }

    @ApiOperation(value = "Update a booking by its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Booking not found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    public ResponseEntity<Booking> update(@RequestBody @ApiParam(value = "The booking data")
                                       @Valid final Booking bookingRequest,
                                       @PathVariable("id") @ApiParam(value = "The booking id")
                                       @NotNull final String id) {
        log.info("Updating bookingId={} and bodyRequest={}.", id, bookingRequest);

        final Booking bookingResponse = bookingService.modifyReservation(Long.valueOf(id), bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @ApiOperation(value = "Find all bookings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @GetMapping
    public ResponseEntity<List<Booking>> findAll() {
        log.info("Finding all bookings.");
        return ResponseEntity.ok(bookingService.checkAvailability());
    }
}
