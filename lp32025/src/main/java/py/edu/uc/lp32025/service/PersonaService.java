package py.edu.uc.lp32025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.repository.PersonaRepository;
import py.edu.uc.lp32025.exception.NotFoundException;
import py.edu.uc.lp32025.exception.BusinessException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public List<Persona> findAllPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> findPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    public Persona savePersona(Persona persona) {
        // Validación de la fecha de nacimiento
        if (persona.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro.");
        }
        return personaRepository.save(persona);
    }
    public Persona updatePersona(Long id, Persona personaDetails) {
        Persona existing = personaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Persona no encontrada con ID: " + id));

        existing.setNombre(personaDetails.getNombre());
        existing.setApellido(personaDetails.getApellido());
        existing.setFechaNacimiento(personaDetails.getFechaNacimiento());
        existing.setNumeroDocumento(personaDetails.getNumeroDocumento());

        if (existing.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new BusinessException("VALIDATION_ERROR", "La fecha de nacimiento no puede ser en el futuro.");
        }

        return personaRepository.save(existing);
    }
    public void deletePersonaById(Long id) {
        personaRepository.deleteById(id);
    }
    public List<Persona> findByNombreContainingIgnoreCase(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}