package com.doodle.challenge.web.controller;

import com.doodle.challenge.domain.Meeting;
import com.doodle.challenge.service.MeetingService;
import com.doodle.challenge.web.dto.MeetingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Meetings", description = "Meeting management endpoints")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/slots/{slotId}/meeting")
    @Operation(summary = "Convert a time slot into a meeting")
    public ResponseEntity<MeetingDto> createMeeting(
            @PathVariable Long slotId,
            @Valid @RequestBody MeetingDto request) {

        Meeting meeting = meetingService.createMeeting(
                slotId,
                request.getTitle(),
                request.getDescription(),
                request.getParticipantIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(meeting));
    }

    @GetMapping("/meetings/{id}")
    @Operation(summary = "Get meeting by ID")
    public ResponseEntity<MeetingDto> getMeeting(@PathVariable Long id) {
        Meeting meeting = meetingService.getMeetingById(id);
        return ResponseEntity.ok(toDto(meeting));
    }

    private MeetingDto toDto(Meeting meeting) {
        return new MeetingDto(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getDescription(),
                meeting.getOrganizer().getId(),
                meeting.getSlot().getId(),
                meeting.getParticipants().stream()
                        .map(p -> p.getUser().getId())
                        .collect(Collectors.toList())
        );
    }
}