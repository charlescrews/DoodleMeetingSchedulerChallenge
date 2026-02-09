package com.doodle.challenge.web.dto;

import com.doodle.challenge.domain.SlotStatus;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public class TimeSlotDto {

    private Long id;
    private Long userId;

    @NotNull(message = "Start time is required")
    private ZonedDateTime start;

    @NotNull(message = "End time is required")
    private ZonedDateTime end;

    private SlotStatus status;
    private Long meetingId;

    public TimeSlotDto() {}

    public TimeSlotDto(Long id, Long userId, ZonedDateTime start, ZonedDateTime end,
                       SlotStatus status, Long meetingId) {
        this.id = id;
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.status = status;
        this.meetingId = meetingId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public ZonedDateTime getStart() { return start; }
    public void setStart(ZonedDateTime start) { this.start = start; }
    public ZonedDateTime getEnd() { return end; }
    public void setEnd(ZonedDateTime end) { this.end = end; }
    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }
    public Long getMeetingId() { return meetingId; }
    public void setMeetingId(Long meetingId) { this.meetingId = meetingId; }
}
