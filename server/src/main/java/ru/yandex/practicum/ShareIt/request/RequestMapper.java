package ru.yandex.practicum.ShareIt.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.ItemMapper;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.service.ItemService;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.request.model.RequestDto;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RequestMapper {

    UserService userService;
    ItemService itemService;
    ItemRepository itemRepository;
    ItemMapper itemMapper;

    public RequestDto convertToDto(Request request) {

        List<ItemDto> list = itemRepository.findByRequestId(
                request.getId(),
                Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(item -> itemMapper.convertToDto(item,item.getOwner().getId()))
                .collect(Collectors.toList());

        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                list
        );
    }

    public Request convertFromDto(RequestDto requestDto, long userId){

        User requester = userService.getById(userId);
        Request request = new Request();

        if (requestDto.getDescription() != null
                && !(requestDto.getDescription().isBlank())) {
            request.setDescription(requestDto.getDescription());
        } else {
            ValidationException e = new ValidationException("Blank description");
            log.error(e.getMessage());
            throw e;
        }

        request.setRequester(requester);
        request.setCreated(LocalDateTime.now().withNano(0));
        return request;
    }
}
