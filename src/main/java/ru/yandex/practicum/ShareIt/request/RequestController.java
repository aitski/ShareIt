package ru.yandex.practicum.ShareIt.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.request.model.RequestDto;
import ru.yandex.practicum.ShareIt.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public Request create(@RequestBody RequestDto requestDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {

        return requestService.create(requestMapper.convertFromDto(requestDto, userId));
    }

    @GetMapping
    public List<RequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {

        List<RequestDto> list = requestService.getAll(userId).stream().
                map(requestMapper::convertToDto).collect(Collectors.toList());
        log.debug("List of requests returned for requester {} : {}", userId, list);
        return list;
    }

    @GetMapping("/all")
    public List<RequestDto> getAllPagination(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(defaultValue = "0", required = false) int from,
                                             @RequestParam(defaultValue = "10", required = false) int size) {

        List<RequestDto> list = requestService.getAllPagination(userId, from, size).stream().
                map(requestMapper::convertToDto).collect(Collectors.toList());
        log.debug("List of requests returned from {} size {}: {}", from, size, list);
        return list;
    }

    @GetMapping("{requestId}")
    public RequestDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long requestId) {

        RequestDto requestDto = requestMapper.convertToDto(requestService.getById(requestId, userId));
        log.debug("Item returned " + requestDto);
        return requestDto;
    }
}
