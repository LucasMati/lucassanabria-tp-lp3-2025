package py.edu.uc.lp32025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data                   // Genera getters, setters, toString, equals, hashCode
@AllArgsConstructor     // Genera un constructor con todos los campos
@NoArgsConstructor      // Genera un constructor vacío
public class EmpleadoDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String tipoEmpleado;        // Para identificar el subtipo (ej: TiempoCompleto)
    private String informacionCompleta; // Campo extra con el cálculo polimórfico
}
