package ru.yandex.practicum.ShareIt.ItemTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.exception.exceptions.OwnershipException;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.CommentMapper;
import ru.yandex.practicum.ShareIt.item.ItemMapper;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.storage.CommentRepository;
import ru.yandex.practicum.ShareIt.request.RequestRepository;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    CommentMapper commentMapper;
    @Mock
    RequestRepository requestRepository;
    @Mock
    UserService userService;
    @InjectMocks
    ItemMapper itemMapper;

    @Test
    public void whenConvertToDto_thenReturnDto() {


        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setRequest(request);
        item.setDescription("desc");
        item.setAvailable(true);

        Booking lastB = new Booking();
        lastB.setId(1L);
        lastB.setBooker(user);

        Booking nextB = new Booking();
        nextB.setId(2L);
        nextB.setBooker(user);

        Optional<Booking> last = Optional.of(lastB);
        Optional<Booking> next = Optional.of(nextB);

        Comment comment = new Comment();

        CommentDto commentDto = new CommentDto(
                1,
                "text",
                "name",
                null
        );

        when(userService.getById(1))
                .thenReturn(user);

        when(bookingRepository.findFirstByItem_IdAndEndIsBefore(any(), any()))
                .thenReturn(last);
        when(bookingRepository.findFirstByItem_IdAndStartIsAfter(any(), any()))
                .thenReturn(next);

        when(commentRepository.findAllByItem_Id(item.getId()))
                .thenReturn(List.of(comment));

        when(commentMapper.convertToDto(any()))
                .thenReturn(commentDto);

        assertEquals(1, itemMapper.convertToDto(item, 1).getId());
        assertEquals("desc", itemMapper.convertToDto(item, 1).getDescription());
        assertEquals(1, itemMapper.convertToDto(item, 1).getRequestId());
        assertEquals(true, itemMapper.convertToDto(item, 1).getAvailable());
        assertEquals(List.of(commentDto), itemMapper.convertToDto(item, 1).getComments());
        assertEquals(1, itemMapper.convertToDto(item, 1).getLastBooking().getId());
        assertEquals(1, itemMapper.convertToDto(item, 1).getLastBooking().getBookerId());
        assertEquals(2, itemMapper.convertToDto(item, 1).getNextBooking().getId());
        assertEquals(1, itemMapper.convertToDto(item, 1).getNextBooking().getBookerId());
    }

    @Test
    public void whenConvertToDto_thenReturnLastNextBookingNull() {


        User user = new User();
        user.setId(1L);

        User owner = new User();
        owner.setId(2L);

        Request request = new Request();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setRequest(request);
        item.setDescription("desc");
        item.setAvailable(true);

        Booking lastB = new Booking();
        lastB.setId(1L);
        lastB.setBooker(user);

        Booking nextB = new Booking();
        nextB.setId(2L);
        nextB.setBooker(user);

        Optional<Booking> last = Optional.of(lastB);
        Optional<Booking> next = Optional.of(nextB);

        Comment comment = new Comment();

        CommentDto commentDto = new CommentDto(
                1,
                "text",
                "name",
                null
        );

        when(userService.getById(1))
                .thenReturn(user);

        when(bookingRepository.findFirstByItem_IdAndEndIsBefore(any(), any()))
                .thenReturn(last);
        when(bookingRepository.findFirstByItem_IdAndStartIsAfter(any(), any()))
                .thenReturn(next);

        when(commentRepository.findAllByItem_Id(item.getId()))
                .thenReturn(List.of(comment));

        when(commentMapper.convertToDto(any()))
                .thenReturn(commentDto);

        assertNull(itemMapper.convertToDto(item, 1).getLastBooking());
        assertNull(itemMapper.convertToDto(item, 1).getNextBooking());
    }

    @Test
    public void whenConvertToDtoNullItemReq_thenReturn0ReqId() {


        User user = new User();
        user.setId(1L);

        Request request = new Request();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("desc");
        item.setAvailable(true);

        Booking lastB = new Booking();
        lastB.setId(1L);
        lastB.setBooker(user);

        Booking nextB = new Booking();
        nextB.setId(2L);
        nextB.setBooker(user);

        Optional<Booking> last = Optional.of(lastB);
        Optional<Booking> next = Optional.of(nextB);

        Comment comment = new Comment();

        CommentDto commentDto = new CommentDto(
                1,
                "text",
                "name",
                null
        );

        when(userService.getById(1))
                .thenReturn(user);

        when(bookingRepository.findFirstByItem_IdAndEndIsBefore(any(), any()))
                .thenReturn(last);
        when(bookingRepository.findFirstByItem_IdAndStartIsAfter(any(), any()))
                .thenReturn(next);

        when(commentRepository.findAllByItem_Id(item.getId()))
                .thenReturn(List.of(comment));

        when(commentMapper.convertToDto(any()))
                .thenReturn(commentDto);

        assertEquals(0, itemMapper.convertToDto(item, 1).getRequestId());
    }

    @Test
    public void whenConvertFromDtoPatch_thenReturnItem() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("desc");
        item.setName("item");
        item.setAvailable(true);

        ItemDto itemDto = new ItemDto(
                1,
                "item1",
                "desc1",
                false,
                null,
                null,
                null,
                0);

        assertEquals("item1", itemMapper.convertFromDtoPatch(item, itemDto, 1).getName());
        assertEquals("desc1", itemMapper.convertFromDtoPatch(item, itemDto, 1).getDescription());
        assertEquals(false, itemMapper.convertFromDtoPatch(item, itemDto, 1).getAvailable());
    }

    @Test
    public void whenConvertFromDtoPatch_thenReturnNoChange() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("desc");
        item.setName("item");
        item.setAvailable(true);

        ItemDto itemDto1 = new ItemDto(
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                0);

        assertEquals("item", itemMapper.convertFromDtoPatch(item, itemDto1, 1).getName());
        assertEquals("desc", itemMapper.convertFromDtoPatch(item, itemDto1, 1).getDescription());
        assertEquals(true, itemMapper.convertFromDtoPatch(item, itemDto1, 1).getAvailable());
    }

    @Test
    public void whenConvertFromDtoPatch_thenOwnershipException() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setDescription("desc");
        item.setName("item");
        item.setAvailable(true);

        ItemDto itemDto = new ItemDto(
                1,
                "item1",
                "desc1",
                false,
                null,
                null,
                null,
                0);

        assertThrows(OwnershipException.class,
                () -> itemMapper.convertFromDtoPatch(item, itemDto, 1));
    }

    @Test
    public void whenConvertFromDtoCreate_thenReturnItem() {

        ItemDto itemDto = new ItemDto(
                0,
                "item",
                "desc",
                true,
                null,
                null,
                null,
                1);

        Request request = new Request();
        request.setId(1L);

        when(requestRepository.findById(itemDto.getRequestId()))
                .thenReturn(Optional.of(request));

        assertEquals("item", itemMapper.convertFromDtoCreate(itemDto).getName());
        assertEquals("desc", itemMapper.convertFromDtoCreate(itemDto).getDescription());
        assertEquals(true, itemMapper.convertFromDtoCreate(itemDto).getAvailable());
        assertEquals(1, itemMapper.convertFromDtoCreate(itemDto).getRequest().getId());
    }

    @Test
    public void whenConvertFromDtoCreateNullName_thenReturnException() {

        ItemDto itemDto = new ItemDto(
                0,
                null,
                "desc",
                true,
                null,
                null,
                null,
                1);

        assertThrows(ValidationException.class,
                ()->itemMapper.convertFromDtoCreate(itemDto));
    }

    @Test
    public void whenConvertFromDtoCreateNullDesc_thenReturnException() {

        ItemDto itemDto = new ItemDto(
                0,
                "name",
                null,
                true,
                null,
                null,
                null,
                1);

        assertThrows(ValidationException.class,
                ()->itemMapper.convertFromDtoCreate(itemDto));
    }

    @Test
    public void whenConvertFromDtoCreateNullAvail_thenReturnException() {

        ItemDto itemDto = new ItemDto(
                0,
                "name",
                "desc",
                null,
                null,
                null,
                null,
                1);

        assertThrows(ValidationException.class,
                ()->itemMapper.convertFromDtoCreate(itemDto));
    }

    @Test
    public void whenConvertFromDtoCreateReqId0_thenReturnItem() {

        ItemDto itemDto = new ItemDto(
                0,
                "item",
                "desc",
                true,
                null,
                null,
                null,
                0);

        assertNull(itemMapper.convertFromDtoCreate(itemDto).getRequest());
    }
}
