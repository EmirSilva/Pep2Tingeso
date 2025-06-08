package Backend.m5_reservation_service.repositories;

import Backend.m5_reservation_service.entities.KartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KartRepository extends JpaRepository<KartEntity, Long> {
    //metodo para buscar un KartEntity por su codigo
    KartEntity findByCode(String code);
    //metodo para buscar todos los KartEntity cuyo atributo "isAvailable" es verdadero
    List<KartEntity> findByIsAvailableTrue();
}