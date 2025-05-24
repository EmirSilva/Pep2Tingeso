package Backend.m2_group_discount_service.repositories;


import Backend.m2_group_discount_service.entities.GroupDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface GroupDiscountRepository extends JpaRepository<GroupDiscountEntity, Long> {
    @Query("SELECT r FROM GroupDiscountEntity r WHERE :groupSize >= r.minGroupSize AND :groupSize <= r.maxGroupSize ORDER BY r.minGroupSize DESC")
    List<GroupDiscountEntity> findApplicableDiscountRules(@org.springframework.data.repository.query.Param("groupSize") int groupSize); // Usamos @Param para vincular el parámetro
}
