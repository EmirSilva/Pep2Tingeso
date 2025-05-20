package Backend.m2_group_discount_service.services;

import org.springframework.stereotype.Service;

@Service
public class GroupDiscountService {
    public double applyGroupDiscount(double basePrice, int groupSize) {
        int discountPercentage = 0;

        if (groupSize >= 11) {
            discountPercentage = 30;  //30% de descuento para grupos de 11-15 personas
        } else if (groupSize >= 6) {
            discountPercentage = 20;  //20% de descuento para grupos de 6-10 personas
        } else if (groupSize >= 3) {
            discountPercentage = 10;  //10% de descuento para grupos de 3-5 personas
        } //si es 1-2 personas no hay descuento

        //calcular el precio con descuento
        double finalPrice = basePrice * (1 - discountPercentage / 100.0);

        //retorna el precio con descuento
        return finalPrice;
    }
}