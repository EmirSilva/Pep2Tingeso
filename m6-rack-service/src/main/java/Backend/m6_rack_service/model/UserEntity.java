package Backend.m6_rack_service.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserEntity {
    private Long id;
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private int monthlyVisits;
}