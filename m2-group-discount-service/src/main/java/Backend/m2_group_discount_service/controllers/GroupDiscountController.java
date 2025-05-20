package Backend.m2_group_discount_service.controllers;

import Backend.m2_group_discount_service.services.GroupDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/group-discount")
public class GroupDiscountController {

    @Autowired
    private GroupDiscountService groupDiscountService;

    @GetMapping("/apply")
    public ResponseEntity<Double> applyGroupDiscount(
            @RequestParam double basePrice,
            @RequestParam int groupSize) {
        double discountedPrice = groupDiscountService.applyGroupDiscount(basePrice, groupSize);
        return new ResponseEntity<>(discountedPrice, HttpStatus.OK);
    }
}