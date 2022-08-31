package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.service.ItemService;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentMapper {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public Comment convertFromDto(CommentDto commentDto, long userId, long itemId) {

        Item item = itemService.getById(itemId);
        User author = userService.getById(userId);
        Optional<Booking> booking = bookingRepository.findFirstByItem_IdAndEndIsBefore(itemId, LocalDateTime.now());

        if (booking.isEmpty() ||
                booking.get().getBooker().getId() != userId) {
            ValidationException e = new ValidationException(
                    "User " + userId + " not booked item " + itemId + " or end date no finished"
            );
            log.error(e.getMessage());
            throw e;
        }

        if (commentDto.getText().isBlank()) {
            ValidationException e = new ValidationException("Text is blank");
            log.error(e.getMessage());
            throw e;
        }
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        return comment;
    }

    public CommentDto convertToDto(Comment comment) {

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

}
