package ru.yandex.practicum.ShareIt.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@ToString

@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    @NotBlank
    @Size(max = 255)
    String name;

    @NotBlank
    @Size(max = 3000)
    String description;

    @NotNull
    Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner")
    User owner;

    @ManyToOne
    @JoinColumn(name = "request")
    Request request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
