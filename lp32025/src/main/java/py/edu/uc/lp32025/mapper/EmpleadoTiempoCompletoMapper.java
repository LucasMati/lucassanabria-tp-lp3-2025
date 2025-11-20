package py.edu.uc.lp32025.mapper;

import org.springframework.stereotype.Component;
import py.edu.uc.lp32025.domain.EmpleadoTiempoCompleto;
import py.edu.uc.lp32025.dto.EmpleadoTiempoCompletoImpuestoDto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Mapper responsable de convertir la entidad EmpleadoTiempoCompleto
 * a su DTO EmpleadoTiempoCompletoImpuestoDto.
 */
@Component
public class EmpleadoTiempoCompletoMapper
        implements BaseMapper<EmpleadoTiempoCompleto, EmpleadoTiempoCompletoImpuestoDto> {

    @Override
    public EmpleadoTiempoCompletoImpuestoDto toDto(EmpleadoTiempoCompleto empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no puede ser nulo.");
        }

        BigDecimal salarioNeto = empleado.calcularSalario();
        BigDecimal impuestoTotal = empleado.calcularImpuestos();
        BigDecimal impuestoBase = empleado.getSalarioMensual()
                .multiply(new BigDecimal("0.10"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal deducciones = empleado.calcularDeducciones();

        return new EmpleadoTiempoCompletoImpuestoDto(
                empleado.getId(),
                salarioNeto,
                impuestoBase,
                deducciones,
                impuestoTotal,
                empleado.validarDatosEspecificos(),
                empleado.obtenerInformacionCompleta()
        );
    }
}
