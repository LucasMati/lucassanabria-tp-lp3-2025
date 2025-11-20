package py.edu.uc.lp32025.interfaces;

import py.edu.uc.lp32025.exception.DiasInsuficientesException;

import java.time.LocalDate;

public interface Permisionable {

    /**
     * Solicita vacaciones verificando límite anual de 20 días.
     */
    void solicitarVacaciones(LocalDate inicio, LocalDate fin) throws DiasInsuficientesException;

    /**
     * Solicita permiso justificando un motivo.
     */
    void solicitarPermiso(String motivo, LocalDate inicio, LocalDate fin) throws DiasInsuficientesException;
}
