package com.doodle.challenge.service;

import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.domain.User;
import com.doodle.challenge.repository.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TimeSlotService timeSlotService;

    private User testUser;
    private ZonedDateTime start;
    private ZonedDateTime end;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "Test User");
        testUser.setId(1L);
        start = ZonedDateTime.now().plusDays(1);
        end = start.plusHours(1);
    }

    @Test
    void createTimeSlot_Success() {
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(timeSlotRepository.save(any(TimeSlot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TimeSlot result = timeSlotService.createTimeSlot(1L, start, end);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertEquals(SlotStatus.FREE, result.getStatus());
        verify(timeSlotRepository).save(any(TimeSlot.class));
    }

    @Test
    void createTimeSlot_InvalidTimeRange_ThrowsException() {
        ZonedDateTime invalidEnd = start.minusHours(1);

        assertThrows(IllegalArgumentException.class, () -> {
            timeSlotService.createTimeSlot(1L, start, invalidEnd);
        });

        verify(timeSlotRepository, never()).save(any());
    }

    @Test
    void updateStatus_ToBusy_WithoutMeeting_ThrowsException() {
        TimeSlot slot = new TimeSlot(testUser, start, end);
        slot.setId(1L);

        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(slot));

        assertThrows(IllegalStateException.class, () -> {
            timeSlotService.updateStatus(1L, SlotStatus.BUSY);
        });
    }

    @Test
    void deleteTimeSlot_WhenBusy_ThrowsException() {
        TimeSlot slot = new TimeSlot(testUser, start, end);
        slot.setId(1L);
        slot.setStatus(SlotStatus.BUSY);

        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(slot));

        assertThrows(IllegalStateException.class, () -> {
            timeSlotService.deleteTimeSlot(1L);
        });

        verify(timeSlotRepository, never()).delete(any());
    }
}
