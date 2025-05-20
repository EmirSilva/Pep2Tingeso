package Backend.m3_cliente_frecuente_service.controllers;

import Backend.m3_cliente_frecuente_service.services.VisitDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visit-discount")
public class VisitDiscountController {
    @Autowired
    private VisitDiscountService visitDiscountService;

    @GetMapping("/apply")
    public ResponseEntity<Double> applyFrequentCustomerDiscount(
            @RequestParam double price,
            @RequestParam int monthlyVisits) {
        double discountedPrice = visitDiscountService.applyVisitDiscount(price, monthlyVisits);
        return new ResponseEntity<>(discountedPrice, HttpStatus.OK);
    }
}
