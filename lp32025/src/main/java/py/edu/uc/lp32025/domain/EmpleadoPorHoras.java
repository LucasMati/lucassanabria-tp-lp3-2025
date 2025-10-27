package py.edu.uc.lp32025.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class EmpleadoPorHoras extends Persona {

    @Column(nullable = false)
    private BigDecimal tarifaPorHora;

    @Column(nullable = false)
    private Integer horasTrabajadas;

    // =============================================================
    // Getters y Setters
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

    // =============================================================
    // Métodos sobrescritos
    // =============================================================

    /**
     * Salario = tarifa × horas + 50% adicional de la tarifa por horas extra (>40h)
     */
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

    /**
     * Deducciones = 2% del salario total
     */
    @Override
    public BigDecimal calcularDeducciones() {
        BigDecimal salario = calcularSalario();
        return salario.multiply(new BigDecimal("0.02"));
    }

    /**
     * Validación: tarifa > 0, horas entre 1 y 80
     */
    @Override
    public boolean validarDatosEspecificos() {
        return tarifaPorHora != null
                && tarifaPorHora.compareTo(BigDecimal.ZERO) > 0
                && horasTrabajadas != null
                && horasTrabajadas >= 1
                && horasTrabajadas <= 80;
    }
}
