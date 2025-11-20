package py.edu.uc.lp32025.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.domain.EmpleadoPermisionable;
import py.edu.uc.lp32025.dto.PermisoDiasRequestDto;
import py.edu.uc.lp32025.dto.PermisoDiasResponseDto;
import py.edu.uc.lp32025.exception.DiasInsuficientesException;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.repository.PersonaRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/empleados")
@Slf4j
public class EmpleadoPermisosController extends BaseController {

    public EmpleadoPermisosController(PersonaRepository repo) {
        super(repo);
    }

    /**
     * POST /empleados/{id}/vacaciones
     * Solicita vacaciones para un empleado permisionable.
     *
     * Request body:
     * {
     *   "fechaInicio": "2025-02-10",
     *   "fechaFin": "2025-02-15",
     *   "motivo": "Descanso personal"
     * }
     */
    @PostMapping("/{id}/vacaciones")
    public ResponseEntity<PermisoDiasResponseDto> solicitarVacaciones(
            @PathVariable Long id,
            @Valid @RequestBody PermisoDiasRequestDto request
    ) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof EmpleadoPermisionable emp)) {
                throw new IllegalArgumentException("El empleado no es permisionable.");
            }

            LocalDate inicio = request.getFechaInicio();
            LocalDate fin = request.getFechaFin();
            long dias = ChronoUnit.DAYS.between(inicio, fin);

            // Llamar al método que lanza DiasInsuficientesException si es necesario
            emp.solicitarVacaciones(inicio, fin);

            // Persistir cambios
            personaRepository.save(emp);

            // Construir respuesta exitosa
            PermisoDiasResponseDto response = new PermisoDiasResponseDto(
                    emp.getId(),
                    emp.getNombre() + " " + emp.getApellido(),
                    "VACACIONES",
                    (int) dias,
                    emp.getDiasVacacionesAnuales(),
                    emp.getDiasPermisoAnuales(),
                    emp.getTotalDiasSolicitados(),
                    inicio,
                    fin,
                    request.getMotivo(),
                    true,
                    "Vacaciones registradas correctamente."
            );

            log.info("✓ Vacaciones aprobadas para empleado ID {}: {} días", id, dias);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DiasInsuficientesException ex) {
            log.warn("✗ Vacaciones rechazadas para empleado ID {}: {}", id, ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje(ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("✗ Empleado no encontrado: {}", id);
            throw ex;

        } catch (Exception ex) {
            log.error("✗ Error inesperado: {}", ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje("Error interno: " + ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * POST /empleados/{id}/permisos
     * Solicita permiso para un empleado permisionable.
     *
     * Request body:
     * {
     *   "fechaInicio": "2025-02-10",
     *   "fechaFin": "2025-02-12",
     *   "motivo": "Cita médica"
     * }
     */
    @PostMapping("/{id}/permisos")
    public ResponseEntity<PermisoDiasResponseDto> solicitarPermiso(
            @PathVariable Long id,
            @Valid @RequestBody PermisoDiasRequestDto request
    ) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof EmpleadoPermisionable emp)) {
                throw new IllegalArgumentException("El empleado no es permisionable.");
            }

            LocalDate inicio = request.getFechaInicio();
            LocalDate fin = request.getFechaFin();
            String motivo = request.getMotivo();
            long dias = ChronoUnit.DAYS.between(inicio, fin);

            // Llamar al método que lanza DiasInsuficientesException si es necesario
            emp.solicitarPermiso(motivo, inicio, fin);

            // Persistir cambios
            personaRepository.save(emp);

            // Construir respuesta exitosa
            PermisoDiasResponseDto response = new PermisoDiasResponseDto(
                    emp.getId(),
                    emp.getNombre() + " " + emp.getApellido(),
                    "PERMISO",
                    (int) dias,
                    emp.getDiasVacacionesAnuales(),
                    emp.getDiasPermisoAnuales(),
                    emp.getTotalDiasSolicitados(),
                    inicio,
                    fin,
                    motivo,
                    true,
                    "Permiso registrado correctamente."
            );

            log.info("✓ Permiso aprobado para empleado ID {}: {} días (Motivo: {})", id, dias, motivo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DiasInsuficientesException ex) {
            log.warn("✗ Permiso rechazado para empleado ID {}: {}", id, ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje(ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("✗ Empleado no encontrado: {}", id);
            throw ex;

        } catch (Exception ex) {
            log.error("✗ Error inesperado: {}", ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje("Error interno: " + ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /empleados/{id}/dias-disponibles
     * Obtiene el estado de días disponibles para un empleado.
     */
    @GetMapping("/{id}/dias-disponibles")
    public ResponseEntity<PermisoDiasResponseDto> getDiasDisponibles(@PathVariable Long id) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof EmpleadoPermisionable emp)) {
                throw new IllegalArgumentException("El empleado no es permisionable.");
            }

            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setEmpleadoId(emp.getId());
            response.setNombreEmpleado(emp.getNombre() + " " + emp.getApellido());
            response.setDiasVacacionesActuales(emp.getDiasVacacionesAnuales());
            response.setDiasPermisoActuales(emp.getDiasPermisoAnuales());
            response.setTotalDiasSolicitados(emp.getTotalDiasSolicitados());
            response.setExitoso(true);
            response.setMensaje("Información de días disponibles obtenida correctamente.");

            return ResponseEntity.ok(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("✗ Empleado no encontrado: {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("✗ Error al obtener días disponibles: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}