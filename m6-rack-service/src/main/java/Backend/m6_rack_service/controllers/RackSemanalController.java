package Backend.m6_rack_service.controllers;


import Backend.m6_rack_service.entities.RackSemanalEntity;
import Backend.m6_rack_service.services.RackSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rack")
public class RackSemanalController {
    @Autowired
    private RackSemanalService rackService;

    @GetMapping("/")
    public List<RackSemanalEntity> getAll() {
        return rackService.getAllRacks();
    }

    @PostMapping("/")
    public RackSemanalEntity save(@RequestBody RackSemanalEntity rack) {
        return rackService.saveRack(rack);
    }

    @PostMapping("/ocupar")
    public ResponseEntity<Void> marcarComoOcupado(@PathVariable Long reservationId) {
        rackService.marcarComoOcupado(reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/liberar/{reservationId}")
    public ResponseEntity<Void> marcarComoLibre(@PathVariable Long reservationId) {
        rackService.marcarComoLibre(reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<LocalDate, Map<LocalTime, Object>>> getWeeklyAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<LocalDate, Map<LocalTime, Object>> availability = rackService.getAvailabilityByDateRange(startDate, endDate);
        return ResponseEntity.ok(availability);
    }
}


