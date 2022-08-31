package ru.yandex.practicum.ShareIt.ItemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.ShareIt.item.CommentMapper;
import ru.yandex.practicum.ShareIt.item.ItemMapper;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.service.ItemServiceImpl;
import ru.yandex.practicum.ShareIt.item.ItemController;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.user.model.User;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @MockBean
    private ItemServiceImpl itemService;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAllItems_thenReturnListOfItems() throws Exception {

        ItemDto itemDto = new ItemDto(
                1,
                "item",
                "test",
                true,
                null,
                null,
                null,
                0);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("test");
        item.setAvailable(true);

        when(itemMapper.convertToDto(item,1))
                .thenReturn(itemDto);

        when(itemService.getAll(1,0,20))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from","0")
                        .param("size","20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("item")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[0].description", is("test")));
    }

    @Test
    public void whenGetById_thenReturnItem() throws Exception {

        ItemDto itemDto = new ItemDto(
                1,
                "item",
                "test",
                true,
                null,
                null,
                null,
                0);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("test");
        item.setAvailable(true);

        when(itemMapper.convertToDto(item,1))
                .thenReturn(itemDto);

        when(itemService.getById(1))
                .thenReturn(item);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("item")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.description", is("test")));
    }
    
    @Test
    public void whenCreateItem_thenReturnNewItem() throws Exception {

        ItemDto itemDto = new ItemDto(
                1,
                "item",
                "test",
                true,
                null,
                null,
                null,
                0);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("test");
        item.setAvailable(true);

        when(itemMapper.convertFromDtoCreate(any()))
                .thenReturn(item);

        when(itemService.create(item,1))
                .thenReturn(item);

        when(itemMapper.convertToDto(item,1))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"description\": \"test\", \"name\": \"item\",\"available\": \"true\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("test")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    public void whenCreateComment_thenReturnNewComment() throws Exception {

        CommentDto commentDto = new CommentDto(
                1,
                "comment",
                null,
                null
                );

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setAuthor(new User());

        when(commentMapper.convertFromDto(any(),anyInt(),anyInt()))
                .thenReturn(comment);

        when(itemService.createComment(any()))
                .thenReturn(comment);

        when(commentMapper.convertToDto(any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"text\": \"comment\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("comment")));
    }

    @Test
    public void whenPatchItem_thenReturnUpdatedItem() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("testNew");
        item.setAvailable(true);

        when(itemService.getById(1))
                .thenReturn(item);

        when(itemMapper.convertFromDtoPatch(any(),any(),anyInt()))
                .thenReturn(item);

        when(itemService.update(any()))
                .thenReturn(item);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content("{\"description\": \"testNew\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("testNew")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    public void whenSearchText_thenReturnListOfItems() throws Exception {

        ItemDto itemDto = new ItemDto(
                1,
                "item",
                "test",
                true,
                null,
                null,
                null,
                0);

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("test");
        item.setAvailable(true);

        when(itemMapper.convertToDto(item,1))
                .thenReturn(itemDto);

        when(itemService.search("searchText",0,20))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text","searchText")
                        .param("from","0")
                        .param("size","20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("item")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[0].description", is("test")));
    }

}
