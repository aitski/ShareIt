package ru.yandex.practicum.ShareIt.ItemTests;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> jsonTester;

    @Test
    void whenSerializeItemDto_thenReturnJson() throws IOException {

        ItemDto itemDto = new ItemDto(
                1,
                "item",
                "itemdesc",
                true,
                new ItemDto.Booking(1, 1),
                new ItemDto.Booking(2, 2),
                List.of(new CommentDto(
                        1,
                        "comment",
                        "author",
                        LocalDateTime.of(LocalDate.of(2022, 8, 26),
                                LocalTime.of(15, 31, 56)))),
                1
        );
        JsonContent<ItemDto> result = jsonTester.write(itemDto);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemdesc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathMapValue("$.lastBooking").isEqualTo(Map.of("id", 1, "bookerId", 1));
        assertThat(result).extractingJsonPathMapValue("$.nextBooking").isEqualTo(Map.of("id", 2, "bookerId", 2));
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void whenDeserializeJson_thenReturnItemDto() throws IOException {

        String json = "{\"id\":1,\"name\":\"item\",\"description\":\"itemdesc\",\"available\":true,\"lastBooking\":{\"id\":1,\"bookerId\":1},\"nextBooking\":{\"id\":2,\"bookerId\":2},\"comments\":[{\"id\":1,\"text\":\"comment\",\"authorName\":\"author\",\"created\":\"2022-08-26T15:31:56\"}],\"requestId\":1}";
        ItemDto itemDtoActual = jsonTester.parseObject(json);
        List<CommentDto> list = itemDtoActual.getComments();

        assertThat(itemDtoActual.getId()).isEqualTo(1);
        assertThat(itemDtoActual.getName()).isEqualTo("item");
        assertThat(itemDtoActual.getDescription()).isEqualTo("itemdesc");
        assertThat(itemDtoActual.getAvailable()).isEqualTo(true);
        assertThat(itemDtoActual.getLastBooking().getId()).isEqualTo(1);
        assertThat(itemDtoActual.getLastBooking().getBookerId()).isEqualTo(1);
        assertThat(itemDtoActual.getNextBooking().getId()).isEqualTo(2);
        assertThat(itemDtoActual.getNextBooking().getBookerId()).isEqualTo(2);
        assertThat(list.get(0).getId()).isEqualTo(1);
        assertThat(list.get(0).getText()).isEqualTo("comment");
        assertThat(list.get(0).getAuthorName()).isEqualTo("author");
        assertThat(list.get(0).getCreated()).isEqualTo(
                LocalDateTime.of(LocalDate.of(2022, 8, 26),
                        LocalTime.of(15, 31, 56)));
        assertThat(itemDtoActual.getRequestId()).isEqualTo(1);
    }
}
