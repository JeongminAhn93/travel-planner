package com.travelplanner.controller;

import com.travelplanner.entity.Trip;
import com.travelplanner.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequest request) {
        Trip trip = tripService.createTrip(
                request.getTitle(),
                request.getDestination(),
                request.getStartDate(),
                request.getEndDate(),
                request.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(trip);
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getTrips(@RequestParam Long userId) {
        List<Trip> trips = tripService.getTripsByUserId(userId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @RequestBody TripRequest request) {
        Trip trip = tripService.updateTrip(
                id,
                request.getTitle(),
                request.getDestination(),
                request.getStartDate(),
                request.getEndDate()
        );
        return ResponseEntity.ok(trip);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    @lombok.Getter
    @lombok.Setter
    public static class TripRequest {
        private String title;
        private String destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long userId;
    }
}