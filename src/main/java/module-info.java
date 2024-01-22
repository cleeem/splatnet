module splatnet {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens splatnet to javafx.fxml;
    exports splatnet;
    exports splatnet.controllers;
    opens splatnet.controllers to javafx.fxml;
}