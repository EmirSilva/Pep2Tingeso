package Backend.m5_reservation_service.controllers;

import Backend.m5_reservation_service.entities.KartEntity;
import Backend.m5_reservation_service.entities.ReservationEntity;
import Backend.m5_reservation_service.services.ReservationService;
import Backend.m5_reservation_service.entities.UserKartAssignmentEntity;
import Backend.m5_reservation_service.entities.UserEntity;
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
            // Nota: Es mejor usar Object en el segundo Map ya que los valores pueden ser de diferentes tipos, aunque aquí todos son Strings.
            List<Map<String, String>> usuariosPayload = (List<Map<String, String>>) payload.get("usuarios");

            if ((numLaps == null && duration == null) || reservationDateStr == null || reservationTimeStr == null || usuariosPayload == null) {
                // Validación mejorada: al menos numLaps O duration deben tener un valor.
                return ResponseEntity.badRequest().body(null);
            }

            // Parsear la fecha y hora
            LocalDate reservationDate = LocalDate.parse(reservationDateStr);
            // Asegurarse de que el formato de tiempo es correcto antes de parsear
            // Asumimos que frontend siempre envía HH:mm:ss, así que no es necesario substring
            LocalTime reservationTime = LocalTime.parse(reservationTimeStr);

            ReservationEntity reservation = new ReservationEntity();
            // Solo establecer si numLaps o duration son diferentes de null (y opcionalmente > 0)
            if (numLaps != null) {
                reservation.setNumLaps(numLaps);
            }
            if (duration != null) {
                reservation.setDuration(duration);
            }
            reservation.setReservationDate(reservationDate);
            reservation.setReservationTime(reservationTime);
            reservation.setGroupSize(usuariosPayload.size());

            List<UserEntity> usuarios = usuariosPayload.stream()
                    .map(userMap -> {
                        UserEntity user = new UserEntity();
                        user.setName(userMap.get("name")); // <--- ¡¡¡AÑADE ESTA LÍNEA!!!
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
                UserEntity firstUser = reservationService.findByEmail(firstUserEmail);
                if (firstUser != null) {
                    monthlyVisits = firstUser.getMonthlyVisits();
                }
            }

            double price = reservationService.calculateBestTotalPrice(reservation, monthlyVisits);
            return ResponseEntity.ok(price);

        } catch (Exception e) {
            // Logear la excepción completa para depuración
            e.printStackTrace(); // <--- ¡Importante para depuración en Kubernetes!
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

    //metodo get para listar todos los karts que estan actualmente disponibles
    //retorna una lista de objetos KartEntity
    @GetMapping("/available")
    public ResponseEntity<List<KartEntity>> getAvailableKarts() {
        List<KartEntity> availableKarts = reservationService.getAvailableKarts();
        return new ResponseEntity<>(availableKarts, HttpStatus.OK);
    }
}
