package ru.yandex.practicum.ShareIt.RequestTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.ItemMapper;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.service.ItemService;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.request.RequestMapper;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.request.model.RequestDto;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestMapperTest {

    @Mock
    UserService userService;
    @Mock
    ItemService itemService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemMapper itemMapper;

    @InjectMocks
    RequestMapper requestMapper;

    @Test
    public void whenConvertToDto_thenReturnDto() {
        User user = new User();
        user.setId(1L);
        Request request = new Request();
        Item item = new Item();
        item.setOwner(user);
        ItemDto itemDto = new ItemDto(
                1, "item", "description", true,
                null, null, null, 0);

        request.setId(1L);
        request.setDescription("text");
        request.setCreated(LocalDateTime.of(2022, 8, 30, 22, 55));

        when(itemRepository.findByRequestId(1, Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(List.of(item));

        when(itemMapper.convertToDto(item, 1))
                .thenReturn(itemDto);

        assertEquals(1, requestMapper.convertToDto(request).getId());
        assertEquals("text", requestMapper.convertToDto(request).getDescription());
        assertEquals(List.of(itemDto), requestMapper.convertToDto(request).getItems());
        assertEquals(LocalDateTime.of(2022, 8, 30, 22, 55)
                , requestMapper.convertToDto(request).getCreated());
    }

    @Test
    public void whenConvertFromDto_thenReturnRequest() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        RequestDto requestDto = new RequestDto(
                0,
                "text",
                null,
                null
        );

        assertEquals("text", requestMapper.convertFromDto(requestDto, 1).getDescription());
        assertEquals(user, requestMapper.convertFromDto(requestDto, 1).getRequester());
        assertEquals(LocalDateTime.now(), requestMapper.convertFromDto(requestDto, 1).getCreated());
    }

    @Test
    public void whenConvertFromDtoBlankDescr_thenReturnException() {

        User user = new User();
        user.setId(1L);

        when(userService.getById(1))
                .thenReturn(user);

        RequestDto requestDto = new RequestDto(
                0,
                "",
                null,
                null
        );

        assertThrows(ValidationException.class,
                ()->requestMapper.convertFromDto(requestDto,1));
    }
}
