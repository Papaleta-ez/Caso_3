package ni.edu.uam.caso_3;

public class Caja {

    private int cantidadVentas = 0;
    private int totalRecaudado = 0;

    public synchronized Resumen registrarVenta() {
        cantidadVentas++;
        totalRecaudado += 100;

        return new Resumen(cantidadVentas, totalRecaudado);
    }

    public synchronized Resumen obtenerResumen() {
        return new Resumen(cantidadVentas, totalRecaudado);
    }

    public record Resumen(int ventas, int dinero) {
    }
}