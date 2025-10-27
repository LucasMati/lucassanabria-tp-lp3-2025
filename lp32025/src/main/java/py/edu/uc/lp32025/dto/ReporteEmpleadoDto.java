package py.edu.uc.lp32025.dto;

import java.math.BigDecimal;

public class ReporteEmpleadoDto {
    private Long id;
    private String nombreCompleto;
    private String tipoEmpleado;
    private String informacionCompleta;
    private BigDecimal salario;
    private BigDecimal impuestoBase;
    private BigDecimal deducciones;
    private BigDecimal impuestoTotal;
    private boolean datosValidos;

    public ReporteEmpleadoDto() {}

    public ReporteEmpleadoDto(Long id, String nombreCompleto, String tipoEmpleado, String informacionCompleta, java.math.BigDecimal salario, java.math.BigDecimal impuestoBase, java.math.BigDecimal deducciones, java.math.BigDecimal impuestoTotal, boolean datosValidos) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.tipoEmpleado = tipoEmpleado;
        this.informacionCompleta = informacionCompleta;
        this.salario = salario;
        this.impuestoBase = impuestoBase;
        this.deducciones = deducciones;
        this.impuestoTotal = impuestoTotal;
        this.datosValidos = datosValidos;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTipoEmpleado() { return tipoEmpleado; }
    public void setTipoEmpleado(String tipoEmpleado) { this.tipoEmpleado = tipoEmpleado; }
    public String getInformacionCompleta() { return informacionCompleta; }
    public void setInformacionCompleta(String informacionCompleta) { this.informacionCompleta = informacionCompleta; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public BigDecimal getImpuestoBase() { return impuestoBase; }
    public void setImpuestoBase(BigDecimal impuestoBase) { this.impuestoBase = impuestoBase; }
    public BigDecimal getDeducciones() { return deducciones; }
    public void setDeducciones(BigDecimal deducciones) { this.deducciones = deducciones; }
    public BigDecimal getImpuestoTotal() { return impuestoTotal; }
    public void setImpuestoTotal(BigDecimal impuestoTotal) { this.impuestoTotal = impuestoTotal; }
    public boolean isDatosValidos() { return datosValidos; }
    public void setDatosValidos(boolean datosValidos) { this.datosValidos = datosValidos; }
}
