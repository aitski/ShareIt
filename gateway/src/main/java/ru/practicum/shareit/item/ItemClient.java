package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoGateway;
import ru.practicum.shareit.item.dto.ItemDtoGateway;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAll(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> create(long userId, ItemDtoGateway item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> getById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> createComment(long userId, long itemId, CommentDtoGateway comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    public ResponseEntity<Object> patch(long userId, long itemId, ItemDtoGateway item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> search(String text, long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

}
