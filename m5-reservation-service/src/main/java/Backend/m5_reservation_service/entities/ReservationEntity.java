package Backend.m5_reservation_service.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "reservation_user",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> usuarios;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserKartAssignmentEntity> userKartAssignments;

    private LocalDate reservationDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime reservationTime;
    private int numLaps;
    private int duration;
    private int groupSize;
    private boolean isBirthday;
    private double totalPrice;

    public void setIsBirthday(boolean isBirthday) {
        this.isBirthday = isBirthday;
    }
}
