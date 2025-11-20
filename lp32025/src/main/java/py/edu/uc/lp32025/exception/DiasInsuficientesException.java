package py.edu.uc.lp32025.exception;

import java.time.LocalDate;

public class DiasInsuficientesException extends PermisoDenegadoException {

    private static final long serialVersionUID = 1L;

    private final int diasSolicitados;
    private final int diasDisponibles;

    public DiasInsuficientesException(
            String mensaje,
            int diasSolicitados,
            int diasDisponibles,
            LocalDate inicio,
            LocalDate fin
    ) {
        super(mensaje,
                "Días insuficientes (solicitados=" + diasSolicitados +
                        ", disponibles=" + diasDisponibles + ")",
                inicio,
                fin
        );
        this.diasSolicitados = diasSolicitados;
        this.diasDisponibles = diasDisponibles;
    }

    public int getDiasSolicitados() { return diasSolicitados; }
    public int getDiasDisponibles() { return diasDisponibles; }
}
