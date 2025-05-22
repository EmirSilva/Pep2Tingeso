package Backend.m3_cliente_frecuente_service.services;
import org.springframework.stereotype.Service;

@Service
public class VisitDiscountService {
    //metodo que aplica un descuento al precio basado en la frecuencia de visitas mensuales de un cliente
    public double applyVisitDiscount(double price, int monthlyVisits) {
        if (monthlyVisits >= 7) return price * 0.7;
        if (monthlyVisits >= 5) return price * 0.8;
        if (monthlyVisits >= 2) return price * 0.9;
        return price;
    }
}