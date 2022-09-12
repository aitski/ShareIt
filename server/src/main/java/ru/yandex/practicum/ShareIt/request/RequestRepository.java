package ru.yandex.practicum.ShareIt.request;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ShareIt.request.model.Request;

import java.util.List;


public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findByRequesterId (long requesterId, Sort sort);
    Page<Request> findAll (Pageable pageable);
}
