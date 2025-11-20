package py.edu.uc.lp32025.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.repository.PersonaRepository;
import py.edu.uc.lp32025.util.NominaUtils;
import py.edu.uc.lp32025.domain.Persona;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nomina")
@Slf4j
public class NominaReporteController {

    private final PersonaRepository personaRepository;

    public NominaReporteController(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * GET /nomina/reporte
     *
     * Devuelve un reporte en JSON con:
     * - datos básicos del empleado
     * - salario, deducciones, impuestos
     * - días de vacaciones y permisos solicitados
     * - total de días solicitados
     */
    @GetMapping("/reporte")
    public ResponseEntity<String> generarReporteNomina() {
        try {
            List<Persona> personas = personaRepository.findAll();
            String reporte = NominaUtils.generarReporteNomina(personas);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.error("Error al generar reporte de nómina: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /nomina/reporte/filtrado?dias=10
     *
     * Objetivo 2: Devuelve un reporte JSON con empleados que han solicitado
     * más de un número especificado de días.
     *
     * Parámetro:
     * - dias: umbral mínimo de días solicitados (default: 0)
     *
     * Ejemplo: GET /nomina/reporte/filtrado?dias=15
     * Retorna: Empleados con > 15 días solicitados
     */
    @GetMapping("/reporte/filtrado")
    public ResponseEntity<String> generarReporteEmpleadosMasDias(
            @RequestParam(value = "dias", defaultValue = "0") int diasMinimos
    ) {
        try {
            List<Persona> personas = personaRepository.findAll();
            String reporte = NominaUtils.generarReporteEmpleadosConMasDias(personas, diasMinimos);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.error("Error al generar reporte filtrado: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /nomina/total-dias
     *
     * Objetivo 2: Calcula y devuelve el total de días solicitados
     * por TODOS los empleados.
     */
    @GetMapping("/total-dias")
    public ResponseEntity<?> calcularTotalDiasSolicitados() {
        try {
            List<Persona> personas = personaRepository.findAll();
            int totalDias = NominaUtils.calcularTotalDiasSolicitados(personas);

            Map<String, Object> response = new HashMap<>();
            response.put("totalEmpleados", personas.size());
            response.put("totalDiasSolicitados", totalDias);
            response.put("promedioDiasPorEmpleado",
                    personas.isEmpty() ? 0 : (double) totalDias / personas.size()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al calcular total de días: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}