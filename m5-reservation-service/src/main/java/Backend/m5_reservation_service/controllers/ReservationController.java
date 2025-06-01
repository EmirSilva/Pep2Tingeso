package Backend.m5_reservation_service.controllers;

import Backend.m5_reservation_service.entities.ReservationEntity;
import Backend.m5_reservation_service.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public ResponseEntity<List<ReservationEntity>> getAllReservations() {
        List<ReservationEntity> reservations = reservationService.getReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable Long id) {
        Optional<ReservationEntity> reservation = reservationService.getReservationById(id);
        return reservation.map(res -> new ResponseEntity<>(res, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ReservationEntity> createReservation(@RequestBody ReservationEntity reservation) {
        ReservationEntity savedReservation = reservationService.createReservation(reservation);
        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationEntity> updateReservation(@PathVariable Long id, @RequestBody ReservationEntity reservation) {
        Optional<ReservationEntity> existingReservation = reservationService.getReservationById(id);
        if (existingReservation.isPresent()) {
            reservation.setId(id);
            ReservationEntity updatedReservation = reservationService.saveReservation(reservation);
            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        boolean deleted = reservationService.deleteReservation(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/calculate-total-price")
    public ResponseEntity<Double> calculateTotalPrice(@RequestBody Map<String, Object> payload) {
        try {
            Integer numLaps = (Integer) payload.get("numLaps");
            Integer duration = (Integer) payload.get("duration");
            String reservationDateStr = (String) payload.get("reservationDate");
            String reservationTimeStr = (String) payload.get("reservationTime");
            List<Map<String, String>> usuariosPayload = (List<Map<String, String>>) payload.get("usuarios");

            if (numLaps == null || duration == null || reservationDateStr == null || reservationTimeStr == null || usuariosPayload == null) {
                return ResponseEntity.badRequest().body(null);
            }

            LocalDate reservationDate = LocalDate.parse(reservationDateStr);
            LocalTime reservationTime = LocalTime.parse(reservationTimeStr.substring(0, 5)); // Eliminar los segundos

            ReservationEntity reservation = new ReservationEntity();
            reservation.setNumLaps(numLaps);
            reservation.setDuration(duration);
            reservation.setReservationDate(reservationDate);
            reservation.setReservationTime(reservationTime);
            reservation.setGroupSize(usuariosPayload.size());

            List<UserEntity> usuarios = usuariosPayload.stream()
                    .map(userMap -> {
                        UserEntity user = new UserEntity();
                        user.setDateOfBirth(LocalDate.parse(userMap.get("dateOfBirth")));
                        user.setEmail(userMap.get("email"));
                        return user;
                    })
                    .collect(Collectors.toList());
            reservation.setUsuarios(usuarios);

            int monthlyVisits = 0;
            if (!usuariosPayload.isEmpty()) {
                String firstUserEmail = usuariosPayload.get(0).get("email");
                //usar el userservice para obtener las visitas mensuales
                UserEntity firstUser = userService.findByEmail(firstUserEmail);
                if (firstUser != null) {
                    monthlyVisits = firstUser.getMonthlyVisits();
                }
            }

            double price = reservationService.calculateBestTotalPrice(reservation, monthlyVisits);
            return ResponseEntity.ok(price);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //metodo get para obtener los karts asignados a una reserva especifica por su id
    //utiliza el ReservationService para obtener la lista de UserKartAssignmentEntity
    @GetMapping("/{id}/karts-asignados")
    public ResponseEntity<List<UserKartAssignmentEntity>> getAssignedKarts(@PathVariable Long id) {
        List<UserKartAssignmentEntity> assignments = reservationService.getUserKartAssignmentsByReservationId(id);
        return ResponseEntity.ok(assignments);
    }
}