package Backend.m6_rack_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KartEntity {
    private Long id;
    private String code;
    private boolean isAvailable;
}