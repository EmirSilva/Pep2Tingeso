package Backend.m7_report_service.controllers;

import Backend.m7_report_service.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@CrossOrigin
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/ingresos-por-mes-tipo")
    public ResponseEntity<List<Map<String, Object>>> getIngresosPorMesTipo(
            @RequestParam int yearInicio,
            @RequestParam int monthInicio,
            @RequestParam int yearFin,
            @RequestParam int monthFin) {
        List<Map<String, Object>> reporte = reportService.generarReporteIngresosPorMesTipoTarifa(yearInicio, monthInicio, yearFin, monthFin);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/ingresos-personas")
    public ResponseEntity<List<Map<String, Object>>> getReporteIngresosPorMesNumeroPersonas(
            @RequestParam int yearInicio,
            @RequestParam int monthInicio,
            @RequestParam int yearFin,
            @RequestParam int monthFin) {
        List<Map<String, Object>> reporte = reportService.generarReporteIngresosPorMesNumeroPersonas(yearInicio, monthInicio, yearFin, monthFin);
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }
}
