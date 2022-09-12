package ru.yandex.practicum.ShareIt.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.request.RequestRepository;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    @Override
    public Request create(Request request) {

        Request requestNew = requestRepository.save(request);
        log.debug("request created: {}", requestNew);
        return requestNew;
    }

    @Override
    public List<Request> getAll(long userId) {

        userService.getById(userId);
        return requestRepository.findByRequesterId(
                userId,
                Sort.by(Sort.Direction.DESC, "created"));
    }

    @Override
    public List<Request> getAllPagination(long userId, int from, int size) {

        userService.getById(userId);
        Pageable pageRequest = PageRequest.of(from/size, size, Sort.by(Sort.Direction.DESC, "created"));
        return requestRepository.findAll(pageRequest).getContent().stream()
                .filter(request -> !request.getRequester().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Request getById(long requestId, long userId) {

        userService.getById(userId);
        return requestRepository.findById(requestId).orElseThrow
                (() -> {
                    log.error("Request with id {} not found", requestId);
                    return new NotFoundException();
                });
    }
}
