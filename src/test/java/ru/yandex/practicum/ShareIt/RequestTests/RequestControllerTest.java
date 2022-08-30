package ru.yandex.practicum.ShareIt.RequestTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.ShareIt.request.RequestController;
import ru.yandex.practicum.ShareIt.request.RequestMapper;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.request.model.RequestDto;
import ru.yandex.practicum.ShareIt.request.service.RequestServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @MockBean
    private RequestServiceImpl requestService;
    @MockBean
    private RequestMapper requestMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenCreateRequest_thenReturnNewRequest() throws Exception {

        RequestDto requestDto = new RequestDto(
                0,
                "test",
                null,
                null);

        Request request = new Request();
        request.setId(1L);
        request.setDescription("test");

        when(requestMapper.convertFromDto(requestDto, 1))
                .thenReturn(request);

        when(requestService.create(any()))
                .thenReturn(request);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"description\": \"test\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("test")));
    }

    @Test
    public void whenGetAllRequests_thenReturnListOfRequests() throws Exception {

        RequestDto requestDto = new RequestDto(
                1,
                "test",
                null,
                null);

        Request request = new Request();
        request.setId(1L);
        request.setDescription("test");

        when(requestMapper.convertToDto(request))
                .thenReturn(requestDto);

        when(requestService.getAll(1))
                .thenReturn(List.of(request));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("test")));
    }

    @Test
    public void whenGetAllRequestsPaging_thenReturnListOfRequestsPaging() throws Exception {

        RequestDto requestDto = new RequestDto(
                1,
                "test",
                null,
                null);

        Request request = new Request();
        request.setId(1L);
        request.setDescription("test");

        when(requestMapper.convertToDto(request))
                .thenReturn(requestDto);

        when(requestService.getAll(1))
                .thenReturn(List.of(request));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .param("from","0")
                        .param("size","20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("test")));
    }

    @Test
    public void whenGetById_thenReturnRequest() throws Exception {

        RequestDto requestDto = new RequestDto(
                1,
                "test",
                null,
                null);

        Request request = new Request();
        request.setId(1L);
        request.setDescription("test");

        when(requestMapper.convertToDto(request))
                .thenReturn(requestDto);

        when(requestService.getById(1,1))
                .thenReturn(request);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("test")));
    }
}
