package py.edu.uc.lp32025.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Column;

// IMPORTS DE BEAN VALIDATION
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// IMPORTS DE JACKSON
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.math.BigDecimal;
import java.time.LocalDate;

// Configuración de Jackson para la deserialización polimórfica
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoEmpleado" // Campo esperado en el JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmpleadoTiempoCompleto.class, name = "TIEMPO_COMPLETO"),
        @JsonSubTypes.Type(value = EmpleadoPorHoras.class, name = "POR_HORA"),
        @JsonSubTypes.Type(value = Contratista.class, name = "CONTRATISTA")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos base
    @Column(nullable = false)
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    // =================================================================
    // APLICACIÓN DE BEAN VALIDATION EN numeroDocumento
    // =================================================================
    @NotBlank(message = "El número de documento no puede estar vacío.")

    // Regexp: Inicia con 1-9 (mayor a 0), seguido de 0 o más dígitos (solo números).
    @Pattern(regexp = "^[1-9][0-9]*$", message = "El número de documento debe ser un valor numérico positivo mayor a 0.")

    @Size(max = 20, message = "El número de documento no puede exceder los 20 dígitos.")

    @Column(unique = true, nullable = false)
    private String numeroDocumento;

    // CONSTRUCTOR SIN ARGUMENTOS (Obligatorio para JPA y Jackson)
    public Persona() {
        // Constructor vacío
    }

    // =================================================================
    // MÉTODOS ABSTRACTOS (Polimorfismo)
    // =================================================================

    /** Define el cálculo de salario neto. */
    public abstract BigDecimal calcularSalario();

    /** Define el cálculo de deducciones específicas (Template Method). */
    public abstract BigDecimal calcularDeducciones();

    /** Define la validación de campos específicos (lógica de negocio). */
    public abstract boolean validarDatosEspecificos();

    // =================================================================
    // TEMPLATE METHOD PATTERN (Cálculo de Impuestos)
    // =================================================================

    /**
     * Template Method: Define el esqueleto del algoritmo de cálculo de impuestos.
     */
    public final BigDecimal calcularImpuestos() {
        BigDecimal salario = this.calcularSalario();
        BigDecimal impuestoBase = this.calcularImpuestoBase();
        BigDecimal deducciones = this.calcularDeducciones();

        // Impuesto Total = Impuesto Base - Deducciones (mínimo 0)
        return impuestoBase.subtract(deducciones).max(BigDecimal.ZERO);
    }

    /** Calcula el 10% del salario (Impuesto Base). */
    public BigDecimal calcularImpuestoBase() {
        BigDecimal salario = this.calcularSalario();
        if (salario == null) return BigDecimal.ZERO;
        return salario.multiply(new BigDecimal("0.10"));
    }

    // =================================================================
    // MÉTODOS CONCRETOS
    // =================================================================

    /** Proporciona la información base de la persona y llama a calcularSalario() (Polimórfico). */
    public String obtenerInformacionCompleta() {
        return "ID: " + id +
                ", Nombre: " + nombre + " " + apellido +
                ", Documento: " + numeroDocumento +
                ", Fecha Nac.: " + fechaNacimiento.toString() +
                ", Salario Neto: " + this.calcularSalario().toString();
    }

    // =================================================================
    // Getters y Setters
    // =================================================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
}