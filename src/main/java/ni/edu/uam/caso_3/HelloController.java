package ni.edu.uam.caso_3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class HelloController {

    @FXML private Label vendedor1Label;
    @FXML private Label vendedor2Label;
    @FXML private Label vendedor3Label;

    @FXML private ProgressBar vendedor1Barra;
    @FXML private ProgressBar vendedor2Barra;
    @FXML private ProgressBar vendedor3Barra;

    @FXML private Label totalVentasLabel;
    @FXML private Label recaudacionLabel;
    @FXML private Label estadoLabel;

    @FXML private Button iniciarButton;
    @FXML private Button cancelarButton;

    private Caja caja;
    private Thread hilo1;
    private Thread hilo2;
    private Thread hilo3;

    private volatile boolean cancelado;
    private volatile boolean simulacionActiva;

    @FXML
    private void iniciarSimulacion() {

        if (simulacionActiva) {
            return;
        }

        caja = new Caja();
        cancelado = false;
        simulacionActiva = true;

        reiniciarVista();

        estadoLabel.setText("Estado: simulación en proceso");
        iniciarButton.setDisable(true);
        cancelarButton.setDisable(false);

        hilo1 = new Thread(() ->
                procesarVentas("Vendedor 1", vendedor1Label, vendedor1Barra)
        );

        hilo2 = new Thread(() ->
                procesarVentas("Vendedor 2", vendedor2Label, vendedor2Barra)
        );

        hilo3 = new Thread(() ->
                procesarVentas("Vendedor 3", vendedor3Label, vendedor3Barra)
        );

        hilo1.start();
        hilo2.start();
        hilo3.start();

        esperarHilos();
    }

    private void procesarVentas(
            String vendedor,
            Label etiqueta,
            ProgressBar barra
    ) {
        for (int venta = 1; venta <= 5; venta++) {

            if (cancelado || Thread.currentThread().isInterrupted()) {
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (cancelado) {
                return;
            }

            Caja.Resumen resumen = caja.registrarVenta();
            int ventaActual = venta;

            Platform.runLater(() -> {
                etiqueta.setText(vendedor + ": " + ventaActual + " / 5");
                barra.setProgress(ventaActual / 5.0);

                totalVentasLabel.setText(
                        "Total de ventas: " + resumen.ventas()
                );

                recaudacionLabel.setText(
                        "Recaudación: C$" + resumen.dinero()
                );
            });
        }
    }

    @FXML
    private void cancelarSimulacion() {

        if (!simulacionActiva) {
            return;
        }

        cancelado = true;
        estadoLabel.setText("Estado: cancelando...");
        cancelarButton.setDisable(true);

        interrumpirHilos();
    }

    private void esperarHilos() {

        Thread espera = new Thread(() -> {
            try {
                hilo1.join();
                hilo2.join();
                hilo3.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                Caja.Resumen resumenFinal = caja.obtenerResumen();

                totalVentasLabel.setText(
                        "Total de ventas: " + resumenFinal.ventas()
                );

                recaudacionLabel.setText(
                        "Recaudación: C$" + resumenFinal.dinero()
                );

                if (cancelado) {
                    estadoLabel.setText("Estado: cancelado");
                } else {
                    estadoLabel.setText("Estado: terminado");
                }

                simulacionActiva = false;
                iniciarButton.setDisable(false);
                cancelarButton.setDisable(true);
            });
        });

        espera.setDaemon(true);
        espera.start();
    }

    private void interrumpirHilos() {
        if (hilo1 != null) hilo1.interrupt();
        if (hilo2 != null) hilo2.interrupt();
        if (hilo3 != null) hilo3.interrupt();
    }

    private void reiniciarVista() {
        vendedor1Label.setText("Vendedor 1: 0 / 5");
        vendedor2Label.setText("Vendedor 2: 0 / 5");
        vendedor3Label.setText("Vendedor 3: 0 / 5");

        vendedor1Barra.setProgress(0);
        vendedor2Barra.setProgress(0);
        vendedor3Barra.setProgress(0);

        totalVentasLabel.setText("Total de ventas: 0");
        recaudacionLabel.setText("Recaudación: C$0");
    }

    public void cerrarAplicacion() {
        cancelado = true;
        interrumpirHilos();
    }
}