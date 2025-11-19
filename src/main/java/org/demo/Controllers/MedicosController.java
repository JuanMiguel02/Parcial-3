package org.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.demo.Models.Medico;
import org.demo.Repositories.MedicoRepository;

import static org.demo.Utils.AlertHelper.mostrarAlerta;

public class MedicosController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtConsultorio;

    @FXML private TableView<Medico> tblMedicos;

    @FXML private TableColumn<Medico, String> colNombre;
    @FXML private TableColumn<Medico, String> colDocumento;
    @FXML private TableColumn<Medico, String> colTelefono;
    @FXML private TableColumn<Medico, String> colDireccion;
    @FXML private TableColumn<Medico, String> colCorreo;
    @FXML private TableColumn<Medico, String> colEspecialidad;
    @FXML private TableColumn<Medico, String> colConsultorio;

    private MedicoRepository medicoRepository;

    @FXML
    public void initialize() {
        medicoRepository = MedicoRepository.getInstancia();

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colConsultorio.setCellValueFactory(new PropertyValueFactory<>("consultorio"));

        cargarMedicos();

        tblMedicos.getSelectionModel().selectedItemProperty().addListener((obs, ant, sel) -> {
            if (sel != null) {
                txtNombre.setText(sel.getNombre());
                txtDocumento.setText(sel.getDocumento());
                txtTelefono.setText(sel.getTelefono());
                txtDireccion.setText(sel.getDireccion());
                txtCorreo.setText(sel.getCorreo());
                txtEspecialidad.setText(sel.getEspecialidad());
                txtConsultorio.setText(sel.getConsultorio());
            } else {
                limpiarCampos();
            }
        });
        configurarValidaciones();
    }

    @FXML
    private void onGuardarMedico() {
        if (!validarCampos()) return;

        if (medicoRepository.existeMedicoConDocumento(txtDocumento.getText())) {
            mostrarAlerta("Error", "Ya existe un médico con ese documento", Alert.AlertType.ERROR);
            return;
        }

        Medico medico = new Medico(
                txtNombre.getText(),
                txtDocumento.getText(),
                txtTelefono.getText(),
                txtDireccion.getText(),
                txtCorreo.getText(),
                txtEspecialidad.getText(),
                txtConsultorio.getText()
        );

        medicoRepository.guardarMedico(medico);

        mostrarAlerta("Éxito", "Médico registrado correctamente", Alert.AlertType.INFORMATION);

        limpiarCampos();
        cargarMedicos();
    }

    @FXML
    private void onEliminarMedico() {
        Medico sel = tblMedicos.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrarAlerta( "Seleccione un médico para eliminar");
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Confirmar eliminación");
        conf.setHeaderText("¿Eliminar médico?");
        conf.setContentText("Médico: " + sel.getNombre());

        conf.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                medicoRepository.eliminarMedico(sel);
                cargarMedicos();
                mostrarAlerta("Éxito", "Médico eliminado correctamente", Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void onActualizarMedico() {
        Medico sel = tblMedicos.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrarAlerta( "Seleccione un médico para actualizar");
            return;
        }

        sel.setNombre(txtNombre.getText());
        sel.setDocumento(txtDocumento.getText());
        sel.setTelefono(txtTelefono.getText());
        sel.setDireccion(txtDireccion.getText());
        sel.setCorreo(txtCorreo.getText());
        sel.setEspecialidad(txtEspecialidad.getText());
        sel.setConsultorio(txtConsultorio.getText());

        medicoRepository.actualizarMedico(sel);

        tblMedicos.refresh();
        mostrarAlerta("Éxito", "Médico actualizado correctamente", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onLimpiarCampos() {
        limpiarCampos();
    }

    private void cargarMedicos() {
        tblMedicos.setItems(medicoRepository.getMedicos());
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty()) { txtNombre.requestFocus(); mostrarAlerta("Nombre obligatorio"); return false; }
        if (txtDocumento.getText().isEmpty()) { txtDocumento.requestFocus(); mostrarAlerta("Documento obligatorio"); return false; }
        if (txtTelefono.getText().isEmpty()) { txtTelefono.requestFocus(); mostrarAlerta("Teléfono obligatorio"); return false; }
        if (txtDireccion.getText().isEmpty()) { txtDireccion.requestFocus(); mostrarAlerta("Dirección obligatoria"); return false; }
        if (txtCorreo.getText().isEmpty()) { txtCorreo.requestFocus(); mostrarAlerta("Correo obligatorio"); return false; }
        if (txtEspecialidad.getText().isEmpty()) { txtEspecialidad.requestFocus(); mostrarAlerta("Especialidad obligatoria"); return false; }
        if (txtConsultorio.getText().isEmpty()) { txtConsultorio.requestFocus(); mostrarAlerta("Consultorio obligatorio"); return false; }

        return true;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDocumento.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtCorreo.clear();
        txtEspecialidad.clear();
        txtConsultorio.clear();
    }

    private void configurarValidaciones() {

        // Solo letras y espacios (Nombre)
        txtNombre.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]*")) {
                txtNombre.setText(oldVal);
            }
        });

        // Solo números (Documento)
        txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtDocumento.setText(oldVal);
            }
        });

        // Solo números (Teléfono)
        txtTelefono.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtTelefono.setText(oldVal);
            }
        });

        // Letras + números + espacios (Dirección)
        txtDireccion.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-Z0-9ÁÉÍÓÚáéíóúÑñ\\s\\-#\\.]*")) {
                txtDireccion.setText(oldVal);
            }
        });

        // Correo: letras, números y símbolos permitidos
        txtCorreo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[A-Za-z0-9@._+-]*")) {
                txtCorreo.setText(oldVal);
            }
        });

        // Solo letras (Especialidad)
        txtEspecialidad.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]*")) {
                txtEspecialidad.setText(oldVal);
            }
        });

        // Consultorio: puede ser número o número con letra (ej: 304, 12B)
        txtConsultorio.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-Z0-9ÁÉÍÓÚáéíóúÑñ\\s]*")) {
                txtConsultorio.setText(oldVal);
            }
        });
    }

}
