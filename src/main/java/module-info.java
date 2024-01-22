module splatnet {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens splatnet to javafx.fxml;
    exports splatnet;
}