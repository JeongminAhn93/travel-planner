package com.travelplanner.service;

import com.travelplanner.entity.Trip;
import com.travelplanner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    @Transactional
    public Trip createTrip(String title, String destination, LocalDate startDate, LocalDate endDate, Long userId) {
        Trip trip = Trip.builder()
                .title(title)
                .destination(destination)
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .build();
        return tripRepository.save(trip);
    }

    @Transactional(readOnly = true)
    public List<Trip> getTripsByUserId(Long userId) {
        return tripRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + id));
    }

    @Transactional
    public Trip updateTrip(Long id, String title, String destination, LocalDate startDate, LocalDate endDate) {
        Trip trip = getTripById(id);
        trip.update(title, destination, startDate, endDate);
        return trip;
    }

    @Transactional
    public void deleteTrip(Long id) {
        Trip trip = getTripById(id);
        tripRepository.delete(trip);
    }
}