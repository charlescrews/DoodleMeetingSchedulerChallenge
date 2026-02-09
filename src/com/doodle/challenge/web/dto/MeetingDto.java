package com.doodle.challenge.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class MeetingDto {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private Long organizerId;
    private Long slotId;

    @NotEmpty(message = "At least one participant is required")
    private List<Long> participantIds;

    public MeetingDto() {}

    public MeetingDto(Long id, String title, String description, Long organizerId,
                      Long slotId, List<Long> participantIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.organizerId = organizerId;
        this.slotId = slotId;
        this.participantIds = participantIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
    public List<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Long> participantIds) { this.participantIds = participantIds; }
}