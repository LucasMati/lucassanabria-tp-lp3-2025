package py.edu.uc.lp32025.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.exception.DiasInsuficientesException;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;
import py.edu.uc.lp32025.interfaces.Permisionable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Slf4j
public class Contratista extends Persona implements Permisionable {

    private static final int LIMITE_DIAS_ANUALES = 20;

    @Column(nullable = false)
    private BigDecimal montoPorProyecto;

    @Column(nullable = false)
    private Integer proyectosCompletados;

    @Column(nullable = false)
    private LocalDate fechaFinContrato;

    // =============================================================
    // IMPLEMENTACIÓN DE PERMISIONABLE
    // =============================================================

    @Override
    public void solicitarVacaciones(LocalDate inicio, LocalDate fin) throws PermisoDenegadoException {
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

        log.info("✅ Vacaciones aprobadas para Contratista {}: {} días (Total anual: {}/{})",
                getNombre(), diasSolicitados, getTotalDiasSolicitados(), LIMITE_DIAS_ANUALES);
    }

    @Override
    public void solicitarPermiso(String motivo, LocalDate inicio, LocalDate fin) throws PermisoDenegadoException {
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

        log.info("✅ Permiso aprobado para Contratista {}: {} días (Motivo: {}, Total anual: {}/{})",
                getNombre(), diasSolicitados, motivo, getTotalDiasSolicitados(), LIMITE_DIAS_ANUALES);
    }

    // =============================================================
    // MÉTODOS SOBRESCRITOS
    // =============================================================

    @Override
    public BigDecimal calcularSalario() {
        if (montoPorProyecto == null || proyectosCompletados == null) return BigDecimal.ZERO;
        return montoPorProyecto.multiply(BigDecimal.valueOf(proyectosCompletados));
    }

    @Override
    public BigDecimal calcularDeducciones() {
        return BigDecimal.ZERO;
    }

    @Override
    public boolean validarDatosEspecificos() {
        return fechaFinContrato != null
                && fechaFinContrato.isAfter(LocalDate.now())
                && proyectosCompletados != null
                && proyectosCompletados >= 0
                && montoPorProyecto != null
                && montoPorProyecto.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean contratoVigente() {
        return fechaFinContrato != null && !fechaFinContrato.isBefore(LocalDate.now());
    }

    @Override
    public String obtenerInformacionCompleta() {
        return super.obtenerInformacionCompleta() +
                ", Fecha fin contrato: " + fechaFinContrato +
                ", Contrato vigente: " + contratoVigente() +
                ", Días solicitados: " + getTotalDiasSolicitados() + "/" + LIMITE_DIAS_ANUALES;
    }

    // =============================================================
    // GETTERS Y SETTERS
    // =============================================================

    public BigDecimal getMontoPorProyecto() {
        return montoPorProyecto;
    }

    public void setMontoPorProyecto(BigDecimal montoPorProyecto) {
        this.montoPorProyecto = montoPorProyecto;
    }

    public Integer getProyectosCompletados() {
        return proyectosCompletados;
    }

    public void setProyectosCompletados(Integer proyectosCompletados) {
        this.proyectosCompletados = proyectosCompletados;
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    public void setFechaFinContrato(LocalDate fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }
}