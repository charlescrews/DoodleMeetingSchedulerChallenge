package com.doodle.challenge.service;

import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AvailabilityService {

    private final TimeSlotRepository timeSlotRepository;

    public AvailabilityService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public Map<Long, List<TimeSlot>> getAvailability(
            List<Long> userIds,
            ZonedDateTime from,
            ZonedDateTime to) {

        List<TimeSlot> slots = timeSlotRepository.findByUsersAndTimeRange(userIds, from, to);

        return slots.stream()
                .collect(Collectors.groupingBy(slot -> slot.getUser().getId()));
    }
}