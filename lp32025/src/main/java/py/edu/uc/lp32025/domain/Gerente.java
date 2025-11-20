package py.edu.uc.lp32025.domain;

import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;
import py.edu.uc.lp32025.interfaces.PermisionableGerente;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Clase Gerente que implementa PermisionableGerente.
 *
 * IMPORTANTE: Los gerentes NO tienen el límite de 20 días anuales.
 * Pueden solicitar más de 20 días al año (objetivo 8 del trabajo).
 *
 * Límites propios de Gerente:
 * - Vacaciones: máximo 30 días CONSECUTIVOS por solicitud
 * - Permisos: máximo 10 días por solicitud
 */
@Entity
@Slf4j
public class Gerente extends EmpleadoTiempoCompleto implements PermisionableGerente {

    private static final int LIMITE_VACACIONES_CONSECUTIVAS = 30;
    private static final int LIMITE_PERMISOS = 10;

    private BigDecimal bonoAnual = BigDecimal.ZERO;

    /**
     * Solicita vacaciones para un gerente.
     * Los gerentes pueden superar los 20 días anuales (sin límite anual).
     * Solo valida que no excedan 30 días consecutivos por solicitud.
     */
    @Override
    public void solicitarVacaciones(LocalDate inicio, LocalDate fin) throws PermisoDenegadoException {

        long diasSolicitados = ChronoUnit.DAYS.between(inicio, fin);

        // Validar límite de días consecutivos (no límite anual de 20)
        if (diasSolicitados > LIMITE_VACACIONES_CONSECUTIVAS) {
            throw new PermisoDenegadoException(
                    "Vacaciones rechazadas para gerente: excede el límite de " +
                            LIMITE_VACACIONES_CONSECUTIVAS + " días consecutivos",
                    "Solicitó " + diasSolicitados + " días consecutivos (máximo: " +
                            LIMITE_VACACIONES_CONSECUTIVAS + ")",
                    inicio,
                    fin
            );
        }

        // Registrar días → Los gerentes SÍ pueden superar los 20 días anuales
        setDiasVacacionesAnuales(getDiasVacacionesAnuales() + (int) diasSolicitados);

        log.info("✓ Vacaciones aprobadas para gerente {} por {} días. Total anual: {} (SIN límite de 20 días)",
                getNombre(), diasSolicitados, getTotalDiasSolicitados());
    }

    /**
     * Solicita permiso para un gerente.
     * Los gerentes pueden superar los 20 días anuales.
     * Solo valida que no excedan 10 días por solicitud de permiso.
     */
    @Override
    public void solicitarPermiso(String motivo, LocalDate inicio, LocalDate fin) throws PermisoDenegadoException {

        long diasSolicitados = ChronoUnit.DAYS.between(inicio, fin);

        // Validar límite de días de permiso
        if (diasSolicitados > LIMITE_PERMISOS) {
            throw new PermisoDenegadoException(
                    "Permiso rechazado para gerente: excede el límite de " +
                            LIMITE_PERMISOS + " días para permisos",
                    "Solicitó " + diasSolicitados + " días (máximo: " + LIMITE_PERMISOS + ")",
                    inicio,
                    fin
            );
        }

        // Registrar días → Los gerentes SÍ pueden superar los 20 días anuales
        setDiasPermisoAnuales(getDiasPermisoAnuales() + (int) diasSolicitados);

        log.info("✓ Permiso aprobado para gerente {} ({}) de {} días. Total anual: {} (SIN límite de 20 días)",
                getNombre(), motivo, diasSolicitados, getTotalDiasSolicitados());
    }

    /**
     * Calcula el bono anual del gerente (15% del salario).
     */
    @Override
    public BigDecimal calcularBonoAnual() {
        BigDecimal salario = calcularSalario();
        this.bonoAnual = salario.multiply(new BigDecimal("0.15"));
        return bonoAnual;
    }

    // =============================================================
    // Getters y Setters
    // =============================================================

    public BigDecimal getBonoAnual() {
        return bonoAnual;
    }

    public void setBonoAnual(BigDecimal bonoAnual) {
        this.bonoAnual = bonoAnual;
    }

    @Override
    public String obtenerInformacionCompleta() {
        return super.obtenerInformacionCompleta() +
                ", Bono Anual: " + bonoAnual +
                ", Días solicitados: " + getTotalDiasSolicitados() + " (sin límite)";
    }
}