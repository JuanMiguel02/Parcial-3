package org.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.demo.Models.Paciente;
import org.demo.Models.TipoDocumento;
import org.demo.Repositories.PacienteRepository;

import static org.demo.Utils.AlertHelper.mostrarAlerta;

public class PacientesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtFechaNacimiento;
    @FXML private TextField txtEnfermedad;

    @FXML private ComboBox<TipoDocumento> cmbTipoDoc;

    @FXML private TableView<Paciente> tblPacientes;

    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, TipoDocumento> colTipoDoc;
    @FXML private TableColumn<Paciente, String> colDocumento;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colDireccion;
    @FXML private TableColumn<Paciente, String> colCorreo;
    @FXML private TableColumn<Paciente, String> colFechaNacimiento;
    @FXML private TableColumn<Paciente, String> colEnfermedad;

    private PacienteRepository pacienteRepository;

    @FXML
    public void initialize() {
        pacienteRepository = PacienteRepository.getInstancia();

        // Inicializar combo
        cmbTipoDoc.getItems().setAll(TipoDocumento.values());

        // Configurar columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipoDoc.setCellValueFactory(new PropertyValueFactory<>("tipoDocumentoFormateado"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numDocumento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colEnfermedad.setCellValueFactory(new PropertyValueFactory<>("enfermedad"));

        cargarPacientes();

        // Listener selección tabla
        tblPacientes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, sel) -> {
            if (sel != null) {
                txtNombre.setText(sel.getNombre());
                cmbTipoDoc.setValue(sel.getTipoDocumento());
                txtDocumento.setText(sel.getNumDocumento());
                txtTelefono.setText(sel.getTelefono());
                txtDireccion.setText(sel.getDireccion());
                txtCorreo.setText(sel.getCorreo());
                txtFechaNacimiento.setText(sel.getFechaNacimiento());
                txtEnfermedad.setText(sel.getEnfermedad());
            } else {
                limpiarCampos();
            }
        });

        configurarValidaciones();
    }

    // GUARDAR
    @FXML
    private void onGuardarPaciente() {

        if (!validarCampos()) return;

        if (cmbTipoDoc.getValue() == null) {
            mostrarAlerta("Debe seleccionar un tipo de documento");
            return;
        }

        if (pacienteYaExiste(txtCorreo.getText(), txtDocumento.getText(), txtTelefono.getText())) {
            mostrarAlerta("Advertencia",
                    "Ya existe un paciente con ese correo, documento o teléfono",
                    Alert.AlertType.WARNING);
            return;
        }

        Paciente p = new Paciente(
                txtNombre.getText(),
                cmbTipoDoc.getValue(),
                txtDocumento.getText(),
                txtTelefono.getText(),
                txtDireccion.getText(),
                txtCorreo.getText(),
                txtFechaNacimiento.getText(),
                txtEnfermedad.getText()
        );

        pacienteRepository.guardarPaciente(p);
        mostrarAlerta("Éxito", "Paciente registrado exitosamente", Alert.AlertType.INFORMATION);

        limpiarCampos();
        cargarPacientes();
    }

    // ELIMINAR
    @FXML
    private void onEliminarPaciente() {
        Paciente sel = tblPacientes.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrarAlerta("Debe seleccionar un paciente para eliminar");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText("¿Desea eliminar este paciente?");
        confirm.setContentText(sel.getNombre() + " - " + sel.getNumDocumento());

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                pacienteRepository.eliminarPaciente(sel);
                cargarPacientes();
                mostrarAlerta("Éxito", "Paciente eliminado", Alert.AlertType.INFORMATION);
            }
        });
    }

    // ACTUALIZAR
    @FXML
    private void onActualizarPaciente() {
        Paciente sel = tblPacientes.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrarAlerta("Debe seleccionar un paciente para actualizar");
            return;
        }

        if (cmbTipoDoc.getValue() == null) {
            mostrarAlerta("Debe seleccionar un tipo de documento");
            return;
        }

        sel.setNombre(txtNombre.getText());
        sel.setTipoDocumento(cmbTipoDoc.getValue());
        sel.setNumDocumento(txtDocumento.getText());
        sel.setTelefono(txtTelefono.getText());
        sel.setDireccion(txtDireccion.getText());
        sel.setCorreo(txtCorreo.getText());
        sel.setFechaNacimiento(txtFechaNacimiento.getText());
        sel.setEnfermedad(txtEnfermedad.getText());

        pacienteRepository.actualizarPaciente(sel);
        tblPacientes.refresh();

        mostrarAlerta("Éxito", "Paciente actualizado correctamente", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onLimpiarCampos() {
        limpiarCampos();
    }

    // AUXILIARES
    private void cargarPacientes() {
        tblPacientes.setItems(pacienteRepository.getPacientes());
    }

    private void limpiarCampos() {
        txtNombre.clear();
        cmbTipoDoc.setValue(null);
        txtDocumento.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtCorreo.clear();
        txtFechaNacimiento.clear();
        txtEnfermedad.clear();
    }

    private boolean pacienteYaExiste(String correo, String documento, String telefono) {
        return pacienteRepository.existePacienteConCorreo(correo)
                || pacienteRepository.existePacienteConDocumento(documento)
                || pacienteRepository.existePacienteConTelefono(telefono);
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta("El nombre es obligatorio");
            return false;
        }
        if (cmbTipoDoc.getValue() == null) {
            mostrarAlerta("Debe seleccionar un tipo de documento");
            return false;
        }
        if (!txtDocumento.getText().matches("\\d{5,}")) {
            mostrarAlerta("Documento inválido (mínimo 5 números)");
            return false;
        }
        if (!txtTelefono.getText().matches("\\d{10}")) {
            mostrarAlerta("Teléfono inválido (10 dígitos)");
            return false;
        }
        if (!txtCorreo.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            mostrarAlerta("Correo inválido");
            return false;
        }
        if (txtFechaNacimiento.getText().isEmpty()) {
            mostrarAlerta("La fecha de nacimiento es obligatoria");
            return false;
        }
        if (txtEnfermedad.getText().isEmpty()) {
            mostrarAlerta("Debe especificar una enfermedad");
            return false;
        }
        return true;
    }

    private void configurarValidaciones() {

        txtNombre.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]*")) {
                txtNombre.setText(oldVal);
            }
        });

        txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtDocumento.setText(oldVal);
            }
        });

        txtTelefono.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtTelefono.setText(oldVal);
            }
        });

        txtDireccion.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-Z0-9ÁÉÍÓÚáéíóúÑñ\\s\\-#\\.]*")) {
                txtDireccion.setText(oldVal);
            }
        });

        txtCorreo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[A-Za-z0-9@._+-]*")) {
                txtCorreo.setText(oldVal);
            }
        });

        txtFechaNacimiento.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[0-9\\-]*")) {
                txtFechaNacimiento.setText(oldVal);
            }
        });

        txtEnfermedad.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ0-9\\s]*")) {
                txtEnfermedad.setText(oldVal);
            }
        });
    }

}
