package ru.yandex.practicum.ShareIt.RequestTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.request.RequestRepository;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.request.service.RequestServiceImpl;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceJUnitTest {

    @Mock
    RequestRepository mockRequestRepository;

    @Mock
    UserService userService;

    @InjectMocks
    RequestServiceImpl requestService;

    User user = new User();

    @Test
    public void whenCreateNewRequest_thenReturnNewRequestWithId() {

        Request request = new Request();
        request.setDescription("request");

        Request requestDAO = new Request();
        requestDAO.setId(1L);
        requestDAO.setDescription("request");

        when(mockRequestRepository.save(request))
                .thenReturn(requestDAO);

        Assertions.assertEquals(requestDAO, requestService.create(request));
    }

    @Test
    public void whenGetById_thenReturnRequest() {

        Request request = new Request();
        request.setId(1L);
        request.setDescription("request");

        when(userService.getById(1))
                .thenReturn(user);

        when(mockRequestRepository.findById(1L))
                .thenReturn(Optional.of(request));

        Assertions.assertEquals(request, requestService.getById(1, 1));
    }

    @Test
    public void whenGetById_thenReturnException() {

        when(userService.getById(1))
                .thenReturn(user);

        when(mockRequestRepository.findById(1L))
                .thenThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> requestService.getById(1, 1));
    }

    @Test
    public void whenRequestGetAll_thenReturnListOfRequests() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Request> list = new ArrayList<>();

        when(mockRequestRepository.findByRequesterId(1,
                        Sort.by(Sort.Direction.DESC, "created")))
                .thenReturn(list);

        Assertions.assertEquals(list, requestService.getAll(1));
    }

    @Test
    public void whenRequestGetAllPagination_thenReturnListOfRequests() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Request> list = new ArrayList<>();

        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created"));
        Page<Request> page = new PageImpl<>(list,pageRequest,0);

        when(mockRequestRepository.findAll(pageRequest))
                .thenReturn(page);

        Assertions.assertEquals(list, requestService.getAllPagination(1,0,10));
    }

}
