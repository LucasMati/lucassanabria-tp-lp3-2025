package py.edu.uc.lp32025.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.interfaces.Permisionable;
import py.edu.uc.lp32025.dto.PermisoDiasRequestDto;
import py.edu.uc.lp32025.dto.PermisoDiasResponseDto;
import py.edu.uc.lp32025.exception.DiasInsuficientesException;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.repository.PersonaRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controlador REST para gestionar permisos y vacaciones de empleados.
 * Usa POLIMORFISMO para trabajar con cualquier tipo de empleado que implemente Permisionable.
 */
@RestController
@RequestMapping("/empleados")
@Slf4j
public class EmpleadoPermisosController extends BaseController {

    public EmpleadoPermisosController(PersonaRepository repo) {
        super(repo);
    }

    /**
     * POST /empleados/{id}/vacaciones
     * Solicita vacaciones para cualquier empleado permisionable.
     *
     * ✅ POLIMORFISMO: Funciona para EmpleadoTiempoCompleto, EmpleadoPorHoras, Contratista y Gerente
     */
    @PostMapping("/{id}/vacaciones")
    public ResponseEntity<PermisoDiasResponseDto> solicitarVacaciones(
            @PathVariable Long id,
            @Valid @RequestBody PermisoDiasRequestDto request
    ) {
        try {
            Persona p = getPersonaOrThrow(id);

            // ✅ POLIMORFISMO VERDADERO: Verifica si implementa Permisionable
            if (!(p instanceof Permisionable emp)) {
                throw new IllegalArgumentException(
                        "El empleado con ID " + id + " no puede solicitar vacaciones. " +
                                "Tipo: " + p.getClass().getSimpleName()
                );
            }

            LocalDate inicio = request.getFechaInicio();
            LocalDate fin = request.getFechaFin();
            long dias = ChronoUnit.DAYS.between(inicio, fin);

            // Llamar al método polimórfico
            emp.solicitarVacaciones(inicio, fin);

            // Persistir cambios
            personaRepository.save(p);

            // Construir respuesta exitosa
            PermisoDiasResponseDto response = new PermisoDiasResponseDto(
                    p.getId(),
                    p.getNombre() + " " + p.getApellido(),
                    "VACACIONES",
                    (int) dias,
                    p.getDiasVacacionesAnuales(),
                    p.getDiasPermisoAnuales(),
                    p.getTotalDiasSolicitados(),
                    inicio,
                    fin,
                    request.getMotivo(),
                    true,
                    "Vacaciones registradas correctamente para " + p.getClass().getSimpleName()
            );

            log.info("✅ Vacaciones aprobadas para {} (ID {}): {} días",
                    p.getClass().getSimpleName(), id, dias);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (PermisoDenegadoException ex) {
            log.warn("❌ Vacaciones rechazadas para empleado ID {}: {}", id, ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje(ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("❌ Empleado no encontrado: {}", id);
            throw ex;

        } catch (Exception ex) {
            log.error("❌ Error inesperado: {}", ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje("Error interno: " + ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * POST /empleados/{id}/permisos
     * Solicita permiso para cualquier empleado permisionable.
     *
     * ✅ POLIMORFISMO: Funciona para EmpleadoTiempoCompleto, EmpleadoPorHoras, Contratista y Gerente
     */
    @PostMapping("/{id}/permisos")
    public ResponseEntity<PermisoDiasResponseDto> solicitarPermiso(
            @PathVariable Long id,
            @Valid @RequestBody PermisoDiasRequestDto request
    ) {
        try {
            Persona p = getPersonaOrThrow(id);

            // ✅ POLIMORFISMO VERDADERO: Verifica si implementa Permisionable
            if (!(p instanceof Permisionable emp)) {
                throw new IllegalArgumentException(
                        "El empleado con ID " + id + " no puede solicitar permisos. " +
                                "Tipo: " + p.getClass().getSimpleName()
                );
            }

            LocalDate inicio = request.getFechaInicio();
            LocalDate fin = request.getFechaFin();
            String motivo = request.getMotivo();
            long dias = ChronoUnit.DAYS.between(inicio, fin);

            // Llamar al método polimórfico
            emp.solicitarPermiso(motivo, inicio, fin);

            // Persistir cambios
            personaRepository.save(p);

            // Construir respuesta exitosa
            PermisoDiasResponseDto response = new PermisoDiasResponseDto(
                    p.getId(),
                    p.getNombre() + " " + p.getApellido(),
                    "PERMISO",
                    (int) dias,
                    p.getDiasVacacionesAnuales(),
                    p.getDiasPermisoAnuales(),
                    p.getTotalDiasSolicitados(),
                    inicio,
                    fin,
                    motivo,
                    true,
                    "Permiso registrado correctamente para " + p.getClass().getSimpleName()
            );

            log.info("✅ Permiso aprobado para {} (ID {}): {} días (Motivo: {})",
                    p.getClass().getSimpleName(), id, dias, motivo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DiasInsuficientesException ex) {
            log.warn("❌ Permiso rechazado para empleado ID {}: {}", id, ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje(ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("❌ Empleado no encontrado: {}", id);
            throw ex;

        } catch (Exception ex) {
            log.error("❌ Error inesperado: {}", ex.getMessage());
            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setExitoso(false);
            response.setMensaje("Error interno: " + ex.getMessage());
            response.setEmpleadoId(id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /empleados/{id}/dias-disponibles
     * Obtiene el estado de días disponibles para cualquier empleado.
     */
    @GetMapping("/{id}/dias-disponibles")
    public ResponseEntity<PermisoDiasResponseDto> getDiasDisponibles(@PathVariable Long id) {
        try {
            Persona p = getPersonaOrThrow(id);

            if (!(p instanceof Permisionable)) {
                throw new IllegalArgumentException(
                        "El empleado con ID " + id + " no tiene días asignables. " +
                                "Tipo: " + p.getClass().getSimpleName()
                );
            }

            PermisoDiasResponseDto response = new PermisoDiasResponseDto();
            response.setEmpleadoId(p.getId());
            response.setNombreEmpleado(p.getNombre() + " " + p.getApellido());
            response.setDiasVacacionesActuales(p.getDiasVacacionesAnuales());
            response.setDiasPermisoActuales(p.getDiasPermisoAnuales());
            response.setTotalDiasSolicitados(p.getTotalDiasSolicitados());
            response.setExitoso(true);
            response.setMensaje("Información de días disponibles para " + p.getClass().getSimpleName());

            return ResponseEntity.ok(response);

        } catch (EmpleadoNoEncontradoException ex) {
            log.error("❌ Empleado no encontrado: {}", id);
            throw ex;
        } catch (Exception ex) {
            log.error("❌ Error al obtener días disponibles: {}", ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}