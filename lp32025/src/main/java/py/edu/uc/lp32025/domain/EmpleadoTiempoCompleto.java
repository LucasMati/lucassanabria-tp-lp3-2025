package py.edu.uc.lp32025.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.exception.DiasInsuficientesException;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;
import py.edu.uc.lp32025.interfaces.Permisionable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Slf4j
public class EmpleadoTiempoCompleto extends Persona implements Permisionable {

    private static final int LIMITE_DIAS_ANUALES = 20;

    @Column(nullable = false)
    private BigDecimal salarioMensual;  // SALARIO BRUTO

    @Column(nullable = false)
    private String departamento;

    public EmpleadoTiempoCompleto() {
        super();
    }

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

        log.info("✅ Vacaciones aprobadas para EmpleadoTiempoCompleto {}: {} días (Total anual: {}/{})",
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

        log.info("✅ Permiso aprobado para EmpleadoTiempoCompleto {}: {} días (Motivo: {}, Total anual: {}/{})",
                getNombre(), diasSolicitados, motivo, getTotalDiasSolicitados(), LIMITE_DIAS_ANUALES);
    }

    // =============================================================
    // MÉTODOS ABSTRACTOS IMPLEMENTADOS
    // =============================================================

    @Override
    public BigDecimal calcularSalario() {
        return salarioMensual == null ? BigDecimal.ZERO : salarioMensual;
    }

    @Override
    public BigDecimal calcularDeducciones() {
        if (salarioMensual == null || departamento == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal porcentaje = departamento.equalsIgnoreCase("IT")
                ? new BigDecimal("0.05")  // 5% para IT
                : new BigDecimal("0.03"); // 3% otros departamentos

        return salarioMensual.multiply(porcentaje)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean validarDatosEspecificos() {
        final BigDecimal SALARIO_MINIMO_REQUERIDO = new BigDecimal("2899048");

        return salarioMensual != null
                && salarioMensual.compareTo(SALARIO_MINIMO_REQUERIDO) >= 0
                && departamento != null
                && !departamento.isBlank();
    }

    // =============================================================
    // EXTENSIÓN DE obtenerInformacionCompleta()
    // =============================================================

    @Override
    public String obtenerInformacionCompleta() {
        return super.obtenerInformacionCompleta() +
                ", Departamento: " + departamento +
                ", Salario Bruto: " + salarioMensual.setScale(2, RoundingMode.HALF_UP) +
                ", Días solicitados: " + getTotalDiasSolicitados() + "/" + LIMITE_DIAS_ANUALES;
    }

    // =============================================================
    // GETTERS Y SETTERS
    // =============================================================

    public BigDecimal getSalarioMensual() {
        return salarioMensual;
    }

    public void setSalarioMensual(BigDecimal salarioMensual) {
        this.salarioMensual = salarioMensual;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}