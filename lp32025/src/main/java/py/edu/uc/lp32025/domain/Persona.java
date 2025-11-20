package py.edu.uc.lp32025.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import py.edu.uc.lp32025.interfaces.Mapeable;

@Setter
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoEmpleado"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmpleadoTiempoCompleto.class, name = "TIEMPO_COMPLETO"),
        @JsonSubTypes.Type(value = EmpleadoPorHoras.class, name = "POR_HORA"),
        @JsonSubTypes.Type(value = Contratista.class, name = "CONTRATISTA"),
        @JsonSubTypes.Type(value = Gerente.class, name = "GERENTE")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona implements Mapeable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📌 Datos básicos
    @Column(nullable = false)
    private String nombre;

    private String apellido;
    private LocalDate fechaNacimiento;

    // 📌 Validación documento
    @NotBlank(message = "El número de documento no puede estar vacío.")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "El número de documento debe ser un valor numérico positivo mayor a 0.")
    @Size(max = 20, message = "El número de documento no puede exceder los 20 dígitos.")
    @Column(unique = true, nullable = false)
    private String numeroDocumento;

    // =============================================================
    // 🆕 NUEVOS CAMPOS REQUERIDOS POR EL TRABAJO
    // =============================================================
    private int diasVacacionesAnuales = 0;
    private int diasPermisoAnuales = 0;

    public int getTotalDiasSolicitados() {
        return diasVacacionesAnuales + diasPermisoAnuales;
    }

    public Persona() {}

    // Métodos abstractos
    public abstract BigDecimal calcularSalario();
    public abstract BigDecimal calcularDeducciones();
    public abstract boolean validarDatosEspecificos();

    // Template método de impuestos
    public final BigDecimal calcularImpuestos() {
        BigDecimal salario = this.calcularSalario();
        BigDecimal impuestoBase = this.calcularImpuestoBase();
        BigDecimal deducciones = this.calcularDeducciones();
        return impuestoBase.subtract(deducciones).max(BigDecimal.ZERO);
    }

    public BigDecimal calcularImpuestoBase() {
        BigDecimal salario = this.calcularSalario();
        if (salario == null) return BigDecimal.ZERO;
        return salario.multiply(new BigDecimal("0.10"));
    }

    public String obtenerInformacionCompleta() {
        return "ID: " + id +
                ", Nombre: " + nombre + " " + apellido +
                ", Documento: " + numeroDocumento +
                ", Fecha Nac.: " + fechaNacimiento +
                ", Salario Neto: " + this.calcularSalario();
    }

    @Override
    public PosicionGps ubicarElemento() {
        return new PosicionGps(-25.2637, -57.5759);
    }

    @Override
    public Avatar obtenerImagen() {
        return new Avatar(
                "https://cdn-icons-png.flaticon.com/512/847/847969.png",
                nombre + " " + apellido
        );
    }
}
