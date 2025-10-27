package py.edu.uc.lp32025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp32025.domain.EmpleadoTiempoCompleto;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.repository.EmpleadoTiempoCompletoRepository;
import py.edu.uc.lp32025.repository.PersonaRepository;
import py.edu.uc.lp32025.dto.EmpleadoTiempoCompletoImpuestoDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.RoundingMode;
import java.util.stream.Collectors;

// Imports de SLF4J
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class EmpleadoTiempoCompletoService {

    // INYECCIÓN DEL LOGGER
    private static final Logger log = LoggerFactory.getLogger(EmpleadoTiempoCompletoService.class);
    // FIN INYECCIÓN LOGGER

    private final EmpleadoTiempoCompletoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private static final int BATCH_SIZE = 100;

    @Autowired
    public EmpleadoTiempoCompletoService(
            EmpleadoTiempoCompletoRepository empleadoRepository,
            PersonaRepository personaRepository
    ) {
        this.empleadoRepository = empleadoRepository;
        this.personaRepository = personaRepository;
        log.info("Servicio EmpleadoTiempoCompletoService inicializado.");
    }

    // Método para guardar o actualizar un empleado
    public EmpleadoTiempoCompleto save(EmpleadoTiempoCompleto empleado) {
        log.debug("Guardando empleado: {}", empleado.getNombre());
        return empleadoRepository.save(empleado);
    }

    // Método para obtener un empleado por ID
    public Optional<EmpleadoTiempoCompleto> findById(Long id) {
        log.debug("Buscando empleado con ID: {}", id);
        return empleadoRepository.findById(id);
    }

    // Método para obtener todos los empleados (CORREGIDO)
    public List<EmpleadoTiempoCompleto> findAll() {
        log.debug("Buscando todos los empleados de tiempo completo.");
        return empleadoRepository.findAll();
    }

    // Método para eliminar un empleado por ID
    public void deleteById(Long id) {
        log.warn("Eliminando empleado con ID: {}", id);
        empleadoRepository.deleteById(id);
    }

    // Cálculo de Impuestos
    public Optional<EmpleadoTiempoCompletoImpuestoDTO> calcularImpuestosDetalle(Long id) {
        log.info("Iniciando cálculo de impuestos para ID: {}", id);
        return empleadoRepository.findById(id).map(empleado -> {

            BigDecimal salarioNeto = empleado.calcularSalario();
            BigDecimal impuestoTotal = empleado.calcularImpuestos();

            log.debug("Cálculo para {}: Salario Neto: {}, Impuesto Total: {}",
                    empleado.getNombre(), salarioNeto, impuestoTotal);

            // ... (resto del cálculo y creación del DTO) ...
            BigDecimal impuestoBase = empleado.getSalarioMensual()
                    .multiply(new BigDecimal("0.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal deducciones = empleado.calcularDeducciones();
            boolean datosValidos = empleado.validarDatosEspecificos();
            String informacionCompleta = empleado.obtenerInformacionCompleta();

            return new EmpleadoTiempoCompletoImpuestoDTO(
                    empleado.getId(), salarioNeto, impuestoBase, deducciones,
                    impuestoTotal, datosValidos, informacionCompleta
            );
        }).or(() -> {
            log.warn("Empleado con ID {} no encontrado para cálculo de impuestos.", id);
            return Optional.empty();
        });
    }

    // =================================================================
    // 4.1 Persistencia en Batch
    // =================================================================

    @Transactional
    public List<Persona> guardarEmpleadosEnBatch(List<Persona> empleados) {
        if (empleados == null || empleados.isEmpty()) {
            log.warn("Intentando guardar un lote de empleados vacío.");
            return new ArrayList<>();
        }

        log.info("Iniciando procesamiento de lote para {} registros.", empleados.size());

        // 1. Validación Polimórfica (antes de persistir)
        List<String> errores = empleados.stream()
                .filter(empleado -> !empleado.validarDatosEspecificos())
                .map(empleado -> "ID: " + empleado.getId() + ", Nombre: " + empleado.getNombre() + " - Falló validación específica.")
                .collect(Collectors.toList());

        if (!errores.isEmpty()) {
            String mensajeError = "Validación de lote fallida. " + errores.size() + " errores.";
            log.error(mensajeError);
            throw new IllegalArgumentException(mensajeError + " Detalles: " + String.join("; ", errores));
        }

        log.debug("Validación polimórfica exitosa para todos los registros del lote.");

        // 2. Procesamiento en Chunks (lotes de 100)
        List<Persona> empleadosGuardados = new ArrayList<>();

        for (int i = 0; i < empleados.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, empleados.size());
            List<Persona> chunk = empleados.subList(i, end);

            log.info("Guardando chunk {}-{} (Tamaño: {})", i, end - 1, chunk.size());

            List<Persona> savedChunk = personaRepository.saveAll(chunk);
            empleadosGuardados.addAll(savedChunk);
        }

        log.info("Finalizado el procesamiento de lote. Total de registros guardados: {}", empleadosGuardados.size());

        return empleadosGuardados;
    }
    @Transactional
    public py.edu.uc.lp32025.dto.BatchResponseDto guardarEmpleadosEnBatch(py.edu.uc.lp32025.dto.BatchEmpleadosRequest request) {
        List<py.edu.uc.lp32025.domain.Persona> empleados = request != null ? request.getEmpleados() : null;
        py.edu.uc.lp32025.dto.BatchResponseDto response = new py.edu.uc.lp32025.dto.BatchResponseDto();

        if (empleados == null || empleados.isEmpty()) {
            log.warn("Intentando guardar un lote vacío en DTO.");
            return response;
        }

        log.info("Procesando batch con {} empleados (versión DTO).", empleados.size());

        final int BATCH_SIZE = 100;

        // Validación polimórfica inicial
        for (py.edu.uc.lp32025.domain.Persona p : empleados) {
            if (p == null) continue;
            boolean valid = false;
            try {
                valid = p.validarDatosEspecificos();
            } catch (Exception ex) {
                log.error("Error validando datos de {}: {}", p.getNombre(), ex.getMessage());
            }
            response.addResult(
                    new py.edu.uc.lp32025.dto.BatchResponseDto.ItemResult(
                            p.getId(),
                            p.getNombre(),
                            valid,
                            valid ? "VALID" : "FALLO_VALIDACION"
                    )
            );
        }

        // Procesamiento por chunks
        for (int i = 0; i < empleados.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, empleados.size());
            List<py.edu.uc.lp32025.domain.Persona> chunk = empleados.subList(i, end);

            List<py.edu.uc.lp32025.domain.Persona> validChunk = chunk.stream()
                    .filter(p -> p != null && p.validarDatosEspecificos())
                    .toList();

            if (validChunk.isEmpty()) continue;

            try {
                List<py.edu.uc.lp32025.domain.Persona> saved = personaRepository.saveAll(validChunk);
                for (py.edu.uc.lp32025.domain.Persona s : saved) {
                    for (py.edu.uc.lp32025.dto.BatchResponseDto.ItemResult r : response.getResults()) {
                        if (r.getNombre() != null && r.getNombre().equals(s.getNombre())) {
                            r.setId(s.getId());
                            r.setSuccess(true);
                            r.setMessage("GUARDADO");
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("Error persistiendo chunk: {}", ex.getMessage());
                for (py.edu.uc.lp32025.domain.Persona p : validChunk) {
                    for (py.edu.uc.lp32025.dto.BatchResponseDto.ItemResult r : response.getResults()) {
                        if (r.getNombre() != null && r.getNombre().equals(p.getNombre())) {
                            r.setSuccess(false);
                            r.setMessage("ERROR_PERSISTENCIA: " + ex.getMessage());
                        }
                    }
                }
            }
        }

        log.info("Batch finalizado (DTO). Registros procesados: {}", empleados.size());
        return response;
    }
}