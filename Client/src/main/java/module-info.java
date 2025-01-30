module Client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires Common;

    opens org.chinesecheckers.client.main to javafx.fxml;
    exports org.chinesecheckers.client.main;
}