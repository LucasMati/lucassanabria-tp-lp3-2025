package py.edu.uc.lp32025.exception;

import java.time.LocalDate;

public class PermisoDenegadoException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String motivo;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public PermisoDenegadoException(String mensaje, String motivo, LocalDate fechaInicio, LocalDate fechaFin) {
        super(mensaje);
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getMotivo() { return motivo; }

    public LocalDate getFechaInicio() { return fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }

    @Override
    public String toString() {
        return "PermisoDenegadoException{" +
                "mensaje='" + getMessage() + '\'' +
                ", motivo='" + motivo + '\'' +
                ", rango=" + fechaInicio + " → " + fechaFin +
                '}';
    }
}
