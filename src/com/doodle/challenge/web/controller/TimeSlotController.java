package com.doodle.challenge.web.controller;

import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import com.doodle.challenge.service.TimeSlotService;
import com.doodle.challenge.web.dto.TimeSlotDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Time Slots", description = "Time slot management endpoints")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping("/users/{userId}/slots")
    @Operation(summary = "Create a new time slot for a user")
    public ResponseEntity<TimeSlotDto> createTimeSlot(
            @PathVariable Long userId,
            @Valid @RequestBody TimeSlotDto request) {

        TimeSlot slot = timeSlotService.createTimeSlot(
                userId, request.getStart(), request.getEnd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(slot));
    }

    @GetMapping("/users/{userId}/slots")
    @Operation(summary = "Get time slots for a user")
    public ResponseEntity<List<TimeSlotDto>> getTimeSlots(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to,
            @RequestParam(required = false) SlotStatus status) {

        List<TimeSlot> slots;
        if (status != null) {
            slots = timeSlotService.getTimeSlotsByUserAndStatus(userId, status, from, to);
        } else {
            slots = timeSlotService.getTimeSlotsByUser(userId, from, to);
        }

        List<TimeSlotDto> response = slots.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/slots/{slotId}")
    @Operation(summary = "Update a time slot")
    public ResponseEntity<TimeSlotDto> updateTimeSlot(
            @PathVariable Long slotId,
            @Valid @RequestBody TimeSlotDto request) {

        TimeSlot slot = timeSlotService.updateTimeSlot(
                slotId, request.getStart(), request.getEnd()
        );
        return ResponseEntity.ok(toDto(slot));
    }

    @DeleteMapping("/slots/{slotId}")
    @Operation(summary = "Delete a time slot")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long slotId) {
        timeSlotService.deleteTimeSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/slots/{slotId}/status")
    @Operation(summary = "Update time slot status")
    public ResponseEntity<TimeSlotDto> updateStatus(
            @PathVariable Long slotId,
            @RequestBody SlotStatus status) {

        TimeSlot slot = timeSlotService.updateStatus(slotId, status);
        return ResponseEntity.ok(toDto(slot));
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