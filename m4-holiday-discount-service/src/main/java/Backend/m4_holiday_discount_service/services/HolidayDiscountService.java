package Backend.m4_holiday_discount_service.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class HolidayDiscountService {

    // Metodo que determina si una fecha dada es un dia festivo segun una lista
    public boolean isHoliday(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        // lista de dias festivos (mes y dia)
        return (month == 1 && day == 1) ||     // 1 de enero (año nuevo)
                (month == 4 && (day == 18 || day == 19)) ||   // 18 y 19 de abril (pascua)
                (month == 5 && day == 10) ||   // 10 de mayo (dia de la madre)
                (month == 9 && (day == 18 || day == 19)) ||   // 18 y 19 de septiembre (fiestas patrias)
                (month == 11 && day == 1) ||   // 1 de noviembre   (halloween)
                (month == 12 && day == 25);    // 25 de diciembre   (navidad)
    }

    // Metodo que aplica un descuento al precio de la reserva si la fecha es festiva
    public double applyHolidayDiscount(double price, LocalDate reservationDate) {
        if (isHoliday(reservationDate)) {
            double discountPercentage = 0.15;
            return price * (1 - discountPercentage);
        }
        return price;
    }
}
