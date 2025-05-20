package Backend.m5_reservation_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_kart_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKartAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "kart_id")
    private KartEntity kart;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @JsonBackReference
    private ReservationEntity reservation;
}