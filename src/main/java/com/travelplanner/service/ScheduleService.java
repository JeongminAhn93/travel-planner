package com.travelplanner.service;

import com.travelplanner.entity.Schedule;
import com.travelplanner.entity.Trip;
import com.travelplanner.repository.ScheduleRepository;
import com.travelplanner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TripRepository tripRepository;

    @Transactional
    public Schedule createSchedule(Long tripId, String title, String description, String location,
                                   LocalDate date, LocalTime startTime, LocalTime endTime) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));

        Schedule schedule = Schedule.builder()
                .trip(trip)
                .title(title)
                .description(description)
                .location(location)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByTripId(Long tripId) {
        return scheduleRepository.findByTripId(tripId);
    }

    @Transactional(readOnly = true)
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + id));
    }

    @Transactional
    public Schedule updateSchedule(Long id, String title, String description, String location,
                                   LocalDate date, LocalTime startTime, LocalTime endTime) {
        Schedule schedule = getScheduleById(id);
        schedule.update(title, description, location, date, startTime, endTime);
        return schedule;
    }

    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = getScheduleById(id);
        scheduleRepository.delete(schedule);
    }
}