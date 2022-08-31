package ru.yandex.practicum.ShareIt.request.service;

import ru.yandex.practicum.ShareIt.request.model.Request;

import java.util.List;

public interface RequestService {

    Request create (Request request);
    List<Request> getAll (long userId);
    List<Request> getAllPagination (long userId, int from, int size);
    Request getById (long requestId, long userId);

}
