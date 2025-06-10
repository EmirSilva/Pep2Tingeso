package Backend.m6_rack_service.controllers;

import Backend.m6_rack_service.entities.RackSemanalEntity;
import Backend.m6_rack_service.model.ReservationEntity;
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
    public ResponseEntity<String> marcarComoOcupado(@RequestBody ReservationEntity reservation) {
        try {
            rackService.marcarComoOcupado(reservation);
            return new ResponseEntity<>("Espacio en rack marcado como ocupado exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al marcar espacio en rack como ocupado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/libre/{reservationId}")
    public ResponseEntity<String> marcarComoLibre(@PathVariable Long reservationId) {
        try {
            rackService.marcarComoLibre(reservationId);
            return new ResponseEntity<>("Espacio en rack marcado como libre exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al marcar espacio en rack como libre: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<LocalDate, Map<LocalTime, Object>>> getWeeklyAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<LocalDate, Map<LocalTime, Object>> availability = rackService.getAvailabilityByDateRange(startDate, endDate);
        return ResponseEntity.ok(availability);
    }
}


