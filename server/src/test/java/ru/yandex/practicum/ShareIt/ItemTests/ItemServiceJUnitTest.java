package ru.yandex.practicum.ShareIt.ItemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.service.ItemServiceImpl;
import ru.yandex.practicum.ShareIt.item.storage.CommentRepository;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceJUnitTest {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ItemServiceImpl itemService;

    User user = new User();

    @Test
    public void whenItemGetAll_thenReturnListOfItems() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Item> list = new ArrayList<>();

        Pageable pageItem = PageRequest.of(0, 10);
        Page<Item> page = new PageImpl<>(list,pageItem,0);

        when(mockItemRepository.findAll(pageItem))
                .thenReturn(page);

        Assertions.assertEquals(list, itemService.getAll(1,0,10));
    }

    @Test
    public void whenGetById_thenReturnItem() {

        Item item = new Item();
        item.setId(1L);
        item.setDescription("item");

        when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        Assertions.assertEquals(item, itemService.getById(1));
    }

    @Test
    public void whenGetById_thenReturnException() {

        when(mockItemRepository.findById(1L))
                .thenThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getById(1));
    }


    @Test
    public void whenCreateNewItem_thenReturnNewItemWithId() {

        Item item = new Item();
        item.setDescription("item");

        Item itemDAO = new Item();
        itemDAO.setId(1L);
        itemDAO.setDescription("item");

        when((userService.getById(1)))
                .thenReturn(new User());

        when(mockItemRepository.save(item))
                .thenReturn(itemDAO);

        Assertions.assertEquals(itemDAO, itemService.create(item,1));
    }

    @Test
    public void whenCreateNewComment_thenReturnNewCommentWithId() {

        Comment comment = new Comment();
        comment.setText("comment");

        Comment commentDAO = new Comment();
        commentDAO.setId(1L);
        commentDAO.setText("comment");

        when(commentRepository.save(comment))
                .thenReturn(commentDAO);

        Assertions.assertEquals(commentDAO, itemService.createComment(comment));
    }

    @Test
    public void whenPatchItem_thenReturnUpdatedItem() {

        Item itemDAO = new Item();
        itemDAO.setId(1L);
        itemDAO.setDescription("itemUpdated");

        when(mockItemRepository.save(itemDAO))
                .thenReturn(itemDAO);

        Assertions.assertEquals(itemDAO, itemService.update(itemDAO));
    }

    @Test
    public void whenItemSearch_thenReturnListOfItems() {

        List<Item> list = new ArrayList<>();

        Pageable pageItem = PageRequest.of(0, 10);
        Page<Item> page = new PageImpl<>(list,pageItem,0);

        when(mockItemRepository.findAll(pageItem))
                .thenReturn(page);

        Assertions.assertEquals(list, itemService.search("item",0,10));
    }

    @Test
    public void whenItemSearchBlannk_thenReturnEmptyList() {

        Assertions.assertEquals(Collections.emptyList(), itemService.search("",0,10));
    }

}
