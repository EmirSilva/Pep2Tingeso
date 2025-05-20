package Backend.m6_rack_service.repositories;

import Backend.m6_rack_service.entities.RackSemanalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RackSemanalRepository extends JpaRepository<RackSemanalEntity, Long> {
    //metodo para buscar un RackSemanalEntity por su fecha y hora
    RackSemanalEntity findByFechaHora(LocalDateTime fechaHora);
}
