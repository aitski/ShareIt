package ru.yandex.practicum.ShareIt.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.ShareIt.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@ToString
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    long id;

    @NotBlank
    @Size(max = 3000)
    String description;

    @ManyToOne
    @JoinColumn(name = "requester")
    User requestor;
}
