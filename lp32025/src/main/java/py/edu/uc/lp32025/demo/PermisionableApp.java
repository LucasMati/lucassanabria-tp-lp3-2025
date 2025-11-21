package py.edu.uc.lp32025.demo;

import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.domain.EmpleadoTiempoCompleto;
import py.edu.uc.lp32025.domain.EmpleadoPorHoras;
import py.edu.uc.lp32025.domain.Contratista;
import py.edu.uc.lp32025.domain.Gerente;
import py.edu.uc.lp32025.exception.PermisoDenegadoException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Demostración del uso de interfaces Permisionable y PermisionableGerente
 * con TODOS los tipos de empleados.
 */
@Slf4j
public class PermisionableApp {
    public static void main(String[] args) {
        log.info("=== DEMOSTRACIÓN DE INTERFACE PERMISIONABLE ===\n");

        // =================================================================
        // 1️⃣ EMPLEADO TIEMPO COMPLETO
        // =================================================================
        EmpleadoTiempoCompleto empleadoTC = new EmpleadoTiempoCompleto();
        empleadoTC.setNombre("Lucas");
        empleadoTC.setApellido("Sanabria");
        empleadoTC.setNumeroDocumento("12345678");
        empleadoTC.setSalarioMensual(new BigDecimal("5000000"));
        empleadoTC.setDepartamento("IT");

        try {
            log.info("--- CASOS DE EMPLEADO TIEMPO COMPLETO ---");

            // ✅ Vacaciones válidas (9 días)
            empleadoTC.solicitarVacaciones(LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 10));
            log.info("✅ Vacaciones aprobadas: 9 días. Total acumulado: {}", empleadoTC.getTotalDiasSolicitados());

            // ✅ Permiso válido (5 días)
            empleadoTC.solicitarPermiso("Cita médica", LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 6));
            log.info("✅ Permiso aprobado: 5 días. Total acumulado: {}", empleadoTC.getTotalDiasSolicitados());

            // ❌ Intento exceder límite (solicita 7 días más cuando ya tiene 14)
            empleadoTC.solicitarVacaciones(LocalDate.of(2025, 12, 15), LocalDate.of(2025, 12, 22));

        } catch (PermisoDenegadoException e) {
            log.error("❌ EMPLEADO TC — {} | Motivo: {} | Rango: {} → {}",
                    e.getMessage(), e.getMotivo(), e.getFechaInicio(), e.getFechaFin());
        }

        // =================================================================
        // 2️⃣ EMPLEADO POR HORAS
        // =================================================================
        EmpleadoPorHoras empleadoPH = new EmpleadoPorHoras();
        empleadoPH.setNombre("María");
        empleadoPH.setApellido("González");
        empleadoPH.setNumeroDocumento("87654321");
        empleadoPH.setTarifaPorHora(new BigDecimal("50000"));
        empleadoPH.setHorasTrabajadas(40);

        try {
            log.info("\n--- CASOS DE EMPLEADO POR HORAS ---");

            // ✅ Vacaciones válidas (10 días)
            empleadoPH.solicitarVacaciones(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 11));
            log.info("✅ Vacaciones aprobadas: 10 días. Total: {}", empleadoPH.getTotalDiasSolicitados());

            // ✅ Permiso válido (5 días)
            empleadoPH.solicitarPermiso("Asunto personal", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 6));
            log.info("✅ Permiso aprobado: 5 días. Total: {}", empleadoPH.getTotalDiasSolicitados());

        } catch (PermisoDenegadoException e) {
            log.error("❌ EMPLEADO PH — {}", e.getMessage());
        }

        // =================================================================
        // 3️⃣ CONTRATISTA
        // =================================================================
        Contratista contratista = new Contratista();
        contratista.setNombre("Carlos");
        contratista.setApellido("López");
        contratista.setNumeroDocumento("11223344");
        contratista.setMontoPorProyecto(new BigDecimal("2000000"));
        contratista.setProyectosCompletados(3);
        contratista.setFechaFinContrato(LocalDate.of(2025, 12, 31));

        try {
            log.info("\n--- CASOS DE CONTRATISTA ---");

            // ✅ Vacaciones válidas (7 días)
            contratista.solicitarVacaciones(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 8));
            log.info("✅ Vacaciones aprobadas: 7 días. Total: {}", contratista.getTotalDiasSolicitados());

        } catch (PermisoDenegadoException e) {
            log.error("❌ CONTRATISTA — {}", e.getMessage());
        }

        // =================================================================
        // 4️⃣ GERENTE (CASO ESPECIAL - SIN LÍMITE DE 20 DÍAS)
        // =================================================================
        Gerente gerente = new Gerente();
        gerente.setNombre("Ana");
        gerente.setApellido("Martínez");
        gerente.setNumeroDocumento("99887766");
        gerente.setSalarioMensual(new BigDecimal("8000000"));
        gerente.setDepartamento("Management");

        try {
            log.info("\n=== DEMOSTRACIÓN DE INTERFACE PERMISIONABLEGERENTE ===");
            log.info("--- CASOS DE GERENTE (SIN LÍMITE ANUAL) ---");

            // ✅ Vacaciones válidas (25 días - excede 20 pero es gerente)
            gerente.solicitarVacaciones(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 26));
            log.info("✅ Vacaciones aprobadas: 25 días. Total: {} (sin límite)", gerente.getTotalDiasSolicitados());

            // ❌ Vacaciones excesivas (35 días consecutivos - excede límite de 30)
            gerente.solicitarVacaciones(LocalDate.of(2025, 12, 1), LocalDate.of(2026, 1, 5));

        } catch (PermisoDenegadoException e) {
            log.error("❌ GERENTE — {} | Motivo: {} | Rango: {} → {}",
                    e.getMessage(), e.getMotivo(), e.getFechaInicio(), e.getFechaFin());
        }

        try {
            // ✅ Permiso válido (8 días)
            gerente.solicitarPermiso("Conferencia internacional", LocalDate.of(2025, 10, 5), LocalDate.of(2025, 10, 13));
            log.info("✅ Permiso aprobado: 8 días. Total: {} (sin límite)", gerente.getTotalDiasSolicitados());

            // ❌ Permiso excesivo (15 días - excede límite de 10)
            gerente.solicitarPermiso("Descanso extendido", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 16));

        } catch (PermisoDenegadoException e) {
            log.error("❌ GERENTE — {} | Motivo: {} | Rango: {} → {}",
                    e.getMessage(), e.getMotivo(), e.getFechaInicio(), e.getFechaFin());
        }

        // 💰 Cálculo de bono anual
        BigDecimal bono = gerente.calcularBonoAnual();
        log.info("💵 Bono anual asignado a gerente {}: {}", gerente.getNombre(), bono);

        log.info("\n=== RESUMEN FINAL ===");
        log.info("Empleado TC: {} días solicitados de 20 permitidos", empleadoTC.getTotalDiasSolicitados());
        log.info("Empleado PH: {} días solicitados de 20 permitidos", empleadoPH.getTotalDiasSolicitados());
        log.info("Contratista: {} días solicitados de 20 permitidos", contratista.getTotalDiasSolicitados());
        log.info("Gerente: {} días solicitados (SIN LÍMITE)", gerente.getTotalDiasSolicitados());

        log.info("\n=== FIN DEMO ===");
    }
}