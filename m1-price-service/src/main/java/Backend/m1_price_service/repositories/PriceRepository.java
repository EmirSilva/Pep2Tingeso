package Backend.m1_price_service.repositories;

import Backend.m1_price_service.entities.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
    //metodo para encontrar una regla de precio que se ajuste a las vueltas y duración
    Optional<PriceEntity> findFirstByMaxLapsGreaterThanEqualAndMaxDurationGreaterThanEqualOrderByMaxLapsAscMaxDurationAsc(int numLaps, int duration);
}
