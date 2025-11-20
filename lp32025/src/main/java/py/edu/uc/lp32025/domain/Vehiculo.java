package py.edu.uc.lp32025.domain;

import py.edu.uc.lp32025.interfaces.Mapeable;

public class Vehiculo implements Mapeable {

    private String marca;
    private String modelo;

    public Vehiculo(String marca, String modelo) {
        this.marca = marca;
        this.modelo = modelo;
    }

    @Override
    public PosicionGps ubicarElemento() {
        // MOCK: Ubicación de vehículo ficticia
        return new PosicionGps(-25.3100, -57.5800);
    }

    @Override
    public Avatar obtenerImagen() {
        return new Avatar(
                "https://cdn-icons-png.flaticon.com/512/743/743007.png",
                marca + " " + modelo
        );
    }
}
