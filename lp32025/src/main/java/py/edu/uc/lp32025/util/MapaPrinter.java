package py.edu.uc.lp32025.util;

import lombok.extern.slf4j.Slf4j;
import py.edu.uc.lp32025.interfaces.Mapeable;
import py.edu.uc.lp32025.domain.PosicionGps;
import py.edu.uc.lp32025.domain.Avatar;

import java.util.List;

@Slf4j
public class MapaPrinter {

    public static void imprimirMapa(List<Mapeable> elementos) {

        log.info("=== MOSTRANDO ELEMENTOS MAPEABLES ===");

        for (Mapeable elemento : elementos) {
            PosicionGps pos = elemento.ubicarElemento();
            Avatar avatar = elemento.obtenerImagen();

            log.info(">> Elemento: {}", avatar.getNick());
            log.info("📍 Ubicación → Lat: {} | Lon: {}", pos.getLatitud(), pos.getLongitud());
            log.info("🖼️ Imagen → {}", avatar.getImagen());
        }

        log.info("=== FIN LISTADO MAPEABLE ===");
    }
}
