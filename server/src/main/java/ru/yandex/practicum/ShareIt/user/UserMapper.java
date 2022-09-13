package ru.yandex.practicum.ShareIt.user;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.model.UserDto;

@Service
@AllArgsConstructor
public class UserMapper {

    public UserDto convertToDto(User user) {

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User convertFromDto(User user, UserDto userDto) {

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }
}
