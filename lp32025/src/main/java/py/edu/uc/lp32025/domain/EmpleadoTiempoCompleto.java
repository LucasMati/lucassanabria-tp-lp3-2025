package py.edu.uc.lp32025.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class EmpleadoTiempoCompleto extends Persona {

    @Column(nullable = false)
    private BigDecimal salarioMensual;  // SALARIO BRUTO

    @Column(nullable = false)
    private String departamento;

    public EmpleadoTiempoCompleto() {
        super();
    }

    // =============================================================
    // MÉTODOS ABSTRACTOS IMPLEMENTADOS
    // =============================================================

    /**
     * Según el enunciado:
     * calcularSalario(): retorna el salario mensual.
     */
    @Override
    public BigDecimal calcularSalario() {
        return salarioMensual == null ? BigDecimal.ZERO : salarioMensual;
    }

    /**
     * Regla del TP:
     * - 5% si es del departamento IT
     * - 3% para el resto
     */
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

    /**
     * Regla del TP:
     * validarDatosEspecificos():
     *  - salario > 0
     *  - departamento no vacío
     *
     * *PLUS*: mantenemos tu salario mínimo requerido (2899048),
     * porque vos querías conservarlo.
     */
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
                ", Salario Bruto: " + salarioMensual.setScale(2, RoundingMode.HALF_UP);
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
