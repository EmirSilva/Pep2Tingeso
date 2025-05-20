package Backend.m4_holiday_discount_service.controllers;

import Backend.m4_holiday_discount_service.services.HolidayDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/holiday-discount")
public class HolidayDiscountController {
    @Autowired
    private HolidayDiscountService holidayDiscountService;

    @GetMapping("/is-holiday")
    public ResponseEntity<Boolean> isHoliday(@RequestParam LocalDate date) {
        return new ResponseEntity<>(holidayDiscountService.isHoliday(date), HttpStatus.OK);
    }

    @GetMapping("/apply")
    public ResponseEntity<Double> applyHolidayDiscount(@RequestParam double price, @RequestParam LocalDate reservationDate) {
        double discountedPrice = holidayDiscountService.applyHolidayDiscount(price, reservationDate);
        return new ResponseEntity<>(discountedPrice, HttpStatus.OK);
    }
}