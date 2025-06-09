package Backend.m6_rack_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Si usas relaciones bidireccionales
import com.fasterxml.jackson.annotation.JsonBackReference;   // Si usas relaciones bidireccionales
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor // Necesario para deserialización de JSON
@AllArgsConstructor // Útil para constructores completos
public class ReservationEntity {
    private Long id;
    private List<UserEntity> usuarios;

    // ¡CAMBIO CLAVE! Añadir la lista de asignaciones
    // Si tu UserKartAssignmentEntity tiene una referencia @JsonBackReference a ReservationEntity,
    // entonces esta propiedad en ReservationEntity debería tener @JsonManagedReference
    // para evitar ciclos infinitos durante la serialización/deserialización.
    // Aunque para el modelo en m6, como no es JPA, no es estrictamente una relación JPA,
    // las anotaciones de Jackson son importantes para el JSON.
    private List<UserKartAssignmentEntity> userKartAssignments; // Asegúrate de que esta clase exista en el paquete model

    private LocalDate reservationDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime reservationTime;
    private int numLaps;
    private int duration;
    private int groupSize;
    private boolean isBirthday;
    private double totalPrice;

    // Getter y Setter para isBirthday (aunque Lombok @Data los crea,
    // si tenías un setter específico, asegúrate de que esté o que Lombok lo maneje)
    public void setIsBirthday(boolean isBirthday) {
        this.isBirthday = isBirthday;
    }
}
