package Backend.m6_rack_service.services;

import Backend.m6_rack_service.entities.RackSemanalEntity;
import Backend.m6_rack_service.model.ReservationEntity;
import Backend.m6_rack_service.repositories.RackSemanalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RackSemanalService {

    @Autowired
    private RackSemanalRepository rackRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String reservationServiceUrl = "http://m5-reservation-service/reservations/";

    //metodo para obtener una lista de todos los registros del rack semanal
    public List<RackSemanalEntity> getAllRacks() {
        return rackRepository.findAll();
    }

    //metodo para guardar un nuevo registro en el rack semanal
    public RackSemanalEntity saveRack(RackSemanalEntity rack) {
        return rackRepository.save(rack);
    }

    //metodo para marcar un espacio en el rack semanal como ocupado segun la reserva
    @Transactional
    public void marcarComoOcupado(ReservationEntity reservation) {
        try {
            LocalDateTime fechaHora = reservation.getReservationDate().atTime(reservation.getReservationTime());
            String reservadoPor = null;
            if (reservation.getUsuarios() != null && !reservation.getUsuarios().isEmpty() && reservation.getUsuarios().get(0) != null) {
                reservadoPor = reservation.getUsuarios().get(0).getName();
            }

            RackSemanalEntity rack = rackRepository.findByFechaHora(fechaHora);
            if (rack != null) {
                rack.setEstado("ocupado");
                rack.setReservadoPor(reservadoPor);
                rack.setReservationId(reservation.getId());
                rackRepository.save(rack);
            } else {
                RackSemanalEntity nuevoRack = new RackSemanalEntity(fechaHora, "ocupado", reservadoPor);
                nuevoRack.setReservationId(reservation.getId());
                rackRepository.save(nuevoRack);
            }
        } catch (Exception e) {
            System.err.println("Error al marcar como ocupado en rack semanal: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error interno al marcar rack: " + e.getMessage(), e);
        }
    }

    //metodo para marcar un espacio en el rack semanal como libre segun la reserva
    //se libera el espacio y se elimina el nombre del usuario que hizo la reserva
    public void marcarComoLibre(Long reservationId) {
        try {
            ResponseEntity<ReservationEntity> response = restTemplate.getForEntity(
                    reservationServiceUrl + reservationId,
                    ReservationEntity.class
            );
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ReservationEntity reservation = response.getBody();
                LocalDateTime fechaHora = reservation.getReservationDate().atTime(reservation.getReservationTime());

                RackSemanalEntity rack = rackRepository.findByFechaHora(fechaHora);
                if (rack != null) {
                    rack.setEstado("libre");
                    rack.setReservadoPor(null);
                    rack.setReservationId(null);
                    rack.setKartId(null);
                    rackRepository.save(rack);
                }
            } else {
                System.err.println("No se pudo obtener la reserva con ID: " + reservationId + ". Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al comunicarse con el servicio de reservas: " + e.getMessage());
        }
    }

    private static final List<LocalTime> HOURLY_SLOTS = List.of(
            LocalTime.of(10, 0), LocalTime.of(11, 0), LocalTime.of(12, 0), LocalTime.of(13, 0),
            LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0), LocalTime.of(17, 0),
            LocalTime.of(18, 0), LocalTime.of(19, 0), LocalTime.of(20, 0), LocalTime.of(21, 0),
            LocalTime.of(22, 0)
    );

    //metodo para obtener un mapa de disponibilidad del rack dentro de un rango de fechas especificado
    //se devuelve un mapa con la fecha como clave y otro mapa con la hora como clave
    public Map<LocalDate, Map<LocalTime, Object>> getAvailabilityByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Map<LocalTime, Object>> availabilityMap = new HashMap<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            availabilityMap.put(currentDate, new HashMap<>());
            for (LocalTime startTime : HOURLY_SLOTS) {
                availabilityMap.get(currentDate).put(startTime, Map.of("estado", "disponible"));

                LocalDateTime slotDateTime = currentDate.atTime(startTime);
                RackSemanalEntity rackEntry = rackRepository.findByFechaHora(slotDateTime);

                if (rackEntry != null && rackEntry.getEstado().equals("ocupado")) {
                    Map<String, Object> slotInfo = new HashMap<>();
                    slotInfo.put("estado", "ocupado");
                    if (rackEntry.getReservadoPor() != null) {
                        slotInfo.put("reservadoPor", rackEntry.getReservadoPor());
                    }
                    availabilityMap.get(currentDate).put(startTime, slotInfo);
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        return availabilityMap;
    }
}