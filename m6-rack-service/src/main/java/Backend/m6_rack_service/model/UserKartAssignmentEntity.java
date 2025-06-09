package Backend.m6_rack_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Importa si la relación es bidireccional
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Necesario para deserialización de JSON
@AllArgsConstructor // Útil para constructores completos
public class UserKartAssignmentEntity {
    private Long id;

    // Al ser un DTO, no necesitamos @ManyToOne, solo la referencia al objeto
    private UserEntity user;
    private KartEntity kart;

    // Si tu ReservationEntity en m5 usa @JsonManagedReference,
    // esta relación aquí DEBE usar @JsonBackReference para evitar bucles de serialización
    // si el JSON incluye la referencia a la reserva padre.
    // Dado que ReservationEntity en m5 tiene @JsonManagedReference en userKartAssignments,
    // y UserKartAssignmentEntity tiene @JsonBackReference en reservation,
    // es crucial mantener esto en el modelo de m6.
    @JsonBackReference // ¡Importante para evitar recursión infinita de JSON!
    private ReservationEntity reservation;
}
