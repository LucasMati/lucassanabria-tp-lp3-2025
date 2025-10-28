package py.edu.uc.lp32025.dto;

import java.math.BigDecimal;

public class EmpleadoTiempoCompletoImpuestoDto {

    private Long empleadoId;
    private BigDecimal salarioNeto;
    private BigDecimal impuestoBase;
    private BigDecimal deducciones;
    private BigDecimal impuestoTotalAPagar;
    private boolean datosValidos;
    private String informacionCompleta; // <-- CAMPO AÑADIDO

    // Constructor corregido
    public EmpleadoTiempoCompletoImpuestoDto(
            Long empleadoId,
            BigDecimal salarioNeto,
            BigDecimal impuestoBase,
            BigDecimal deducciones,
            BigDecimal impuestoTotalAPagar,
            boolean datosValidos,
            String informacionCompleta // <-- Argumento del constructor
    ) {
        this.empleadoId = empleadoId;
        this.salarioNeto = salarioNeto;
        this.impuestoBase = impuestoBase;
        this.deducciones = deducciones;
        this.impuestoTotalAPagar = impuestoTotalAPagar;
        this.datosValidos = datosValidos;
        this.informacionCompleta = informacionCompleta; // <-- ASIGNACIÓN CORRECTA
    }

    // Getters y Setters
    public Long getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Long empleadoId) { this.empleadoId = empleadoId; }
    public BigDecimal getSalarioNeto() { return salarioNeto; }
    public void setSalarioNeto(BigDecimal salarioNeto) { this.salarioNeto = salarioNeto; }
    public BigDecimal getImpuestoBase() { return impuestoBase; }
    public void setImpuestoBase(BigDecimal impuestoBase) { this.impuestoBase = impuestoBase; }
    public BigDecimal getDeducciones() { return deducciones; }
    public void setDeducciones(BigDecimal deducciones) { this.deducciones = deducciones; }
    public BigDecimal getImpuestoTotalAPagar() { return impuestoTotalAPagar; }
    public void setImpuestoTotalAPagar(BigDecimal impuestoTotalAPagar) { this.impuestoTotalAPagar = impuestoTotalAPagar; }
    public boolean isDatosValidos() { return datosValidos; }
    public void setDatosValidos(boolean datosValidos) { this.datosValidos = datosValidos; }

    // Getter y Setter para el nuevo campo
    public String getInformacionCompleta() { return informacionCompleta; }
    public void setInformacionCompleta(String informacionCompleta) { this.informacionCompleta = informacionCompleta; }
}