package Backend.m1_price_service.services;

import Backend.m1_price_service.entities.PriceEntity;
import Backend.m1_price_service.repositories.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    //metodo que calcula el precio base de una reserva basado en el numero de vueltas y la duracion
    //si los valores se pasan los rangos definidos, retorna el precio maximo por defecto
    public double calculateBasePrice(int numLaps, int duration) {
        Optional<PriceEntity> rule = priceRepository.findFirstByMaxLapsGreaterThanEqualAndMaxDurationGreaterThanEqualOrderByMaxLapsAscMaxDurationAsc(numLaps, duration);
        if (rule.isPresent()) {
            return rule.get().getBasePrice();
        }
        return 25000;
    }

    //metodo que aplica un descuento de cumpleaños al precio de la reserva si alguno de los usuarios cumple años en la fecha de la reserva
    //se aplica el descuento dependiendo el tamaño del grupo
    public double applyBirthdayDiscount(double price, List<LocalDate> birthDates, LocalDate reservationDate, int groupSize, double basePricePerPerson) {
        int birthdayCount = 0;
        double totalDiscount = 0;

        if (birthDates != null && !birthDates.isEmpty()) {
            for (LocalDate birthDate : birthDates) {
                if (birthDate != null &&
                        birthDate.getMonth() == reservationDate.getMonth() &&
                        birthDate.getDayOfMonth() == reservationDate.getDayOfMonth()) {
                    birthdayCount++;
                    if ((groupSize >= 3 && groupSize <= 5 && birthdayCount <= 1) || (groupSize >= 6 && birthdayCount <= 2)) {
                        totalDiscount += basePricePerPerson * 0.5;
                    }
                }
            }
        }
        return price - totalDiscount;
    }

    //metodo que verifica si una fecha y hora de reserva corresponden a un fin de semana
    public boolean isWeekend(LocalDateTime reservationDateTime) {
        DayOfWeek day = reservationDateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}