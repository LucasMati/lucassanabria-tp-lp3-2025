package py.edu.uc.lp32025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.uc.lp32025.domain.EmpleadoPorHoras;
import java.util.List;

public interface EmpleadoPorHorasRepository extends JpaRepository<EmpleadoPorHoras, Long> {
    List<EmpleadoPorHoras> findByHorasTrabajadasGreaterThan(Integer horas);
}
