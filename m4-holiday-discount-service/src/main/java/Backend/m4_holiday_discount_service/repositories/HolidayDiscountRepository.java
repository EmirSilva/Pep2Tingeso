package Backend.m4_holiday_discount_service.repositories;

import Backend.m4_holiday_discount_service.entities.HolidayDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayDiscountRepository extends JpaRepository<HolidayDiscountEntity, Long> {
    Optional<HolidayDiscountEntity> findByHolidayDate(LocalDate holidayDate);
}
