package py.edu.uc.lp32025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.domain.EmpleadoTiempoCompleto;
import py.edu.uc.lp32025.repository.PersonaRepository;
import py.edu.uc.lp32025.dto.EmpleadoDto;
import py.edu.uc.lp32025.dto.ReporteEmpleadoDto;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;
import py.edu.uc.lp32025.exception.DiasInsuficientesException;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RemuneracionesService {

    private static final Logger log = LoggerFactory.getLogger(RemuneracionesService.class);

    private final PersonaRepository personaRepository;

    @Autowired
    public RemuneracionesService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    // ================================================================
    // Listar todos los empleados en forma polimórfica
    // ================================================================
    @Transactional(readOnly = true)
    public List<EmpleadoDto> listarTodosLosEmpleados() {

        List<Persona> empleados = personaRepository.findAll();

        return empleados.stream()
                .map(this::mapToEmpleadoDto)
                .collect(Collectors.toList());
    }

    private EmpleadoDto mapToEmpleadoDto(Persona persona) {

        String tipoEmpleado = (persona instanceof EmpleadoTiempoCompleto)
                ? "TIEMPO_COMPLETO"
                : persona.getClass().getSimpleName();

        String infoCompleta = persona.obtenerInformacionCompleta();

        return new EmpleadoDto(
                persona.getId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getNumeroDocumento(),
                persona.getFechaNacimiento(),
                tipoEmpleado,
                infoCompleta
        );
    }

    // ================================================================
    // Cálculo de nómina polimórfica
    // ================================================================
    public Map<String, BigDecimal> calcularNominaTotal() {

        List<Persona> empleados = personaRepository.findAll();
        Map<String, BigDecimal> nomina = new HashMap<>();

        log.info("=== Calculando nómina total (reporte polimórfico) ===");

        for (Persona p : empleados) {
            try {
                BigDecimal salario = p.calcularSalario();
                if (salario == null) salario = BigDecimal.ZERO;

                String tipo = p.getClass().getSimpleName();
                nomina.put(tipo, nomina.getOrDefault(tipo, BigDecimal.ZERO).add(salario));

                log.info("{} → {}: Salario {}", tipo, p.getNombre(), salario);

            } catch (Exception e) {
                log.warn("Error al calcular salario para {}: {}", p.getNombre(), e.getMessage());
                throw new RuntimeException(
                        "Error al calcular salario para " + p.getNombre() + ": " + e.getMessage()
                );
            }
        }

        log.info("=== Fin del cálculo de nómina ===");

        return nomina;
    }

    // ================================================================
    // Reporte polimórfico completo
    // ================================================================
    public List<ReporteEmpleadoDto> generarReporteCompleto() {

        List<Persona> empleados = personaRepository.findAll();
        List<ReporteEmpleadoDto> reporte = new java.util.ArrayList<>();

        log.info("=== Generando reporte polimórfico de empleados ===");

        for (Persona p : empleados) {

            try {

                log.info("──────────────────────────────────────────────");
                log.info("Empleado tipo: {} → {}", p.getClass().getSimpleName(), p.getNombre());

                String info = p.obtenerInformacionCompleta();
                BigDecimal salario = p.calcularSalario() != null ? p.calcularSalario() : BigDecimal.ZERO;
                BigDecimal impuestoBase = p.calcularImpuestoBase();
                BigDecimal deducciones = p.calcularDeducciones();
                BigDecimal impuestoTotal = p.calcularImpuestos();
                boolean datosValidos = true;

                try {
                    p.validarDatosEspecificos();
                } catch (Exception e) {
                    datosValidos = false;
                }

                ReporteEmpleadoDto dto = new ReporteEmpleadoDto(
                        p.getId(),
                        p.getNombre() + " " + p.getApellido(),
                        p.getClass().getSimpleName(),
                        info,
                        salario,
                        impuestoBase,
                        deducciones,
                        impuestoTotal,
                        datosValidos
                );

                reporte.add(dto);

            } catch (Exception ex) {

                log.warn("❌ Error al generar reporte para {}: {}", p.getNombre(), ex.getMessage());

                reporte.add(new ReporteEmpleadoDto(
                        p.getId(),
                        p.getNombre() + " " + p.getApellido(),
                        p.getClass().getSimpleName(),
                        "ERROR al generar reporte: " + ex.getMessage(),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        false
                ));
            }
        }

        log.info("=== Fin del reporte polimórfico ===");

        return reporte;
    }
}
