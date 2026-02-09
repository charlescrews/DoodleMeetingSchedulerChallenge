package com.doodle.challenge.service;

import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.domain.User;
import com.doodle.challenge.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final UserService userService;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, UserService userService) {
        this.timeSlotRepository = timeSlotRepository;
        this.userService = userService;
    }

    public TimeSlot createTimeSlot(Long userId, ZonedDateTime start, ZonedDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        User user = userService.getUserById(userId);
        TimeSlot slot = new TimeSlot(user, start, end);
        return timeSlotRepository.save(slot);
    }

    @Transactional(readOnly = true)
    public TimeSlot getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlotsByUser(Long userId, ZonedDateTime from, ZonedDateTime to) {
        return timeSlotRepository.findByUserAndTimeRange(userId, from, to);
    }

    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlotsByUserAndStatus(
            Long userId, SlotStatus status, ZonedDateTime from, ZonedDateTime to) {
        return timeSlotRepository.findByUserStatusAndTimeRange(userId, status, from, to);
    }

    public TimeSlot updateTimeSlot(Long slotId, ZonedDateTime start, ZonedDateTime end) {
        TimeSlot slot = getTimeSlotById(slotId);

        if (slot.getStatus() == SlotStatus.BUSY) {
            throw new IllegalStateException("Cannot modify a busy time slot");
        }

        if (start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        slot.setStart(start);
        slot.setEnd(end);
        return timeSlotRepository.save(slot);
    }

    public void deleteTimeSlot(Long slotId) {
        TimeSlot slot = getTimeSlotById(slotId);

        if (slot.getStatus() == SlotStatus.BUSY) {
            throw new IllegalStateException("Cannot delete a busy time slot");
        }

        timeSlotRepository.delete(slot);
    }

    public TimeSlot updateStatus(Long slotId, SlotStatus status) {
        TimeSlot slot = getTimeSlotById(slotId);

        if (status == SlotStatus.BUSY && slot.getMeeting() == null) {
            throw new IllegalStateException("Cannot mark slot as BUSY without a meeting");
        }

        slot.setStatus(status);
        return timeSlotRepository.save(slot);
    }
}