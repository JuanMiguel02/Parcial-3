package org.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.demo.Models.Producto;
import org.demo.Repositories.ProductoRepository;

import static org.demo.Utils.AlertHelper.mostrarAlerta;

/**
 * Controlador encargado de gestionar la vista de productos.
 * Permite registrar, actualizar, eliminar y mostrar productos en una tabla.
 * Utiliza la clase ProductoRepository para realizar operaciones de persistencia.
 */
public class ProductosController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtCategoria;

    @FXML private TableView<Producto> tblProductos;

    @FXML private TableColumn<Producto,String> colNombre;
    @FXML private TableColumn<Producto,String> colPrecio;
    @FXML private TableColumn<Producto,String> colCantidad;
    @FXML private TableColumn<Producto,String> colCategoria;
    @FXML private TableColumn<Producto,Integer> colId;

    private ProductoRepository productoRepository;
    private DashboardController dashboardController;


    /**
     * Inicializa la vista de productos.
     * Configura las columnas de la tabla, carga los productos existentes
     * y establece el listener para manejar la selección de productos.
     */
    @FXML
    public void initialize(){
        productoRepository = ProductoRepository.getInstancia();

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        cargarProductos();

        tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, productoAnterior, productoSeleccionado) -> {
            if(productoSeleccionado != null){
                txtNombre.setText(productoSeleccionado.getNombre());
                txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
                txtCantidad.setText(String.valueOf(productoSeleccionado.getCantidad()));
                txtCategoria.setText(String.valueOf(productoSeleccionado.getCategoria()));
            }else{
                limpiarCampos();
            }
        });

    }

    /**
     * Maneja la acción del botón "Guardar".
     * Registra un nuevo producto si los campos son válidos y no existe otro con el mismo nombre.
     */
    @FXML
    private void onGuardarProducto(){
        if(!validarCampos()){
            return;
        }
        try{
            if(productoYaExiste(txtNombre.getText())){
                mostrarAlerta("Este producto: " + txtNombre.getText() + " ya se encuentra registrado");
                return;
            }

            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());

            if(precio <= 0 || cantidad <=0){
                mostrarAlerta("Precio o Cantidad inválidos");
            }
            String categoria = txtCategoria.getText();

            Producto producto = new Producto(precio, nombre, cantidad, categoria);
            productoRepository.guardarProducto(producto);
            mostrarAlerta("Éxito", "Producto: " + producto.getNombre()
                    + " $" + producto.getPrecio() + " Registrado Éxitosamente", Alert.AlertType.INFORMATION);
            limpiarCampos();
            cargarProductos();
        } catch (Exception e) {
            mostrarAlerta("No se ha podido registrar el producto");
        }
    }

    /**
     * Maneja la acción del botón "Eliminar".
     * Elimina el producto seleccionado de la tabla tras una confirmación del usuario.
     */
    @FXML
    private void onEliminarProducto(){
        Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();

        if(productoSeleccionado == null){
            mostrarAlerta("Por favor seleccione el producto para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar este producto?");
        confirmacion.setContentText("Producto: " + productoSeleccionado.getNombre() + " - Cantidad" + productoSeleccionado.getCantidad()
        + " -Precio: $ " + productoSeleccionado.getPrecio());

        confirmacion.showAndWait().ifPresent(respuesta ->{
            if(respuesta == ButtonType.OK){
                productoRepository.eliminarProducto(productoSeleccionado);
                cargarProductos();

                mostrarAlerta("Éxito", "Cliente Eliminado Éxitosamente", Alert.AlertType.INFORMATION  );
            }
        });
    }

    /**
     * Maneja la acción del botón "Actualizar".
     * Modifica los datos del producto seleccionado con los nuevos valores ingresados.
     */
    @FXML
    private void onActualizarProducto(){
        Producto  productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if(productoSeleccionado == null){
            mostrarAlerta("Por favor seleccione el producto para eliminar");
            return;
        }

        productoSeleccionado.setNombre(txtNombre.getText());
        productoSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
        productoSeleccionado.setCantidad(Integer.parseInt(txtCantidad.getText()));
        productoSeleccionado.setCategoria(txtCategoria.getText());

        productoRepository.actualizarProducto(productoSeleccionado);
        cargarProductos();
        tblProductos.refresh();

        mostrarAlerta("Éxito", "Producto Actualizado Éxitosamente", Alert.AlertType.INFORMATION  );
    }

    /**
     * Maneja la acción del botón "Limpiar".
     * Limpia todos los campos del formulario.
     */
    @FXML
    private void onLimpiarCampos(){
        limpiarCampos();
    }

    /**
     * Verifica si ya existe un producto con el mismo nombre.
     * @param nombre nombre del producto a verificar
     * @return true si ya existe, false en caso contrario
     */
    private boolean productoYaExiste(String nombre){
        return productoRepository.existeProductoConNombre(nombre);
    }

    /**
     * Carga los productos desde el repositorio y los muestra en la tabla.
     */
    private void cargarProductos(){
        tblProductos.setItems(productoRepository.getProductos());
    }

    /**
     * Valida que todos los campos requeridos estén diligenciados.
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean validarCampos(){
      if(txtNombre.getText().isEmpty()){
          mostrarAlerta("El nombre del producto es obligatorio");
          txtNombre.requestFocus();
          return false;
      }
      if(txtPrecio.getText().isEmpty()){
          mostrarAlerta("El precio del producto es obligatorio");
          txtPrecio.requestFocus();
          return false;
      }
      if(txtCantidad.getText().isEmpty()){
          mostrarAlerta("Cantidad del producto es obligatorio");
          txtCantidad.requestFocus();
          return false;
      }
      if(txtCategoria.getText().isEmpty()){
          mostrarAlerta("Categoria del producto es obligatorio");
          txtCategoria.requestFocus();
          return false;
      }
      return true;
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos(){
        txtNombre.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtCategoria.clear();
    }

    /**
     * Establece la referencia al controlador principal del dashboard.
     * @param dashboardController controlador principal
     */
    public void setDashboardController(DashboardController dashboardController){
        this.dashboardController = dashboardController;
    }
}
