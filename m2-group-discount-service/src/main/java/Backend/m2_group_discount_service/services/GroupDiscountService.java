package Backend.m2_group_discount_service.services;

import Backend.m2_group_discount_service.entities.GroupDiscountEntity;
import Backend.m2_group_discount_service.repositories.GroupDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupDiscountService {

    @Autowired
    private GroupDiscountRepository groupDiscountRepository;

    public double applyGroupDiscount(double basePrice, int groupSize) {
        List<GroupDiscountEntity> rules = groupDiscountRepository.findApplicableDiscountRules(groupSize);

        int discountPercentage = 0;
        if (!rules.isEmpty()) {
            discountPercentage = rules.get(0).getDiscountPercentage();
        }

        //calcular el precio con descuento
        double finalPrice = basePrice * (1 - discountPercentage / 100.0);

        //retorna el precio con descuento
        return finalPrice;
    }
}