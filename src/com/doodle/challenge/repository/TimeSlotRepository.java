package com.doodle.challenge.repository;

import com.doodle.challenge.domain.SlotStatus;
import com.doodle.challenge.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.user.id = :userId " +
            "AND ts.start < :end AND ts.end > :start")
    List<TimeSlot> findByUserAndTimeRange(
            @Param("userId") Long userId,
            @Param("start") ZonedDateTime start,
            @Param("end") ZonedDateTime end
    );

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.user.id IN :userIds " +
            "AND ts.start < :end AND ts.end > :start")
    List<TimeSlot> findByUsersAndTimeRange(
            @Param("userIds") List<Long> userIds,
            @Param("start") ZonedDateTime start,
            @Param("end") ZonedDateTime end
    );

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.user.id = :userId " +
            "AND ts.status = :status AND ts.start < :end AND ts.end > :start")
    List<TimeSlot> findByUserStatusAndTimeRange(
            @Param("userId") Long userId,
            @Param("status") SlotStatus status,
            @Param("start") ZonedDateTime start,
            @Param("end") ZonedDateTime end
    );
}