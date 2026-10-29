package ni.edu.uam.caso_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader cargador = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Scene escena = new Scene(cargador.load(), 500, 500);

        stage.setTitle("Caja de ventas");
        stage.setScene(escena);
        stage.show();
    }
}