package py.edu.uc.lp32025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.EmpleadoTiempoCompleto;
import py.edu.uc.lp32025.domain.Persona; // <-- Importar la clase base Persona para el batch
import py.edu.uc.lp32025.service.EmpleadoTiempoCompletoService;
import py.edu.uc.lp32025.dto.EmpleadoTiempoCompletoImpuestoDTO;
import py.edu.uc.lp32025.dto.BatchEmpleadosRequest;
import py.edu.uc.lp32025.dto.BatchResponseDto;
import jakarta.validation.Valid; // Asegúrate de importar esto

import java.util.List;

@RestController
@RequestMapping("/api/empleados-tiempo-completo") // Ruta base del recurso
public class EmpleadoTiempoCompletoController {

    private final EmpleadoTiempoCompletoService empleadoService;

    @Autowired
    public EmpleadoTiempoCompletoController(EmpleadoTiempoCompletoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // CREATE (POST)
    @PostMapping
    public ResponseEntity<EmpleadoTiempoCompleto> createEmpleado(@Valid @RequestBody EmpleadoTiempoCompleto empleado) {
        return ResponseEntity.status(201).body(empleadoService.save(empleado));
    }

    // READ ALL (GET)
    @GetMapping
    public ResponseEntity<List<EmpleadoTiempoCompleto>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    // READ BY ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoTiempoCompleto> getEmpleadoById(@PathVariable Long id) {
        return empleadoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE (PUT) - CORREGIDO
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoTiempoCompleto> updateEmpleado(@PathVariable Long id, @Valid @RequestBody EmpleadoTiempoCompleto empleadoDetails) {
        return empleadoService.findById(id)
                .map(existingEmpleado -> {

                    // 1. ACTUALIZAR CAMPOS DE LA CLASE BASE (Persona)
                    existingEmpleado.setNombre(empleadoDetails.getNombre());
                    existingEmpleado.setApellido(empleadoDetails.getApellido());
                    existingEmpleado.setFechaNacimiento(empleadoDetails.getFechaNacimiento());
                    existingEmpleado.setNumeroDocumento(empleadoDetails.getNumeroDocumento()); // ¡Crucial!

                    // 2. ACTUALIZAR CAMPOS ESPECÍFICOS (EmpleadoTiempoCompleto)
                    existingEmpleado.setSalarioMensual(empleadoDetails.getSalarioMensual());
                    existingEmpleado.setDepartamento(empleadoDetails.getDepartamento());

                    // 3. Guardar y retornar
                    EmpleadoTiempoCompleto actualizado = empleadoService.save(existingEmpleado);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        if (empleadoService.findById(id).isPresent()) {
            empleadoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // =================================================================
    // ENDPOINT PARA CÁLCULO DE IMPUESTOS (EXISTENTE)
    // =================================================================

    /**
     * GET /api/empleados-tiempo-completo/{id}/impuesto
     */
    @GetMapping("/{id}/impuesto")
    public ResponseEntity<EmpleadoTiempoCompletoImpuestoDTO> getImpuestoDetalle(@PathVariable Long id) {
        return empleadoService.calcularImpuestosDetalle(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =================================================================
    // NUEVO SERVICIO REST PARA PERSISTENCIA EN BATCH (4.1)
    // =================================================================

    /**
     * POST /api/empleados-tiempo-completo/batch
     * Persiste una lista de objetos Persona (polimórfica) en lotes de 100.
     */
    @PostMapping("/batch")
    public ResponseEntity<?> guardarEmpleadosEnBatch(@RequestBody List<Persona> empleados) {
        try {
            // Llama al método del servicio para procesar el batch
            List<Persona> empleadosGuardados = empleadoService.guardarEmpleadosEnBatch(empleados);
            return ResponseEntity.status(201).body(empleadosGuardados);
        } catch (IllegalArgumentException e) {
            // Devuelve 400 Bad Request si la validación polimórfica falla
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Devuelve 500 Internal Server Error para otros fallos de persistencia
            return ResponseEntity.internalServerError().body("Error al guardar empleados en batch: " + e.getMessage());
        }
    }
    @PostMapping("/batch/detallado")
    public ResponseEntity<BatchResponseDto> guardarEnBatchDetallado(@RequestBody BatchEmpleadosRequest request) {
        BatchResponseDto resultado = empleadoService.guardarEmpleadosEnBatch(request);
        return ResponseEntity.ok(resultado);
    }
}