package py.edu.uc.lp32025.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Contratista extends Persona {

    @Column(nullable = false)
    private BigDecimal montoPorProyecto;

    @Column(nullable = false)
    private Integer proyectosCompletados;

    @Column(nullable = false)
    private LocalDate fechaFinContrato;

    // =============================================================
    // Getters y Setters
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

    // =============================================================
    // Métodos sobrescritos
    // =============================================================

    /**
     * Salario = montoPorProyecto × proyectosCompletados
     */
    @Override
    public BigDecimal calcularSalario() {
        if (montoPorProyecto == null || proyectosCompletados == null) return BigDecimal.ZERO;
        return montoPorProyecto.multiply(BigDecimal.valueOf(proyectosCompletados));
    }

    /**
     * Deducciones = 0 (sin deducciones)
     */
    @Override
    public BigDecimal calcularDeducciones() {
        return BigDecimal.ZERO;
    }

    /**
     * Validación: fecha futura, proyectos ≥ 0, monto > 0
     */
    @Override
    public boolean validarDatosEspecificos() {
        return fechaFinContrato != null
                && fechaFinContrato.isAfter(LocalDate.now())
                && proyectosCompletados != null
                && proyectosCompletados >= 0
                && montoPorProyecto != null
                && montoPorProyecto.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Método adicional: contratoVigente
     */
    public boolean contratoVigente() {
        return fechaFinContrato != null && !fechaFinContrato.isBefore(LocalDate.now());
    }

    /**
     * Información extendida para el reporte
     */
    @Override
    public String obtenerInformacionCompleta() {
        return super.obtenerInformacionCompleta() +
                ", Fecha fin contrato: " + fechaFinContrato +
                ", Contrato vigente: " + contratoVigente();
    }
}
