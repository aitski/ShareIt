package ru.yandex.practicum.ShareIt.BookingTests.JUnitTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.ShareIt.booking.BookingMapper;
import ru.yandex.practicum.ShareIt.booking.model.BookingDto;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.service.ItemService;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.yandex.practicum.ShareIt.booking.model.Status.WAITING;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {

    @Mock
    ItemService itemService;
    @Mock
    UserService userService;
    @InjectMocks
    BookingMapper bookingMapper;

    @Test
    public void whenConvertFromDto_thenReturnBooking() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.of(2023, 12, 1, 12, 0));
        bookingDto.setEnd(LocalDateTime.of(2024, 12, 1, 12, 0));

        Item item = new Item();
        item.setId(1L);

        when(itemService.getById(1))
                .thenReturn(item);

        assertEquals(WAITING, bookingMapper.convertFromDto(bookingDto, 1L).getStatus());
        assertEquals(1, bookingMapper.convertFromDto(bookingDto, 1L).getBooker().getId());
        assertEquals(1, bookingMapper.convertFromDto(bookingDto, 1L).getItem().getId());
    }

    @Test
    public void whenConvertFromDtoPastStartEnd_thenReturnExcpetion() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.of(2020, 12, 1, 12, 0));
        bookingDto.setEnd(LocalDateTime.of(2020, 12, 1, 12, 0));

        Item item = new Item();
        item.setId(1L);

        when(itemService.getById(1))
                .thenReturn(item);

        assertThrows(ValidationException.class,
                () -> bookingMapper.convertFromDto(bookingDto, 1L));

    }

}
