package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoGateway;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestControllerGateway {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid RequestDtoGateway request,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Creating request {}, user {}", request, userId);
        return requestClient.create(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all requests, user {}", userId);
        return requestClient.getAll(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllPagination(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get all requests user {}, from {}, size {}", userId, from, size);
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long requestId) {
        log.info("Get request {}, user {}", requestId, userId);
        return requestClient.getById(requestId, userId);
    }

}
