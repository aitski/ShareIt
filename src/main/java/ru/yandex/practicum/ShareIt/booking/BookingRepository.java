package ru.yandex.practicum.ShareIt.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, Status status, Sort sort);

    @Query(value = "select * from bookings b " +
            "left join items i on b.ITEM_ID = i.ITEM_ID " +
            "where i.owner = ? " +
            "order by b.end_date desc",
            nativeQuery = true)
    List<Booking> findByOwner(Long ownerId);

    @Query(value = "select * from bookings b " +
            "left join items i on b.ITEM_ID = i.ITEM_ID " +
            "where i.owner = ? " +
            "and b.end_date < current_timestamp() " +
            "order by b.end_date desc",
            nativeQuery = true)
    List<Booking> findByOwnerPast(Long ownerId);

    @Query(value = "select * from bookings b " +
            "left join items i on b.ITEM_ID = i.ITEM_ID " +
            "where i.owner = ? " +
            "and b.start_date < current_timestamp() " +
            "and b.end_date > current_timestamp() " +
            "order by b.end_date desc",
            nativeQuery = true)
    List<Booking> findByOwnerCurrent(Long ownerId);

    @Query(value = "select * from bookings b " +
            "left join items i on b.ITEM_ID = i.ITEM_ID " +
            "where i.owner = ? " +
            "and b.START_DATE > current_timestamp() " +
            "order by b.end_date desc",
            nativeQuery = true)
    List<Booking> findByOwnerFuture(Long ownerId);

    @Query(value = "select * from bookings b " +
            "left join items i on b.ITEM_ID = i.ITEM_ID " +
            "where i.owner = ? " +
            "and b.status = 'WAITING' " +
            "order by b.end_date desc",
            nativeQuery = true)
    List<Booking> findByOwnerWaiting(Long ownerId);

    @Query(value = "select * from bookings b " +
            "left join items i on b.ITEM_ID = i.ITEM_ID " +
            "where i.owner = ? " +
            "and b.status = 'REJECTED' " +
            "order by b.end_date desc",
            nativeQuery = true)
    List<Booking> findByOwnerRejected(Long ownerId);

    Optional<Booking> findFirstByItem_IdAndStartIsAfter(Long itemId, LocalDateTime start);

    Optional<Booking> findFirstByItem_IdAndEndIsBefore(Long itemId, LocalDateTime end);

}