package Backend.m5_reservation_service.repositories;

import Backend.m5_reservation_service.entities.UserKartAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserKartAssignmentRepository extends JpaRepository<UserKartAssignmentEntity, Long> {
    List<UserKartAssignmentEntity> findByReservationId(Long reservationId);
}

