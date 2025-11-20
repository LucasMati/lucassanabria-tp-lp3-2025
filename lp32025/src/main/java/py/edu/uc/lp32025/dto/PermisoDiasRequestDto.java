package py.edu.uc.lp32025.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO para solicitar vacaciones o permisos.
 * Utilizado en POST /empleados/{id}/vacaciones y POST /empleados/{id}/permisos
 */
public class PermisoDiasRequestDto {

    @NotNull(message = "La fecha de inicio no puede ser nula.")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula.")
    private LocalDate fechaFin;

    @NotBlank(message = "El motivo no puede estar vacío.")
    private String motivo;

    // Constructor vacío
    public PermisoDiasRequestDto() {}

    // Constructor con parámetros
    public PermisoDiasRequestDto(LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    // Getters y Setters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}