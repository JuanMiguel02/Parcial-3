package org.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.demo.Models.Cliente;
import org.demo.Repositories.ClienteRepository;

import static java.util.regex.Pattern.matches;
import static org.demo.Utils.AlertHelper.mostrarAlerta;

/**
 * Controlador encargado de gestionar las operaciones CRUD de los clientes.
 * Administra la interfaz de usuario para registrar, actualizar, eliminar y visualizar clientes.
 */

public class ClientesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCorreo;

    @FXML private TableView<Cliente> tblClientes;

    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colDocumento;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, String> colCorreo;

    private ClienteRepository clienteRepository;
    private DashboardController dashboardController;

    /**
     * Inicializa la tabla y los listeners de selección de cliente.
     * Se ejecuta automáticamente al cargar la vista FXML.
     */
    @FXML
    public void initialize(){
        clienteRepository = ClienteRepository.getInstancia();

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        cargarClientes();

        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, clienteAnterior, clienteSeleccionado) -> {
            if (clienteSeleccionado != null) {
                txtNombre.setText(clienteSeleccionado.getNombre());
                txtDocumento.setText(clienteSeleccionado.getDocumento());
                txtTelefono.setText(clienteSeleccionado.getTelefono());
                txtDireccion.setText(clienteSeleccionado.getDireccion());
                txtCorreo.setText(clienteSeleccionado.getCorreo());
            } else {
                limpiarCampos();
            }
        });
    }

    /**
     * Guarda un nuevo cliente en el repositorio si los datos son válidos.
     * Muestra mensajes de validación y confirmación según el resultado.
     */

    @FXML
    private void onGuardarCliente(){
        if(!validarCampos()){
            return;
        }
        try{
            if(clienteYaExiste(txtCorreo.getText(), txtDocumento.getText(), txtTelefono.getText())){
                mostrarAlerta("Este cliente con correo: " + txtCorreo.getText()
                + " y documento: " + txtDocumento.getText() + " ya se encuentra registrado");
                return;
            }

            String nombre = txtNombre.getText();
            String documento = txtDocumento.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();
            String correo = txtCorreo.getText();

            Cliente cliente = new Cliente(nombre, documento, telefono, direccion, correo);
            clienteRepository.guardarCliente(cliente);
            System.out.println(clienteRepository.contarClientes());

            mostrarAlerta("Éxito", "Cliente: " + cliente.getNombre()
                    + " " + cliente.getDocumento() + " Registrado Éxitosamente", Alert.AlertType.INFORMATION);
            limpiarCampos();
            cargarClientes();

        }catch(Exception e){
            mostrarAlerta("No se ha podido registrar el cliente");
        }
    }

    /**
     * Elimina el cliente seleccionado de la tabla y del repositorio.
     * Solicita confirmación antes de eliminar.
     */
    @FXML
    private void onEliminarCliente(){
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();

        if(clienteSeleccionado == null){
            mostrarAlerta("Por favor seleccione un cliente para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar este cliente?");
        confirmacion.setContentText("Cliente: " + clienteSeleccionado.getNombre() + " - " + clienteSeleccionado
                .getDocumento());

        confirmacion.showAndWait().ifPresent(respuesta ->{
            if(respuesta == ButtonType.OK){
                clienteRepository.eliminarCliente(clienteSeleccionado);
                cargarClientes();

                mostrarAlerta("Éxito", "Cliente Eliminado Éxitosamente", Alert.AlertType.INFORMATION  );
            }
        });

    }

    /**
     * Actualiza la información del cliente seleccionado con los datos ingresados.
     */
    @FXML
    private void onActualizarCliente(){
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if(clienteSeleccionado == null){
            mostrarAlerta("Por favor seleccione un cliente para eliminar");
            return;
        }

        clienteSeleccionado.setNombre(txtNombre.getText());
        clienteSeleccionado.setDocumento(txtDocumento.getText());
        clienteSeleccionado.setTelefono(txtTelefono.getText());
        clienteSeleccionado.setDireccion(txtDireccion.getText());
        clienteSeleccionado.setCorreo(txtCorreo.getText());

        clienteRepository.actualizarCliente(clienteSeleccionado);
        cargarClientes();
        tblClientes.refresh();

        mostrarAlerta("Éxito", "Cliente Actualizado Éxitosamente", Alert.AlertType.INFORMATION  );

    }

    /**
     * Limpia los campos de texto del formulario.
     */
    @FXML
    private void onLimpiarCampos(){
        limpiarCampos();
    }

    /**
     * Verifica si ya existe un cliente con el mismo correo, documento o teléfono.
     * @param correo Correo del cliente.
     * @param documento Documento del cliente.
     * @param telefono Teléfono del cliente.
     * @return true si ya existe un cliente con alguno de esos datos, false en caso contrario.
     */
    private boolean clienteYaExiste(String correo, String documento, String telefono){
        boolean existeCorreo = clienteRepository.existeClienteConCorreo(correo);
        boolean existeDocumento = clienteRepository.existeClienteConDocumento(documento);
        boolean existeTelefono = clienteRepository.existeClienteConTelefono(telefono);

        return existeDocumento || existeCorreo || existeTelefono;
    }

    /**
     * Carga la lista de clientes en la tabla desde el repositorio.
     */
    private void cargarClientes(){
        tblClientes.setItems(clienteRepository.getClientes());
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos(){
        txtNombre.clear();
        txtDocumento.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtCorreo.clear();
    }


    /**
     * Valida los campos del formulario antes de guardar o actualizar.
     * @return true si los campos son válidos, false en caso contrario.
     */
    private boolean validarCampos(){
        if(txtNombre.getText().isEmpty()){
            mostrarAlerta("El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }
        if(txtDocumento.getText().isEmpty() || !txtDocumento.getText().matches("\\d{5,}")){
            mostrarAlerta("El documento debe ser válido (mínimo 5 números)");
            txtDocumento.requestFocus();
            return false;
        }
        if(txtTelefono.getText().isEmpty() || !txtTelefono.getText().matches("\\d{10}")){
            mostrarAlerta("El teléfono debe ser válido (10 números)");
            txtTelefono.requestFocus();
            return false;
        }
        if(txtDireccion.getText().isEmpty()){
            mostrarAlerta("La dirección es obligatorio");
            txtDireccion.requestFocus();
            return false;
        }
        if(txtCorreo.getText().isEmpty() || !txtCorreo.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            mostrarAlerta("La correo es obligatorio");
            txtCorreo.requestFocus();
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
