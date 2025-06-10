package Backend.m3_cliente_frecuente_service.services;

import Backend.m3_cliente_frecuente_service.entities.VisitDiscountEntity;
import Backend.m3_cliente_frecuente_service.repositories.VisitDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitDiscountService {

    @Autowired
    private VisitDiscountRepository visitDiscountRepository;

    public double applyVisitDiscount(double price, int monthlyVisits) {
        List<VisitDiscountEntity> rules = visitDiscountRepository.findApplicableDiscountRules(monthlyVisits);

        if (!rules.isEmpty()) {
            return price * rules.get(0).getDiscountMultiplier();
        }
        return price;
    }
}