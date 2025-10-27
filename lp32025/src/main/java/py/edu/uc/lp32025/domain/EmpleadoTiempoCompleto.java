package py.edu.uc.lp32025.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
public class EmpleadoTiempoCompleto extends Persona {

    // Campos específicos
    @Column(nullable = false)
    private BigDecimal salarioMensual; // Salario bruto

    @Column(nullable = false)
    private String departamento;

    // =================================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS DE PERSONA
    // =================================================================
    public EmpleadoTiempoCompleto() {
        super();
    }
    @Override
    public BigDecimal calcularSalario() {
        if (this.salarioMensual == null) {
            return BigDecimal.ZERO;
        }
        // Factor de retención: 1 - 0.09 = 0.91
        BigDecimal factorDescuento = new BigDecimal("0.91");

        return this.salarioMensual
                .multiply(factorDescuento)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula las deducciones del 5% sobre el salario mensual (bruto).
     * Nota: Cambiado a 'protected' para coincidir con la visibilidad del método abstracto en Persona.
     */
    @Override
    public BigDecimal calcularDeducciones() { // <--- CORRECCIÓN DE PUBLIC A PROTECTED
        if (this.salarioMensual == null) {
            return BigDecimal.ZERO;
        }
        // Factor de deducción: 0.05 (5%)
        BigDecimal factorDeduccion = new BigDecimal("0.05");

        return this.salarioMensual
                .multiply(factorDeduccion)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean validarDatosEspecificos() {
        final BigDecimal SALARIO_MINIMO_REQUERIDO = new BigDecimal("2899048");

        // 1. Validar campos no nulos
        if (this.salarioMensual == null || this.departamento == null || this.departamento.trim().isEmpty()) {
            return false;
        }

        // 2. Validar que el salario sea mayor o igual al mínimo requerido
        return this.salarioMensual.compareTo(SALARIO_MINIMO_REQUERIDO) >= 0;
    }

    // =================================================================
    // SOBRESCRITURA DE obtenerInformacionCompleta()
    // =================================================================

    @Override
    public String obtenerInformacionCompleta() {
        // Llama al método de la superclase para obtener la información base
        return super.obtenerInformacionCompleta() +
                // Agrega las propiedades específicas
                ", Departamento: " + this.departamento +
                ", Salario Bruto (Guardado): " + this.salarioMensual.setScale(2, RoundingMode.HALF_UP).toString();
    }


    // =================================================================
    // Getters y Setters para campos específicos
    // =================================================================
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