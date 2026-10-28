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

        validateSchedule(trip, date, startTime, endTime);

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
        validateSchedule(schedule.getTrip(), date, startTime, endTime);
        schedule.update(title, description, location, date, startTime, endTime);
        return schedule;
    }

    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = getScheduleById(id);
        scheduleRepository.delete(schedule);
    }

    private void validateSchedule(Trip trip, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (date == null) {
            throw new IllegalArgumentException("Schedule date must not be null");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time must not be null");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (date.isBefore(trip.getStartDate()) || date.isAfter(trip.getEndDate())) {
            throw new IllegalArgumentException("Schedule date must be within the trip period");
        }
    }
}