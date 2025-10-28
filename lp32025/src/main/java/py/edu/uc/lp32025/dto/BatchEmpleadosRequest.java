package py.edu.uc.lp32025.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import py.edu.uc.lp32025.domain.Persona;
import java.util.List;

public class BatchEmpleadosRequest {

    @NotEmpty(message = "La lista de empleados no puede estar vacía.")
    @Valid // 🔹 Habilita validación sobre cada Persona en la lista
    private List<Persona> empleados;

    public BatchEmpleadosRequest() {}

    public BatchEmpleadosRequest(List<Persona> empleados) {
        this.empleados = empleados;
    }

    public List<Persona> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Persona> empleados) {
        this.empleados = empleados;
    }
}
