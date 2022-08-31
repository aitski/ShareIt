package ru.yandex.practicum.ShareIt.UserTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserServiceImpl;
import ru.yandex.practicum.ShareIt.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceJUnitTest {

    @Mock
    UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void whenCreateNewUser_thenReturnNewUserWithId() {

        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");

        User userDAO = new User();
        userDAO.setId(1L);
        userDAO.setName("user");
        userDAO.setEmail("user@user.com");

        Mockito
                .when(mockUserRepository.save(user))
                .thenReturn(userDAO);

        Assertions.assertEquals(userDAO, userService.create(user));
    }

    @Test
    public void whenCreateNewUser_thenCallRepository() {
        User user = new User();
        userService.create(user);
        Mockito.verify(mockUserRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void whenGetById_thenReturnUser() {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");

        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(user, userService.getById(1));
    }

    @Test
    public void whenGetById_thenReturnException() {

        Mockito
                .when(mockUserRepository.findById(1L))
                .thenThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> userService.getById(1));
    }

    @Test
    public void whenUpdateUser_thenReturnUpdated() {

        User userUpdated = new User();
        userUpdated.setName("userUpdated");
        userUpdated.setEmail("updated@user.com");

        Mockito
                .when(mockUserRepository.save(userUpdated))
                .thenReturn(userUpdated);

        Assertions.assertEquals(userUpdated, userService.update(userUpdated));
    }

    @Test
    public void whenUserGetAll_thenReturnListOfUsers() {

        List<User> list = new ArrayList<>();

        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(list);

        Assertions.assertEquals(list, userService.getAll());
    }

    @Test
    public void whenDeleteUser_thenCallRepository() {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");

        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.delete(1);

        Mockito.verify(mockUserRepository,
                Mockito.times(1)).delete(user);
    }


}
