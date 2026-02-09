package com.doodle.challenge.web.controller;

import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.service.AvailabilityService;
import com.doodle.challenge.web.dto.AvailabilityDto;
import com.doodle.challenge.web.dto.TimeSlotDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/availability")
@Tag(name = "Availability", description = "Aggregated availability endpoints")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    @Operation(summary = "Get aggregated availability for multiple users")
    public ResponseEntity<AvailabilityDto> getAvailability(
            @RequestParam List<Long> userIds,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {

        Map<Long, List<TimeSlot>> availability = availabilityService.getAvailability(userIds, from, to);

        Map<Long, List<TimeSlotDto>> dtoMap = availability.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(this::toDto)
                                .collect(Collectors.toList())
                ));

        return ResponseEntity.ok(new AvailabilityDto(dtoMap));
    }

    private TimeSlotDto toDto(TimeSlot slot) {
        Long meetingId = (slot.getMeeting() != null) ? slot.getMeeting().getId() : null;
        return new TimeSlotDto(
                slot.getId(),
                slot.getUser().getId(),
                slot.getStart(),
                slot.getEnd(),
                slot.getStatus(),
                meetingId
        );
    }
}