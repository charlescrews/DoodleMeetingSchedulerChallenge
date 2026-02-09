package com.doodle.challenge.repository;

import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TimeSlotRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "Test User");
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void findByUserAndTimeRange_ReturnsOverlappingSlots() {
        ZonedDateTime base = ZonedDateTime.now().plusDays(1);

        TimeSlot slot1 = new TimeSlot(user, base, base.plusHours(1));
        TimeSlot slot2 = new TimeSlot(user, base.plusHours(2), base.plusHours(3));
        TimeSlot slot3 = new TimeSlot(user, base.plusHours(5), base.plusHours(6));

        entityManager.persist(slot1);
        entityManager.persist(slot2);
        entityManager.persist(slot3);
        entityManager.flush();

        List<TimeSlot> result = timeSlotRepository.findByUserAndTimeRange(
                user.getId(),
                base.minusMinutes(30),
                base.plusHours(3).plusMinutes(30)
        );

        assertEquals(2, result.size());
        assertTrue(result.contains(slot1));
        assertTrue(result.contains(slot2));
        assertFalse(result.contains(slot3));
    }

    @Test
    void findByUserStatusAndTimeRange_FiltersByStatus() {
        ZonedDateTime base = ZonedDateTime.now().plusDays(1);

        TimeSlot freeSlot = new TimeSlot(user, base, base.plusHours(1));
        TimeSlot busySlot = new TimeSlot(user, base.plusHours(2), base.plusHours(3));
        busySlot.setStatus(SlotStatus.BUSY);

        entityManager.persist(freeSlot);
        entityManager.persist(busySlot);
        entityManager.flush();

        List<TimeSlot> freeResults = timeSlotRepository.findByUserStatusAndTimeRange(
                user.getId(),
                SlotStatus.FREE,
                base.minusHours(1),
                base.plusHours(4)
        );

        assertEquals(1, freeResults.size());
        assertEquals(freeSlot.getId(), freeResults.get(0).getId());
    }
}