package py.edu.uc.lp32025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.edu.uc.lp32025.domain.EmpleadoTiempoCompleto;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.dto.EmpleadoTiempoCompletoImpuestoDto;
import py.edu.uc.lp32025.exception.EmpleadoNoEncontradoException;
import py.edu.uc.lp32025.mapper.EmpleadoTiempoCompletoMapper;
import py.edu.uc.lp32025.repository.EmpleadoTiempoCompletoRepository;
import py.edu.uc.lp32025.repository.PersonaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmpleadoTiempoCompletoService {

    private static final Logger log = LoggerFactory.getLogger(EmpleadoTiempoCompletoService.class);

    private final EmpleadoTiempoCompletoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final EmpleadoTiempoCompletoMapper mapper;

    private static final int BATCH_SIZE = 100;

    @Autowired
    public EmpleadoTiempoCompletoService(
            EmpleadoTiempoCompletoRepository empleadoRepository,
            PersonaRepository personaRepository,
            EmpleadoTiempoCompletoMapper mapper
    ) {
        this.empleadoRepository = empleadoRepository;
        this.personaRepository = personaRepository;
        this.mapper = mapper;
    }

    // ================================================================
    // CRUD
    // ================================================================
    public EmpleadoTiempoCompleto save(EmpleadoTiempoCompleto empleado) {
        return empleadoRepository.save(empleado);
    }

    public Optional<EmpleadoTiempoCompleto> findById(Long id) {
        return empleadoRepository.findById(id);
    }

    public List<EmpleadoTiempoCompleto> findAll() {
        return empleadoRepository.findAll();
    }

    public void deleteById(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException(id);
        }
        empleadoRepository.deleteById(id);
    }

    // ================================================================
    // Impuestos
    // ================================================================
    public Optional<EmpleadoTiempoCompletoImpuestoDto> calcularImpuestosDetalle(Long id) {

        EmpleadoTiempoCompleto empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(id));

        EmpleadoTiempoCompletoImpuestoDto dto = mapper.toDto(empleado);

        return Optional.of(dto);
    }

    // ================================================================
    // Batch simple
    // ================================================================
    @Transactional
    public List<Persona> guardarEmpleadosEnBatch(List<Persona> empleados) {

        if (empleados == null || empleados.isEmpty()) {
            return new ArrayList<>();
        }

        // Validación polimórfica
        List<String> errores = empleados.stream()
                .filter(e -> !e.validarDatosEspecificos())
                .map(e -> "ID: " + e.getId() + " - Falló validación específica")
                .collect(Collectors.toList());

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("Error en batch: " + String.join("; ", errores));
        }

        List<Persona> guardados = new ArrayList<>();

        for (int i = 0; i < empleados.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, empleados.size());
            guardados.addAll(personaRepository.saveAll(empleados.subList(i, end)));
        }

        return guardados;
    }

    // ================================================================
    // Batch con DTO respuesta
    // ================================================================
    @Transactional
    public py.edu.uc.lp32025.dto.BatchResponseDto guardarEmpleadosEnBatch(
            py.edu.uc.lp32025.dto.BatchEmpleadosRequest request) {

        List<Persona> empleados = request != null ? request.getEmpleados() : null;
        py.edu.uc.lp32025.dto.BatchResponseDto response = new py.edu.uc.lp32025.dto.BatchResponseDto();

        if (empleados == null || empleados.isEmpty()) {
            return response;
        }

        // Validación
        for (Persona p : empleados) {
            boolean valid = p != null && p.validarDatosEspecificos();

            response.addResult(new py.edu.uc.lp32025.dto.BatchResponseDto.ItemResult(
                    p != null ? p.getId() : null,
                    p != null ? p.getNombre() : null,
                    valid,
                    valid ? "VALID" : "FALLO_VALIDACION"
            ));
        }

        // Chunks
        for (int i = 0; i < empleados.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, empleados.size());
            List<Persona> chunk = empleados.subList(i, end);

            List<Persona> validChunk = chunk.stream()
                    .filter(p -> p != null && p.validarDatosEspecificos())
                    .toList();

            try {
                List<Persona> saved = personaRepository.saveAll(validChunk);

                for (Persona s : saved) {
                    response.getResults().stream()
                            .filter(r -> r.getNombre() != null && r.getNombre().equals(s.getNombre()))
                            .findFirst()
                            .ifPresent(r -> {
                                r.setId(s.getId());
                                r.setSuccess(true);
                                r.setMessage("GUARDADO");
                            });
                }

            } catch (Exception ex) {
                for (Persona p : validChunk) {
                    response.getResults().stream()
                            .filter(r -> r.getNombre() != null && r.getNombre().equals(p.getNombre()))
                            .forEach(r -> {
                                r.setSuccess(false);
                                r.setMessage("ERROR_PERSISTENCIA: " + ex.getMessage());
                            });
                }
            }
        }

        return response;
    }
}
