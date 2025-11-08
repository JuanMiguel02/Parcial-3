package org.demo.Controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.demo.Repositories.ClienteRepository;
import org.demo.Repositories.ProductoRepository;
import org.demo.Repositories.VentaRepository;

import java.io.IOException;

/**
 * Controlador principal del panel de administración.
 * Gestiona la carga dinámica de vistas secundarias (clientes, productos y ventas)
 * dentro del área central del dashboard y muestra los conteos actualizados
 * de entidades registradas.
 */
public class DashboardController {

    @FXML private StackPane contenedorCentro;
    @FXML private AnchorPane vistaInicio;

    @FXML private Label lblClientes;
    @FXML private Label lblProductos;
    @FXML private Label lblVentas;

    /**
     * Inicializa el dashboard.
     * Establece enlaces automáticos entre las etiquetas de conteo y
     * las listas observables de los repositorios correspondientes.
     */
    @FXML
    public void initialize(){
        lblClientes.textProperty().bind(
                Bindings.size(ClienteRepository.getInstancia().getClientes()).asString()
        );
        lblProductos.textProperty().bind(
                Bindings.size(ProductoRepository.getInstancia().getProductos()).asString()
        );
        lblVentas.textProperty().bind(
                Bindings.size(VentaRepository.getInstancia().getVentas()).asString()
        );
    }

    /**
     * Carga dinámicamente una vista en el contenedor central del dashboard.
     * Reemplaza el contenido actual y ajusta los anclajes para adaptar la vista al área disponible.
     *
     * @param fxmlRuta ruta del archivo FXML que se desea cargar.
     */
    private void cargarVistaEnCentro(String fxmlRuta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRuta));
            Parent vista = loader.load();

            Object controller = loader.getController();

            try {
                controller.getClass()
                        .getMethod("setDashboardController", DashboardController.class)
                        .invoke(controller, this);
            }  catch (Exception ignored) {}


            // Limpiar el StackPane y agregar la nueva vista
            contenedorCentro.getChildren().clear();
            contenedorCentro.getChildren().add(vista);

            // Ajustar anclajes si es AnchorPane
            if (vista != null) {
                AnchorPane.setTopAnchor(vista, 0.0);
                AnchorPane.setBottomAnchor(vista, 0.0);
                AnchorPane.setLeftAnchor(vista, 0.0);
                AnchorPane.setRightAnchor(vista, 0.0);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la vista: " + e.getMessage(), e);
        }
    }

    /**
     * Muestra la vista inicial del dashboard.
     * Limpia el contenedor central y vuelve a establecer la vista de inicio.
     */
    @FXML
    public void onMostrarInicio() {
        contenedorCentro.getChildren().clear();
        vistaInicio.setVisible(true);
        vistaInicio.setManaged(true);
        contenedorCentro.getChildren().add(vistaInicio);
    }

    /**
     * Carga la vista de gestión de clientes en el panel central.
     */
    @FXML
    private void onVerClientes(){
        cargarVistaEnCentro("/org/demo/Clientes.fxml");
    }

    /**
     * Carga la vista de gestión de productos en el panel central.
     */
    @FXML
    private void onVerProductos(){
        cargarVistaEnCentro("/org/demo/Productos.fxml");
    }

    /**
     * Carga la vista de gestión de ventas en el panel central.
     */
    @FXML private void onVerVentas(){
        cargarVistaEnCentro("/org/demo/Ventas.fxml");
    }
}
