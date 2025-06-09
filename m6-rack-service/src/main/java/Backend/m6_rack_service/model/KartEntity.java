package Backend.m6_rack_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Necesario para deserialización de JSON
@AllArgsConstructor // Útil para constructores completos
public class KartEntity {
    private Long id;
    private String code;
    private boolean isAvailable;
}