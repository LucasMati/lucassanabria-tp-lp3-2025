package py.edu.uc.lp32025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.edu.uc.lp32025.domain.Persona;
import py.edu.uc.lp32025.repository.PersonaRepository;

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

    public void deletePersonaById(Long id) {
        personaRepository.deleteById(id);
    }
}