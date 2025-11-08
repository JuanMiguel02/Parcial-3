module org.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.demo to javafx.fxml;
    opens org.demo.Controllers to javafx.fxml;
    opens org.demo.Models to javafx.fxml;

    exports org.demo;
    exports org.demo.Controllers;
    exports org.demo.Models;
}