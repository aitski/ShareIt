package ru.yandex.practicum.ShareIt.request.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.ShareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;

    @NotBlank
    @Size(max = 3000)
    String description;

    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "requester")
    User requester;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        return id != null && id.equals(((Request) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
