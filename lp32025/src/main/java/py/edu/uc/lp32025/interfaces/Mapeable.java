package py.edu.uc.lp32025.interfaces;

import py.edu.uc.lp32025.domain.PosicionGps;
import py.edu.uc.lp32025.domain.Avatar;

public interface Mapeable {
    PosicionGps ubicarElemento();
    Avatar obtenerImagen();
}