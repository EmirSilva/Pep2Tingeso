package Backend.m6_rack_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    private Long id;
    private List<UserEntity> usuarios;
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
