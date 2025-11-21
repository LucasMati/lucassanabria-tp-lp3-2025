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
public class EmpleadoPorHoras extends Persona implements Permisionable {

    private static final int LIMITE_DIAS_ANUALES = 20;

    @Column(nullable = false)
    private BigDecimal tarifaPorHora;

    @Column(nullable = false)
    private Integer horasTrabajadas;

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

        log.info("✅ Vacaciones aprobadas para EmpleadoPorHoras {}: {} días (Total anual: {}/{})",
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

        log.info("✅ Permiso aprobado para EmpleadoPorHoras {}: {} días (Motivo: {}, Total anual: {}/{})",
                getNombre(), diasSolicitados, motivo, getTotalDiasSolicitados(), LIMITE_DIAS_ANUALES);
    }

    // =============================================================
    // MÉTODOS SOBRESCRITOS
    // =============================================================

    @Override
    public BigDecimal calcularSalario() {
        if (tarifaPorHora == null || horasTrabajadas == null) return BigDecimal.ZERO;

        BigDecimal salarioBase = tarifaPorHora.multiply(BigDecimal.valueOf(Math.min(horasTrabajadas, 40)));

        if (horasTrabajadas > 40) {
            int horasExtra = horasTrabajadas - 40;
            BigDecimal extra = tarifaPorHora.multiply(BigDecimal.valueOf(1.5))
                    .multiply(BigDecimal.valueOf(horasExtra));
            salarioBase = salarioBase.add(extra);
        }

        return salarioBase;
    }

    @Override
    public BigDecimal calcularDeducciones() {
        BigDecimal salario = calcularSalario();
        return salario.multiply(new BigDecimal("0.02"));
    }

    @Override
    public boolean validarDatosEspecificos() {
        return tarifaPorHora != null
                && tarifaPorHora.compareTo(BigDecimal.ZERO) > 0
                && horasTrabajadas != null
                && horasTrabajadas >= 1
                && horasTrabajadas <= 80;
    }

    @Override
    public String obtenerInformacionCompleta() {
        return super.obtenerInformacionCompleta() +
                ", Tarifa por hora: " + tarifaPorHora +
                ", Horas trabajadas: " + horasTrabajadas +
                ", Días solicitados: " + getTotalDiasSolicitados() + "/" + LIMITE_DIAS_ANUALES;
    }

    // =============================================================
    // GETTERS Y SETTERS
    // =============================================================

    public BigDecimal getTarifaPorHora() {
        return tarifaPorHora;
    }

    public void setTarifaPorHora(BigDecimal tarifaPorHora) {
        this.tarifaPorHora = tarifaPorHora;
    }

    public Integer getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Integer horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }
}