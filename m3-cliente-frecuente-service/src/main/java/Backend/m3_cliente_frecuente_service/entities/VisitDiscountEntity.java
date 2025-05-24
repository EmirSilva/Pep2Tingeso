package Backend.m3_cliente_frecuente_service.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "visit_discount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitDiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int minMonthlyVisits;
    private double discountMultiplier;
}
