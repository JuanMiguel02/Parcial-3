package org.demo.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.demo.Models.Cliente;
import org.demo.Models.Producto;
import org.demo.Models.Venta;
import org.demo.Repositories.ClienteRepository;
import org.demo.Repositories.ProductoRepository;
import org.demo.Repositories.VentaRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.demo.Utils.AlertHelper.mostrarAlerta;

/**
 * Controlador encargado de gestionar el proceso de registro y visualización de ventas.
 * Administra las interacciones entre clientes, productos y las operaciones sobre la tabla de ventas.
 */
public class VentasController {

    @FXML private TextField txtFecha;
    @FXML private ComboBox<Cliente> cmbClientes;
    @FXML private ComboBox<Producto> cmbProductos;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtTotal;

    @FXML private TableView<Venta> tblVentas;

    @FXML private TableColumn<Venta, Integer> colId;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colProducto;
    @FXML private TableColumn<Venta, Double> colPrecio;
    @FXML private TableColumn<Venta, Integer> colCantidad;
    @FXML private TableColumn<Venta, Double> colTotal;

    private VentaRepository ventaRepository;
    private DashboardController dashboardController;

    /**
     * Inicializa los datos de la vista de ventas.
     * Configura las columnas de la tabla, carga los datos y establece los listeners.
     */
    @FXML
    public void initialize(){
        ventaRepository = VentaRepository.getInstancia();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha().toString()));
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNombre()));
        colProducto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getNombre()));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        cargarVentas();

        txtFecha.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
       cmbClientes.setItems(ClienteRepository.getInstancia().getClientes());
       cmbProductos.setItems(ProductoRepository.getInstancia().getProductos());

       cmbProductos.getSelectionModel().selectedItemProperty().addListener((observable, productoAnterior, productoSeleccionado) -> {
           if(productoSeleccionado !=null){
               txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
               actualizarTotal();
           }else{
               txtPrecio.clear();
               txtTotal.clear();
           }
       });

       txtCantidad.textProperty().addListener((observable, oldValue, newValue) -> {actualizarTotal();});
    }

    /**
     * Registra una nueva venta en el repositorio si los datos son válidos.
     * Verifica disponibilidad del producto y consistencia de los campos.
     */
    @FXML
    private void onGuardarVenta(){
        if(!validarCampos()){
            return;
        }
        try{
            Producto productoSeleccionado =cmbProductos.getSelectionModel().getSelectedItem();
            Cliente clienteSeleccionado =cmbClientes.getSelectionModel().getSelectedItem();

            if(clienteSeleccionado == null ||  productoSeleccionado == null){
                mostrarAlerta("Por favor rellene todos los campos");
                return;
            }

            int cantidad;
            try{
                cantidad = Integer.parseInt(txtCantidad.getText());
            }catch(NumberFormatException e){
                mostrarAlerta("Por favor ingrese una cantidad valida");
                return;
            }

            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));

            Venta venta = new Venta(clienteSeleccionado,productoSeleccionado, cantidad);
            ventaRepository.guardarVenta(venta);
            ProductoRepository.getInstancia().actualizarProducto(productoSeleccionado);

            mostrarAlerta("Éxito", "Producto Actualizado Éxitosamente", Alert.AlertType.INFORMATION  );
            System.out.println(ventaRepository.getVentas());
            limpiarCampos();

        }catch(Exception e){
            mostrarAlerta("La cantidad del producto seleccionado es insuficiente " );
        }
    }


    /**
     * Carga todas las ventas registradas en la tabla.
     */
    private void cargarVentas(){
        tblVentas.setItems(ventaRepository.getVentas());
    }


    /**
     * Limpia todos los campos del formulario de venta.
     */
    private void limpiarCampos(){
        txtFecha.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtTotal.clear();
        cmbProductos.setValue(null);
        cmbClientes.setValue(null);
    }

    /**
     * Calcula el total de la venta en función del precio del producto y la cantidad ingresada.
     * Actualiza el campo de total en tiempo real.
     */
    private void actualizarTotal(){
        Producto productoSeleccionado =cmbProductos.getSelectionModel().getSelectedItem();
        if(productoSeleccionado == null){
            txtTotal.clear();
            return;
        }

        double precio = productoSeleccionado.getPrecio();
        int cantidad;

        try{
            cantidad = Integer.parseInt(txtCantidad.getText());
            if(cantidad < 0){
                mostrarAlerta("Ingrese una cantidad válida");
                return;
            }
        }catch(NumberFormatException e){
            txtTotal.setText("");
            return;
        }
        double total = precio*cantidad;
        txtTotal.setText(String.valueOf(total));
    }

    /**
     * Valida los campos del formulario antes de registrar la venta.
     * @return true si los campos son válidos, false en caso contrario.
     */
    private boolean validarCampos(){
        if(txtFecha.getText().isEmpty()){
            mostrarAlerta("La fecha es obligatoria");
            txtFecha.requestFocus();
            return false;
        }
        if(txtPrecio.getText().isEmpty()){
            mostrarAlerta("El precio es obligatoria");
            txtPrecio.requestFocus();
            return false;
        }
        if(txtCantidad.getText().isEmpty()){
            mostrarAlerta(("La cantidad es obligatoria"));
            txtCantidad.requestFocus();
            return false;
        }
        if(txtTotal.getText().isEmpty()){
            mostrarAlerta("El total es obligatorio");
            txtTotal.requestFocus();
            return false;
        }
        if(cmbClientes.getSelectionModel().getSelectedItem() == null){
            mostrarAlerta("Seleccione un cliente");
            return false;
        }
        if(cmbProductos.getSelectionModel().getSelectedItem() == null){
            mostrarAlerta("Seleccione un producto");
            return false;
        }
        return true;
    }

    /**
     * Establece una referencia al controlador principal del panel de administración.
     * @param dashboardController instancia del controlador principal.
     */
    public void setDashboardController(DashboardController dashboardController){
        this.dashboardController = dashboardController;
    }
}
