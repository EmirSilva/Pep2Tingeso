package Backend.m3_cliente_frecuente_service.repositories;

import Backend.m3_cliente_frecuente_service.entities.VisitDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface VisitDiscountRepository extends JpaRepository<VisitDiscountEntity, Long> {
    @Query("SELECT v FROM VisitDiscountEntity v WHERE :monthlyVisits >= v.minMonthlyVisits ORDER BY v.minMonthlyVisits DESC")
    List<VisitDiscountEntity> findApplicableDiscountRules(@Param("monthlyVisits") int monthlyVisits);
}