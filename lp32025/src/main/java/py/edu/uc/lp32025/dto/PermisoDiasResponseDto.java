package py.edu.uc.lp32025.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para solicitudes de vacaciones o permisos.
 * Retorna información sobre la solicitud realizada.
 */
public class PermisoDiasResponseDto {

    private Long empleadoId;
    private String nombreEmpleado;
    private String tipoSolicitud; // "VACACIONES" o "PERMISO"
    private int diasSolicitados;
    private int diasVacacionesActuales;
    private int diasPermisoActuales;
    private int totalDiasSolicitados;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;
    private boolean exitoso;
    private String mensaje;
    private LocalDateTime timestamp;

    // Constructor vacío
    public PermisoDiasResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor completo
    public PermisoDiasResponseDto(
            Long empleadoId,
            String nombreEmpleado,
            String tipoSolicitud,
            int diasSolicitados,
            int diasVacacionesActuales,
            int diasPermisoActuales,
            int totalDiasSolicitados,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String motivo,
            boolean exitoso,
            String mensaje
    ) {
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.tipoSolicitud = tipoSolicitud;
        this.diasSolicitados = diasSolicitados;
        this.diasVacacionesActuales = diasVacacionesActuales;
        this.diasPermisoActuales = diasPermisoActuales;
        this.totalDiasSolicitados = totalDiasSolicitados;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Long empleadoId) { this.empleadoId = empleadoId; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(String tipoSolicitud) { this.tipoSolicitud = tipoSolicitud; }

    public int getDiasSolicitados() { return diasSolicitados; }
    public void setDiasSolicitados(int diasSolicitados) { this.diasSolicitados = diasSolicitados; }

    public int getDiasVacacionesActuales() { return diasVacacionesActuales; }
    public void setDiasVacacionesActuales(int diasVacacionesActuales) { this.diasVacacionesActuales = diasVacacionesActuales; }

    public int getDiasPermisoActuales() { return diasPermisoActuales; }
    public void setDiasPermisoActuales(int diasPermisoActuales) { this.diasPermisoActuales = diasPermisoActuales; }

    public int getTotalDiasSolicitados() { return totalDiasSolicitados; }
    public void setTotalDiasSolicitados(int totalDiasSolicitados) { this.totalDiasSolicitados = totalDiasSolicitados; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}