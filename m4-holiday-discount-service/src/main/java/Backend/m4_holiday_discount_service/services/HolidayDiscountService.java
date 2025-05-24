package Backend.m4_holiday_discount_service.services;

import Backend.m4_holiday_discount_service.entities.HolidayDiscountEntity;
import Backend.m4_holiday_discount_service.repositories.HolidayDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class HolidayDiscountService {

    @Autowired
    private HolidayDiscountRepository holidayRepository;

    private final double defaultHolidayDiscount = 0.15;

    public boolean isHoliday(LocalDate date) {
        Optional<HolidayDiscountEntity> holiday = holidayRepository.findByHolidayDate(date);
        return holiday.isPresent();
    }

    public double applyHolidayDiscount(double price, LocalDate reservationDate) {
        Optional<HolidayDiscountEntity> holiday = holidayRepository.findByHolidayDate(reservationDate);
        if (holiday.isPresent()) {
            return price * (1 - defaultHolidayDiscount); // Usando el descuento por defecto
        }
        return price;
    }
}