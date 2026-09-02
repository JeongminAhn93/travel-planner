package com.travelplanner.controller;

import com.travelplanner.entity.Schedule;
import com.travelplanner.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleRequest request) {
        Schedule schedule = scheduleService.createSchedule(
                request.getTripId(),
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam Long tripId) {
        List<Schedule> schedules = scheduleService.getSchedulesByTripId(tripId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        Schedule schedule = scheduleService.updateSchedule(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @lombok.Getter
    @lombok.Setter
    public static class ScheduleRequest {
        private Long tripId;
        private String title;
        private String description;
        private String location;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}