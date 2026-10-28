module ni.edu.uam.caso_3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.caso_3 to javafx.fxml;
    exports ni.edu.uam.caso_3;
}