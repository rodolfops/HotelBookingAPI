package rodolfo.alten.HotelBookingAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import rodolfo.alten.HotelBookingAPI.constants.ErrorMessages;
import rodolfo.alten.HotelBookingAPI.domain.Booking;
import rodolfo.alten.HotelBookingAPI.exception.BookingException;
import rodolfo.alten.HotelBookingAPI.exception.BookingNotFoundException;
import rodolfo.alten.HotelBookingAPI.service.BookingService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Booking> json;
    private JacksonTester<List<Booking>> jsonList;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getAllBookings() throws Exception{
        // given
        LocalDate from = LocalDate.parse("2021-10-01");
        LocalDate to = LocalDate.parse("2021-10-03");
        Booking booking = new Booking(1l, from, to);
        given(bookingService.checkAvailability()).willReturn(Arrays.asList(booking));

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/v1/bookings")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jsonList.write(Arrays.asList(booking)).getJson());
    }

    @Test
    public void postReservation() throws Exception{
        // given
        LocalDate from = LocalDate.parse("2021-10-01");
        LocalDate to = LocalDate.parse("2021-10-03");
        Booking booking = new Booking(from, to);
        Booking savedBooking = new Booking(1l, from, to);
        given(bookingService.placeReservation(any())).willReturn((savedBooking));

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/v1/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(booking).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(json.write(savedBooking).getJson());
    }

    @Test
    public void postReservationBadRequestException() throws Exception{
        LocalDate from = LocalDate.parse("2021-10-01");
        LocalDate to = LocalDate.parse("2021-10-03");
        Booking booking = new Booking(from, to);
        given(bookingService.placeReservation(any())).willThrow(BookingException.class);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/v1/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(booking).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updateReservation() throws Exception{
        // given
        LocalDate from = LocalDate.parse("2021-10-01");
        LocalDate to = LocalDate.parse("2021-10-03");
        Booking booking = new Booking(1l, from, to);
        given(bookingService.modifyReservation(any(),any())).willReturn((booking));

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/v1/bookings/1").contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(booking).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(json.write(booking).getJson());
    }

    @Test
    public void updateReservationNotFoundException() throws Exception{
        // given
        given(bookingService.modifyReservation(any(),any())).willThrow(BookingNotFoundException.class);

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/v1/bookings/1").contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(new Booking()).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateReservationBadRequestException() throws Exception{
        // given
        given(bookingService.modifyReservation(any(),any())).willThrow(BookingException.class);

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/v1/bookings/1").contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(new Booking()).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteReservation() throws Exception{
        // given
        LocalDate from = LocalDate.parse("2021-10-01");
        LocalDate to = LocalDate.parse("2021-10-03");
        Booking booking = new Booking(1l, from, to);
        //doNothing().when(bookingService.cancelReservation(any()));

        // when
        MockHttpServletResponse response = mvc.perform(
                delete("/v1/bookings/1").contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
