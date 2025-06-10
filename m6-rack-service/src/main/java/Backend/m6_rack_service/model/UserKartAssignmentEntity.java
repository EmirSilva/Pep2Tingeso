package Backend.m6_rack_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;  //para evitar la referencia circular
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKartAssignmentEntity {
    private Long id;

    private UserEntity user;
    private KartEntity kart;

    @JsonBackReference //evitar recursion infinita
    private ReservationEntity reservation;
}
