package py.edu.uc.lp32025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.dto.EmpleadoDto;
import py.edu.uc.lp32025.dto.ReporteEmpleadoDto;
import py.edu.uc.lp32025.service.RemuneracionesService;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/remuneraciones")
public class RemuneracionesController {

    private final RemuneracionesService remuneracionesService;

    @Autowired
    public RemuneracionesController(RemuneracionesService remuneracionesService) {
        this.remuneracionesService = remuneracionesService;
    }

    /**
     * GET /api/remuneraciones
     * Lista todos los empleados de la jerarquía como EmpleadoDto.
     */
    @GetMapping
    public ResponseEntity<List<EmpleadoDto>> listarTodosLosEmpleados() {
        List<EmpleadoDto> empleados = remuneracionesService.listarTodosLosEmpleados();
        return ResponseEntity.ok(empleados);
    }

    // =================================================================
    // NUEVOS ENDPOINTS (para el TP)
    // =================================================================

    /**
     * GET /api/remuneraciones/nomina
     * Calcula la nómina total agrupada por tipo de empleado.
     */
    @GetMapping("/nomina")
    public ResponseEntity<Map<String, BigDecimal>> calcularNominaTotal() {
        Map<String, BigDecimal> nomina = remuneracionesService.calcularNominaTotal();
        return ResponseEntity.ok(nomina);
    }

    /**
     * GET /api/remuneraciones/reporte
     * Genera un reporte completo con información polimórfica, deducciones e impuestos.
     */
    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteEmpleadoDto>> generarReporteCompleto() {
        List<ReporteEmpleadoDto> reporte = remuneracionesService.generarReporteCompleto();
        return ResponseEntity.ok(reporte);
    }
}
