package Backend.m2_group_discount_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_discount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int minGroupSize;
    private int maxGroupSize;
    private int discountPercentage;
}

