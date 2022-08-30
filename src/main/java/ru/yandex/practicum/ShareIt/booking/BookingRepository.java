package ru.yandex.practicum.ShareIt.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBooker_Id(Long bookerId, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatus(Long bookerId, Status status, Pageable pageable);

    @Query(value = "select b from Booking b join b.item i on i.owner.id = ?1")
    Page<Booking> findByOwner(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b join b.item i on i.owner.id = ?1 " +
            "and b.end < current_timestamp() ")
    Page<Booking> findByOwnerPast(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b join b.item i on i.owner.id = ?1 " +
            "and b.start < current_timestamp() " +
            "and b.end > current_timestamp() ")
    Page<Booking> findByOwnerCurrent(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b join b.item i on i.owner.id = ?1 " +
            "and b.start > current_timestamp()")
    Page<Booking> findByOwnerFuture(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b join b.item i on i.owner.id = ?1 " +
            "and b.status = 'WAITING' ")
    Page<Booking> findByOwnerWaiting(Long ownerId, Pageable pageable);

    @Query(value = "select b from Booking b join b.item i on i.owner.id = ?1 " +
            "and b.status = 'REJECTED' ")
    Page<Booking> findByOwnerRejected(Long ownerId, Pageable pageable);

    Optional<Booking> findFirstByItem_IdAndStartIsAfter(Long itemId, LocalDateTime start);

    Optional<Booking> findFirstByItem_IdAndEndIsBefore(Long itemId, LocalDateTime end);

}