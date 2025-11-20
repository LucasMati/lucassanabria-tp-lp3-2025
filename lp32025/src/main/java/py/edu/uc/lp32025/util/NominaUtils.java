package py.edu.uc.lp32025.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.dto.ReporteEmpleadoDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilidad para generar reportes de nómina en JSON.
 * Proporciona métodos estáticos para:
 * 1. Generar reporte completo de nómina
 * 2. Filtrar empleados por días solicitados (objetivo 2)
 */
public class NominaUtils {

    /**
     * Genera un reporte JSON de nómina con:
     * - salario
     * - impuestos
     * - deducciones
     * - días de vacaciones/permisos
     * - total días solicitados
     */
    public static String generarReporteNomina(List<Persona> empleados) {
        try {
            List<ReporteEmpleadoDto> reporte = empleados.stream()
                    .map(p -> new ReporteEmpleadoDto(
                            p.getId(),
                            p.getNombre() + " " + p.getApellido(),
                            p.getClass().getSimpleName(),
                            p.obtenerInformacionCompleta(),
                            p.calcularSalario() != null ? p.calcularSalario() : BigDecimal.ZERO,
                            p.calcularImpuestoBase(),
                            p.calcularDeducciones(),
                            p.calcularImpuestos(),
                            p.validarDatosEspecificos()
                    ))
                    .collect(Collectors.toList());

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reporte);

        } catch (Exception e) {
            throw new RuntimeException("Error generando reporte de nómina: " + e.getMessage());
        }
    }

    /**
     * Objetivo 2: Genera reporte JSON con empleados que han solicitado
     * más de un número especificado de días.
     *
     * @param empleados lista de personas
     * @param diasMinimos cantidad mínima de días solicitados
     * @return JSON con empleados que superan el threshold
     */
    public static String generarReporteEmpleadosConMasDias(
            List<Persona> empleados,
            int diasMinimos
    ) {
        try {
            List<ReporteEmpleadoDto> filtrados = empleados.stream()
                    .filter(p -> p.getTotalDiasSolicitados() > diasMinimos)
                    .map(p -> new ReporteEmpleadoDto(
                            p.getId(),
                            p.getNombre() + " " + p.getApellido(),
                            p.getClass().getSimpleName(),
                            "Días solicitados: " + p.getTotalDiasSolicitados() +
                                    " (Vacaciones: " + p.getDiasVacacionesAnuales() +
                                    ", Permisos: " + p.getDiasPermisoAnuales() + ")",
                            p.calcularSalario() != null ? p.calcularSalario() : BigDecimal.ZERO,
                            p.calcularImpuestoBase(),
                            p.calcularDeducciones(),
                            p.calcularImpuestos(),
                            p.validarDatosEspecificos()
                    ))
                    .collect(Collectors.toList());

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filtrados);

        } catch (Exception e) {
            throw new RuntimeException("Error generando reporte filtrado: " + e.getMessage());
        }
    }

    /**
     * Calcula el total de días solicitados por todos los empleados.
     */
    public static int calcularTotalDiasSolicitados(List<Persona> empleados) {
        return empleados.stream()
                .mapToInt(Persona::getTotalDiasSolicitados)
                .sum();
    }
}