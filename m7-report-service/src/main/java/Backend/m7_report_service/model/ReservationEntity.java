package Backend.m7_report_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ReservationEntity {
    private Long id;
    private List<UserEntity> usuarios;
    private LocalDate reservationDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime reservationTime;
    private int numLaps;
    private int duration;
    private int groupSize;
    private double totalPrice;
}
