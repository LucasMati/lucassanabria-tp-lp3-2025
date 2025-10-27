package py.edu.uc.lp32025.dto;

import java.time.LocalDate;

public class EmpleadoDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String tipoEmpleado; // Para identificar el subtipo (ej: TiempoCompleto)
    private String informacionCompleta; // Campo extra con el cálculo polimórfico

    // Constructor, Getters y Setters
    public EmpleadoDto(Long id, String nombre, String apellido, String numeroDocumento, LocalDate fechaNacimiento, String tipoEmpleado, String informacionCompleta) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroDocumento = numeroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoEmpleado = tipoEmpleado;
        this.informacionCompleta = informacionCompleta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTipoEmpleado() { return tipoEmpleado; }
    public void setTipoEmpleado(String tipoEmpleado) { this.tipoEmpleado = tipoEmpleado; }
    public String getInformacionCompleta() { return informacionCompleta; }
    public void setInformacionCompleta(String informacionCompleta) { this.informacionCompleta = informacionCompleta; }
}