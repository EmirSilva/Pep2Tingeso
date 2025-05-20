package Backend.m6_rack_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rack_semanal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RackSemanalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    //"disponible" o "ocupado"
    private String estado;
    private String reservadoPor;

    private Long reservationId;
    private Long kartId;

    public RackSemanalEntity(LocalDateTime fechaHora, String estado, String reservadoPor) {
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.reservadoPor = reservadoPor;
    }
}

