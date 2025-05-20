package Backend.m7_report_service.services;

import Backend.m7_report_service.model.ReservationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private RestTemplate restTemplate;

    private final String reservationServiceUrl = "http://reservation-service/reservations/";

    private List<ReservationEntity> getAllReservations() {
        ResponseEntity<List<ReservationEntity>> response = restTemplate.exchange(
                reservationServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationEntity>>() {}
        );
        return response.getBody();
    }

    public List<Map<String, Object>> generarReporteIngresosPorMesTipoTarifa(int yearInicio, int monthInicio, int yearFin, int monthFin) {
        List<ReservationEntity> allReservations = getAllReservations();
        if (allReservations == null) {
            return List.of(Map.of("error", "No se pudieron obtener las reservas."));
        }

        List<Integer> tiposTarifa = Arrays.asList(10, 15, 20);
        List<Month> mesesIngles = Arrays.asList(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
        List<String> mesesEspanol = Arrays.asList("ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE");

        return tiposTarifa.stream().map(tipoTarifa -> {
            Map<String, Object> reportePorTipo = new HashMap<>();
            String tipoTarifaLabel;
            switch (tipoTarifa) {
                case 10:
                    tipoTarifaLabel = "10 vueltas o max 10 min";
                    break;
                case 15:
                    tipoTarifaLabel = "15 vueltas o max 15 min";
                    break;
                case 20:
                    tipoTarifaLabel = "20 vueltas o max 20 min";
                    break;
                default:
                    tipoTarifaLabel = "otro (" + tipoTarifa + " vueltas)";
                    break;
            }
            reportePorTipo.put("tipoTarifa", tipoTarifaLabel);

            double totalTipo = 0.0;
            for (int i = 0; i < mesesIngles.size(); i++) {
                Month mesIngles = mesesIngles.get(i);
                String mesEspanol = mesesEspanol.get(i);
                double ingresosMes = allReservations.stream()
                        .filter(reserva -> {
                            LocalDate reservaDate = reserva.getReservationDate();
                            return reserva.getNumLaps() == tipoTarifa &&
                                    reservaDate != null &&
                                    reservaDate.getYear() >= yearInicio && reservaDate.getYear() <= yearFin &&
                                    reservaDate.getMonth() == mesIngles;
                        })
                        .mapToDouble(ReservationEntity::getTotalPrice)
                        .sum();
                reportePorTipo.put(mesEspanol, ingresosMes);
                totalTipo += ingresosMes;
            }
            reportePorTipo.put("TOTAL", totalTipo);
            return reportePorTipo;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> generarReporteIngresosPorMesNumeroPersonas(int yearInicio, int monthInicio, int yearFin, int monthFin) {
        List<ReservationEntity> allReservations = getAllReservations();
        if (allReservations == null) {
            return List.of(Map.of("error", "No se pudieron obtener las reservas."));
        }
        List<Month> mesesIngles = Arrays.asList(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
        List<String> mesesEspanol = Arrays.asList("ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE");
        List<String> rangosPersonas = Arrays.asList("1-2 personas", "3-5 personas", "6-10 personas", "11-15 personas");

        return rangosPersonas.stream().map(rango -> {
            Map<String, Object> reportePorRango = new HashMap<>();
            reportePorRango.put("rangoPersonas", rango);
            double totalRango = 0.0;
            String[] limites = rango.split("-");
            int minPersonas = Integer.parseInt(limites[0]);
            int maxPersonas = Integer.parseInt(limites[1].split(" ")[0]);

            for (int i = 0; i < mesesIngles.size(); i++) {
                Month mesIngles = mesesIngles.get(i);
                String mesEspanol = mesesEspanol.get(i);
                double ingresosMes = allReservations.stream()
                        .filter(reserva -> {
                            LocalDate reservaDate = reserva.getReservationDate();
                            return reserva.getGroupSize() >= minPersonas && reserva.getGroupSize() <= maxPersonas &&
                                    reservaDate != null &&
                                    reservaDate.getYear() >= yearInicio && reservaDate.getYear() <= yearFin &&
                                    reservaDate.getMonth() == mesIngles;
                        })
                        .mapToDouble(ReservationEntity::getTotalPrice)
                        .sum();
                reportePorRango.put(mesEspanol, ingresosMes);
                totalRango += ingresosMes;
            }
            reportePorRango.put("TOTAL", totalRango);
            return reportePorRango;
        }).collect(Collectors.toList());
    }
}
