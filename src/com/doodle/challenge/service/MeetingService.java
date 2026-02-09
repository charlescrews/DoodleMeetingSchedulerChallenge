package com.doodle.challenge.service;

import com.doodle.challenge.domain.Meeting;
import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.domain.User;
import com.doodle.challenge.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final TimeSlotService timeSlotService;
    private final UserService userService;

    public MeetingService(
            MeetingRepository meetingRepository,
            TimeSlotService timeSlotService,
            UserService userService) {
        this.meetingRepository = meetingRepository;
        this.timeSlotService = timeSlotService;
        this.userService = userService;
    }

    public Meeting createMeeting(
            Long slotId,
            String title,
            String description,
            List<Long> participantIds) {

        TimeSlot slot = timeSlotService.getTimeSlotById(slotId);

        if (slot.getStatus() == SlotStatus.BUSY) {
            throw new IllegalStateException("Time slot is already busy");
        }

        User organizer = slot.getUser();
        Meeting meeting = new Meeting(title, description, organizer, slot);

        // Add participants
        for (Long participantId : participantIds) {
            User participant = userService.getUserById(participantId);
            meeting.addParticipant(participant);
        }

        // Mark slot as busy
        slot.setStatus(SlotStatus.BUSY);
        slot.setMeeting(meeting);

        return meetingRepository.save(meeting);
    }

    @Transactional(readOnly = true)
    public Meeting getMeetingById(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found: " + id));
    }
}