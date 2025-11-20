package py.edu.uc.lp32025.domain;

import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.exception.DiasInsuficientesException;
import py.edu.uc.lp32025.interfaces.Permisionable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Slf4j
public class EmpleadoPermisionable extends Persona implements Permisionable {

    private static final int LIMITE_DIAS_ANUALES = 20;

    @Override
    public void solicitarVacaciones(LocalDate inicio, LocalDate fin) throws DiasInsuficientesException {

        long diasSolicitados = ChronoUnit.DAYS.between(inicio, fin);
        int totalActual = getDiasVacacionesAnuales() + getDiasPermisoAnuales();
        int diasDisponibles = LIMITE_DIAS_ANUALES - totalActual;

        // Validar que no supere el límite de 20 días anuales
        if (totalActual + diasSolicitados > LIMITE_DIAS_ANUALES) {
            throw new DiasInsuficientesException(
                    "Vacaciones rechazadas: supera el límite anual de " + LIMITE_DIAS_ANUALES + " días. " +
                            "Ya tiene " + totalActual + " días solicitados y quiere agregar " + diasSolicitados + " más.",
                    (int) diasSolicitados,
                    diasDisponibles,
                    inicio,
                    fin
            );
        }

        // Registrar días
        setDiasVacacionesAnuales(getDiasVacacionesAnuales() + (int) diasSolicitados);

        log.info("✓ Vacaciones aprobadas para {}: {} días (Total anual: {}/{})",
                getNombre(), diasSolicitados, getTotalDiasSolicitados(), LIMITE_DIAS_ANUALES);
    }

    @Override
    public void solicitarPermiso(String motivo, LocalDate inicio, LocalDate fin) throws DiasInsuficientesException {

        long diasSolicitados = ChronoUnit.DAYS.between(inicio, fin);
        int totalActual = getDiasVacacionesAnuales() + getDiasPermisoAnuales();
        int diasDisponibles = LIMITE_DIAS_ANUALES - totalActual;

        // Validar que no supere el límite de 20 días anuales
        if (totalActual + diasSolicitados > LIMITE_DIAS_ANUALES) {
            throw new DiasInsuficientesException(
                    "Permiso rechazado: supera el límite anual de " + LIMITE_DIAS_ANUALES + " días. " +
                            "Ya tiene " + totalActual + " días solicitados y quiere agregar " + diasSolicitados + " más.",
                    (int) diasSolicitados,
                    diasDisponibles,
                    inicio,
                    fin
            );
        }

        // Registrar días
        setDiasPermisoAnuales(getDiasPermisoAnuales() + (int) diasSolicitados);

        log.info("✓ Permiso aprobado para {}: {} días (Motivo: {}, Total anual: {}/{})",
                getNombre(), diasSolicitados, motivo, getTotalDiasSolicitados(), LIMITE_DIAS_ANUALES);
    }

    @Override
    public BigDecimal calcularSalario() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularDeducciones() {
        return BigDecimal.ZERO;
    }

    @Override
    public boolean validarDatosEspecificos() {
        return true;
    }

    @Override
    public String obtenerInformacionCompleta() {
        return super.obtenerInformacionCompleta() +
                ", Días solicitados: " + getTotalDiasSolicitados() + "/" + LIMITE_DIAS_ANUALES;
    }
}