package com.doodle.challenge.service;

import com.doodle.challenge.domain.Meeting;
import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.domain.User;
import com.doodle.challenge.repository.MeetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private TimeSlotService timeSlotService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MeetingService meetingService;

    private User organizer;
    private User participant;
    private TimeSlot freeSlot;

    @BeforeEach
    void setUp() {
        organizer = new User("organizer@example.com", "Organizer");
        organizer.setId(1L);

        participant = new User("participant@example.com", "Participant");
        participant.setId(2L);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = start.plusHours(1);
        freeSlot = new TimeSlot(organizer, start, end);
        freeSlot.setId(1L);
        freeSlot.setStatus(SlotStatus.FREE);
    }

    @Test
    void createMeeting_Success() {
        when(timeSlotService.getTimeSlotById(1L)).thenReturn(freeSlot);
        when(userService.getUserById(2L)).thenReturn(participant);
        when(meetingRepository.save(any(Meeting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Meeting result = meetingService.createMeeting(
                1L, "Team Sync", "Weekly sync", List.of(2L)
        );

        assertNotNull(result);
        assertEquals("Team Sync", result.getTitle());
        assertEquals("Weekly sync", result.getDescription());
        assertEquals(organizer, result.getOrganizer());
        assertEquals(freeSlot, result.getSlot());
        assertEquals(1, result.getParticipants().size());
        assertEquals(SlotStatus.BUSY, freeSlot.getStatus());
        verify(meetingRepository).save(any(Meeting.class));
    }

    @Test
    void createMeeting_OnBusySlot_ThrowsException() {
        freeSlot.setStatus(SlotStatus.BUSY);
        when(timeSlotService.getTimeSlotById(1L)).thenReturn(freeSlot);

        assertThrows(IllegalStateException.class, () -> {
            meetingService.createMeeting(
                    1L, "Team Sync", "Weekly sync", List.of(2L)
            );
        });

        verify(meetingRepository, never()).save(any());
    }
}