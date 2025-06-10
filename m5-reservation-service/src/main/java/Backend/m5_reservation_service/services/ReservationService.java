package Backend.m5_reservation_service.services;

import Backend.m5_reservation_service.entities.KartEntity;
import Backend.m5_reservation_service.entities.ReservationEntity;
import Backend.m5_reservation_service.entities.UserEntity;
import Backend.m5_reservation_service.entities.UserKartAssignmentEntity;
import Backend.m5_reservation_service.repositories.ReservationRepository;
import Backend.m5_reservation_service.repositories.UserKartAssignmentRepository;
import Backend.m5_reservation_service.repositories.UserRepository;
import Backend.m5_reservation_service.repositories.KartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KartRepository kartRepository;

    @Autowired
    private UserKartAssignmentRepository userKartAssignmentRepository;

    @Autowired
    //para comunicarse con otros microservicios
    private RestTemplate restTemplate;

    //url de los otros microservicios
    private final String priceServiceUrl = "http://m1-price-service/prices";
    private final String groupDiscountServiceUrl = "http://m2-group-discount-service/group-discount";
    private final String frequentCustomerDiscountServiceUrl = "http://m3-cliente-frecuente-service/visit-discount";
    private final String holidayDiscountServiceUrl = "http://m4-holiday-discount-service/holiday-discount";
    private final String rackServiceUrl = "http://m6-rack-service/rack";

    //metodo para obtener la lista de asignaciones de usuario-kart para una reserva especifica
    public List<UserKartAssignmentEntity> getUserKartAssignmentsByReservationId(Long reservationId) {
        return userKartAssignmentRepository.findByReservationId(reservationId);
    }

    //metodo que crea una nueva reserva
    @Transactional
    public ReservationEntity createReservation(ReservationEntity reservation) {
        List<UserEntity> usuarios = new ArrayList<>();
        UserEntity reservingUser = null;
        for (int i = 0; i < reservation.getUsuarios().size(); i++) {
            UserEntity user = userRepository.findByEmail(reservation.getUsuarios().get(i).getEmail());
            if (user == null) {
                user = reservation.getUsuarios().get(i);
                userRepository.save(user);
            }
            if (i == 0) {
                reservingUser = user;
                user.setMonthlyVisits(user.getMonthlyVisits() + 1);
                userRepository.save(user);
            }
            usuarios.add(user);
        }
        reservation.setUsuarios(usuarios);
        reservation.setGroupSize(usuarios.size());

        //guardar la reserva para obtener un ID
        ReservationEntity savedReservation = reservationRepository.save(reservation);

        //asignar karts
        List<KartEntity> availableKarts = kartRepository.findByIsAvailableTrue();
        List<UserKartAssignmentEntity> userKartAssignments = new ArrayList<>();
        if (availableKarts.size() < usuarios.size()) {
            throw new RuntimeException("No hay suficientes karts disponibles");
        }
        for (int i = 0; i < usuarios.size(); i++) {
            UserEntity user = usuarios.get(i);
            KartEntity kart = availableKarts.get(i);
            kart.setAvailable(false);
            kartRepository.save(kart);
            UserKartAssignmentEntity assignment = new UserKartAssignmentEntity();
            assignment.setUser(user);
            assignment.setKart(kart);
            assignment.setReservation(savedReservation);
            userKartAssignments.add(assignment);
            userKartAssignmentRepository.save(assignment);
        }
        savedReservation.setUserKartAssignments(userKartAssignments);

        //calcular el precio total llamando a los otros microservicios
        double totalPrice = calculateBestTotalPrice(savedReservation, reservingUser != null ? reservingUser.getMonthlyVisits() : 0);
        savedReservation.setTotalPrice(totalPrice);

        try {
            if (savedReservation.getId() == null) {
                savedReservation = reservationRepository.save(savedReservation);
            }
            ResponseEntity<String> rackResponse = restTemplate.postForEntity(
                    rackServiceUrl + "/ocupar",
                    savedReservation,
                    String.class
            );

            if (rackResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("Espacio en rack marcado exitosamente.");
            } else {
                System.err.println("Error al marcar espacio en rack. Status: " + rackResponse.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al comunicarse para marcar el rack: " + e.getMessage());
            e.printStackTrace();
        }

        //guardar la reserva con el precio total
        return reservationRepository.save(savedReservation);
    }

    //metodo para obtener todas las reservas
    public List<ReservationEntity> getReservations() {
        return reservationRepository.findAll();
    }

    //metodo para obtener una reserva por ID
    public Optional<ReservationEntity> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    //metodo para guardar o actualizar una reserva
    public ReservationEntity saveReservation(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    //metodo para eliminar una reserva
    @Transactional
    public boolean deleteReservation(Long id) {
        Optional<ReservationEntity> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isPresent()) {
            ReservationEntity reservation = reservationOptional.get();
            //liberar los karts asociados
            List<UserKartAssignmentEntity> assignments = userKartAssignmentRepository.findByReservationId(id);
            for (UserKartAssignmentEntity assignment : assignments) {
                KartEntity kart = assignment.getKart();
                kart.setAvailable(true);
                kartRepository.save(kart);
                userKartAssignmentRepository.delete(assignment);
            }
            try {
                restTemplate.delete(rackServiceUrl + "/liberar/{reservationId}", id);
                System.out.println("Espacio en rack liberado exitosamente.");
            } catch (Exception e) {
                System.err.println("Error al comunicarsepara liberar el rack: " + e.getMessage());
                e.printStackTrace();
            }

            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //metodo para calcular el precio total llamando a los otros microservicios
    public double calculateBestTotalPrice(ReservationEntity reservation, int monthlyVisits) {
        //microservicio 1
        Double basePrice = restTemplate.getForObject(
                priceServiceUrl + "/basePrice?numLaps={numLaps}&duration={duration}",
                Double.class,
                reservation.getNumLaps(),
                reservation.getDuration()
        );
        if (basePrice == null) basePrice = 0.0;
        double currentPrice = basePrice * reservation.getGroupSize();

        //microservicio 2
        if (reservation.getGroupSize() >= 3) {
            Double discountedPrice = restTemplate.getForObject(
                    groupDiscountServiceUrl + "/apply?basePrice={basePrice}&groupSize={groupSize}",
                    Double.class,
                    currentPrice,
                    reservation.getGroupSize()
            );
            if (discountedPrice != null) currentPrice = discountedPrice;
        }

        //microservicio 3
        if (monthlyVisits >= 2) {
            Double discountedPrice = restTemplate.getForObject(
                    frequentCustomerDiscountServiceUrl + "/apply?price={price}&monthlyVisits={monthlyVisits}",
                    Double.class,
                    currentPrice,
                    monthlyVisits
            );
            if (discountedPrice != null) currentPrice = discountedPrice;
        }

        //microservicio 4
        LocalDate reservationDate = reservation.getReservationDate();
        Boolean isHoliday = restTemplate.getForObject(
                holidayDiscountServiceUrl + "/is-holiday?date={date}",
                Boolean.class,
                reservationDate
        );
        if (isHoliday != null && isHoliday) {
            Double discountedPrice = restTemplate.getForObject(
                    holidayDiscountServiceUrl + "/apply?price={price}&reservationDate={date}",
                    Double.class,
                    currentPrice,
                    reservationDate
            );
            if (discountedPrice != null) currentPrice = discountedPrice;
        }
        return currentPrice;
    }

    //metodo para buscar un usuario por su correo
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //metodo para buscar todos los karts disponibles
    public List<KartEntity> getAvailableKarts() {
        return kartRepository.findByIsAvailableTrue();
    }

}