package ru.yandex.practicum.ShareIt.BookingTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Slf4j
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void whenFindByOwner_thenReturnLisOfOwnedBookings() {

        User user = new User();
        user.setEmail("user@user.com");
        user.setName("user");

        User user1 = new User();
        user1.setEmail("user1@user.com");
        user1.setName("user1");

        em.getEntityManager().persist(user);
        em.getEntityManager().persist(user1);

        user.setId(1L);

        Item item = new Item();
        item.setOwner(user);
        item.setDescription("item");
        item.setName("item");
        item.setAvailable(true);

        Item item1 = new Item();
        item1.setOwner(user1);
        item1.setDescription("item1");
        item1.setName("item1");
        item1.setAvailable(true);

        em.getEntityManager().persist(item);
        em.getEntityManager().persist(item1);

        item.setId(1L);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        Booking booking1 = new Booking();
        booking1.setItem(item1);
        booking1.setStatus(Status.WAITING);
        booking1.setBooker(user);
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(LocalDateTime.now());

        em.getEntityManager().persist(booking);
        em.getEntityManager().persist(booking1);

        booking.setId(1L);

        List<Booking> list = bookingRepository.findByOwner(1L,
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "end")))
                .getContent();

        log.debug("booking expected: {}",booking);
        log.debug("booking actual: {}",list);
        Assertions.assertEquals(List.of(booking),list);

    }

}
