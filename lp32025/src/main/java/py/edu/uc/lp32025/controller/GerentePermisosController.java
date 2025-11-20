package py.edu.uc.lp32025.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.Gerente;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.dto.PermisoDiasRequestDto;
import py.edu.uc.lp32025.dto.PermisoDiasResponseDto;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.repository.PersonaRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/gerentes")
@Slf4j
public class GerentePermisosController extends BaseController {

    public GerentePermisosController(PersonaRepository repo) {
        super(repo);
    }

    /**
     * POST /gerentes/{id}/vacaciones
     * Solicita vacaciones para un gerente.
     * Los gerentes pueden solicitar hasta 30 días consecutivos.
     *
     * Request body:
     * {
     *   "fechaInicio": "2025-02-10",
     *   "fechaFin": "2025-02-25",
     *   "motivo": "Descanso anual"
     * }
     */
    @PostMapping("/{id}/vacaciones")
    public ResponseEntity<PermisoDiasResponseDto> solicitarVacaciones(
            @PathVariable Long id,
            @Valid @RequestBody PermisoDiasRequestDto request
    ) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof Gerente g)) {
                throw new IllegalArgumentException("El empleado no es un gerente.");
            }

            LocalDate inicio = request.getFechaInicio();
            LocalDate fin = request.getFechaFin();
            long dias = ChronoUnit.DAYS.between(inicio, fin);

            // Llamar al método que lanza PermisoDenegadoException si es necesario
            g.solicitarVacaciones(inicio, fin);

            // Persistir cambios
            personaRepository.save(g);

            // Construir respuesta exitosa
            PermisoDiasResponseDto response = new PermisoDiasResponseDto(
                    g.getId(),
                    g.getNombre() + " " + g.getApellido(),
                    "VACACIONES_GERENTE",
                    (int) dias,
                    g.getDiasVacacionesAnuales(),
                    g.getDiasPermisoAnuales(),
                    g.getTotalDiasSolicitados(),
                    inicio,
                    fin,
                    request.getMotivo(),
                    true,
                    "Vacaciones de gerente registradas correctamente."
            );

            log.info("✓ Vacaciones de gerente aprobadas para ID {}: {} días", id, dias);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (PermisoDenegadoException ex) {
            log.warn("✗ Vacaciones de gerente rechazadas para ID {}: {}", id, ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje(ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("✗ Gerente no encontrado: {}", id);
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
     * POST /gerentes/{id}/permisos
     * Solicita permiso para un gerente.
     * Los gerentes pueden solicitar hasta 10 días de permiso.
     *
     * Request body:
     * {
     *   "fechaInicio": "2025-02-10",
     *   "fechaFin": "2025-02-12",
     *   "motivo": "Asunto personal importante"
     * }
     */
    @PostMapping("/{id}/permisos")
    public ResponseEntity<PermisoDiasResponseDto> solicitarPermiso(
            @PathVariable Long id,
            @Valid @RequestBody PermisoDiasRequestDto request
    ) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof Gerente g)) {
                throw new IllegalArgumentException("El empleado no es un gerente.");
            }

            LocalDate inicio = request.getFechaInicio();
            LocalDate fin = request.getFechaFin();
            String motivo = request.getMotivo();
            long dias = ChronoUnit.DAYS.between(inicio, fin);

            // Llamar al método que lanza PermisoDenegadoException si es necesario
            g.solicitarPermiso(motivo, inicio, fin);

            // Persistir cambios
            personaRepository.save(g);

            // Construir respuesta exitosa
            PermisoDiasResponseDto response = new PermisoDiasResponseDto(
                    g.getId(),
                    g.getNombre() + " " + g.getApellido(),
                    "PERMISO_GERENTE",
                    (int) dias,
                    g.getDiasVacacionesAnuales(),
                    g.getDiasPermisoAnuales(),
                    g.getTotalDiasSolicitados(),
                    inicio,
                    fin,
                    motivo,
                    true,
                    "Permiso de gerente registrado correctamente."
            );

            log.info("✓ Permiso de gerente aprobado para ID {}: {} días (Motivo: {})", id, dias, motivo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (PermisoDenegadoException ex) {
            log.warn("✗ Permiso de gerente rechazado para ID {}: {}", id, ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje(ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("✗ Gerente no encontrado: {}", id);
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
     * GET /gerentes/{id}/dias-disponibles
     * Obtiene el estado de días disponibles para un gerente.
     */
    @GetMapping("/{id}/dias-disponibles")
    public ResponseEntity<PermisoDiasResponseDto> getDiasDisponibles(@PathVariable Long id) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof Gerente g)) {
                throw new IllegalArgumentException("El empleado no es un gerente.");
            }

            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setEmpleadoId(g.getId());
            response.setNombreEmpleado(g.getNombre() + " " + g.getApellido());
            response.setDiasVacacionesActuales(g.getDiasVacacionesAnuales());
            response.setDiasPermisoActuales(g.getDiasPermisoAnuales());
            response.setTotalDiasSolicitados(g.getTotalDiasSolicitados());
            response.setExitoso(true);
            response.setMensaje("Información de días disponibles obtenida correctamente.");

            return ResponseEntity.ok(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("✗ Gerente no encontrado: {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("✗ Error al obtener días disponibles: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}