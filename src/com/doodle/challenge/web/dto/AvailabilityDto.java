package com.doodle.challenge.web.dto;

import java.util.List;
import java.util.Map;

public class AvailabilityDto {

    private Map<Long, List<TimeSlotDto>> userSlots;

    public AvailabilityDto() {}

    public AvailabilityDto(Map<Long, List<TimeSlotDto>> userSlots) {
        this.userSlots = userSlots;
    }

    public Map<Long, List<TimeSlotDto>> getUserSlots() {
        return userSlots;
    }

    public void setUserSlots(Map<Long, List<TimeSlotDto>> userSlots) {
        this.userSlots = userSlots;
    }
}