package org.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 720);
        stage.setMaximized(true);
        stage.setTitle("Sistema de Gestión de Ventas");
        stage.setScene(scene);
        stage.show();
    }
}
