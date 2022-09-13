package ru.yandex.practicum.ShareIt.BookingTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.ShareIt.booking.BookingController;
import ru.yandex.practicum.ShareIt.booking.BookingMapper;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.BookingDto;
import ru.yandex.practicum.ShareIt.booking.service.BookingServiceImpl;
import ru.yandex.practicum.ShareIt.item.model.Item;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.ShareIt.booking.model.Status.APPROVED;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingServiceImpl bookingService;
    @MockBean
    private BookingMapper bookingMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenCreateBooking_thenReturnNewBooking() throws Exception {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(new Item());

        when(bookingMapper.convertFromDto(any(), anyLong()))
                .thenReturn(booking);

        when(bookingService.create(booking))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"itemId\": \"1\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void whenPatchBooking_thenReturnUpdatedBooking() throws Exception {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(APPROVED);

        when(bookingService.updateStatus(1, 1, true))
                .thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    public void whenGetById_thenReturnBooking() throws Exception {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(new Item());

        when(bookingService.getById(1, 1))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void whenGetAllBookings_thenReturnListOfBookings() throws Exception {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(new Item());

        when(bookingService.getAll("REJECTED", 1, 0, 20))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void whenGetAllBookingsByOwner_thenReturnListOfBookings() throws Exception {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(new Item());

        when(bookingService.getAllByOwner("REJECTED", 1, 0, 20))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

}
