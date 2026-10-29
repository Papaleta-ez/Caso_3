package ni.edu.uam.caso_3;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class VendedorTask extends Task<Void> {

    private final String nombre;
    private final Caja caja;
    private final Consumer<Caja.Resumen> alRegistrarVenta;

    public VendedorTask(
            String nombre,
            Caja caja,
            Consumer<Caja.Resumen> alRegistrarVenta
    ) {
        this.nombre = nombre;
        this.caja = caja;
        this.alRegistrarVenta = alRegistrarVenta;
    }

    @Override
    protected Void call() {

        updateProgress(0, 5);
        updateMessage(nombre + ": 0 / 5");

        for (int venta = 1; venta <= 5; venta++) {

            if (isCancelled()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (isCancelled()) {
                break;
            }

            Caja.Resumen resumen = caja.registrarVenta();

            updateProgress(venta, 5);
            updateMessage(nombre + ": " + venta + " / 5");

            Platform.runLater(() -> alRegistrarVenta.accept(resumen));
        }

        return null;
    }
}