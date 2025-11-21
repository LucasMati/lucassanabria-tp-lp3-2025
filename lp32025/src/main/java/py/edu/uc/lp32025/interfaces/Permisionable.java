package py.edu.uc.lp32025.interfaces;

import py.edu.uc.lp32025.exception.PermisoDenegadoException;

import java.time.LocalDate;

/**
 * Interfaz base para empleados que pueden solicitar vacaciones y permisos.
 * Todos los métodos lanzan PermisoDenegadoException (clase base).
 */
public interface Permisionable {

    /**
     * Solicita vacaciones verificando límite anual de 20 días.
     * @throws PermisoDenegadoException si no cumple las condiciones
     */
    void solicitarVacaciones(LocalDate inicio, LocalDate fin) throws PermisoDenegadoException;

    /**
     * Solicita permiso justificando un motivo.
     * @throws PermisoDenegadoException si no cumple las condiciones
     */
    void solicitarPermiso(String motivo, LocalDate inicio, LocalDate fin) throws PermisoDenegadoException;
}