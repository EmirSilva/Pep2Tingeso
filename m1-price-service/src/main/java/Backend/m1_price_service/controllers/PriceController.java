package Backend.m1_price_service.controllers;

import Backend.m1_price_service.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/basePrice")
    public ResponseEntity<Double> getBasePrice(@RequestParam int numLaps, @RequestParam int duration) {
        double price = priceService.calculateBasePrice(numLaps, duration);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    @GetMapping("/birthdayDiscount")
    public ResponseEntity<Double> getPriceWithBirthdayDiscount(
            @RequestParam double price,
            @RequestParam List<LocalDate> birthDates,
            @RequestParam LocalDate reservationDate,
            @RequestParam int groupSize,
            @RequestParam double basePricePerPerson) {
        double discountedPrice = priceService.applyBirthdayDiscount(price, birthDates, reservationDate, groupSize, basePricePerPerson);
        return new ResponseEntity<>(discountedPrice, HttpStatus.OK);
    }

    @GetMapping("/isWeekend")
    public ResponseEntity<Boolean> isWeekend(@RequestParam LocalDateTime reservationDateTime) {
        return new ResponseEntity<>(priceService.isWeekend(reservationDateTime), HttpStatus.OK);
    }

}

