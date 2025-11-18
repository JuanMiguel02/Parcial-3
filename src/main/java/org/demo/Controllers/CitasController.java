package org.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.demo.Models.Cita;
import org.demo.Models.Medico;
import org.demo.Models.Paciente;
import org.demo.Repositories.CitaRepository;
import org.demo.Repositories.MedicoRepository;
import org.demo.Repositories.PacienteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.demo.Utils.AlertHelper.mostrarAlerta;

public class CitasController {

    // --- FORMULARIO ---
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtDocumentoPaciente;
    @FXML private TextField txtTelefonoPaciente;

    @FXML private TextField txtBuscarDocumento;

    @FXML private ComboBox<Paciente> cmbPacientes;
    @FXML private ComboBox<Medico> cmbMedicos;

    @FXML private TextField txtMotivo;
    @FXML private TextArea txtObservaciones;

    @FXML private DatePicker dtFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtPrecio;

    // --- TABLA ---
    @FXML private TableView<Cita> tblCitas;
    @FXML private TableColumn<Cita, Integer> colId;
    @FXML private TableColumn<Cita, String> colFecha;
    @FXML private TableColumn<Cita, String> colHora;
    @FXML private TableColumn<Cita, String> colPaciente;
    @FXML private TableColumn<Cita, String> colMedico;
    @FXML private TableColumn<Cita, Double> colPrecio;
    @FXML private TableColumn<Cita, String> colMotivo;

    // --- REPOS ---
    private final CitaRepository citaRepository = CitaRepository.getInstancia();
    private final MedicoRepository medicoRepository = MedicoRepository.getInstancia();
    private final PacienteRepository pacienteRepository = PacienteRepository.getInstancia();

    // idIgnorar para edición de citas (por ahora siempre null)
    private Integer idIgnorar = null;

    @FXML
    public void initialize() {

        // Configurar tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaFormateada"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medicoNombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));

        tblCitas.setItems(citaRepository.getCitas());

        // Pacientes
        cmbPacientes.setItems(pacienteRepository.getPacientes());

        // Listeners para recargar médicos cuando cambie fecha u hora
        dtFecha.valueProperty().addListener((obs, oldV, newV) -> cargarMedicosDisponibles());
        txtHora.textProperty().addListener((obs, oldV, newV) -> cargarMedicosDisponibles());

        cargarMedicosDisponibles();
    }

    // ==========================================================
    //     MÉTODO BUSCAR PACIENTE
    // ==========================================================
    @FXML
    private void onBuscarPaciente() {

        String doc = txtBuscarDocumento.getText().trim();

        if (doc.isEmpty()) {
            mostrarAlerta("Ingrese un número de documento");
            return;
        }

        Optional<Paciente> pacienteOpt = pacienteRepository.buscarPorDocumento(doc);

        if (pacienteOpt.isEmpty()) {
            mostrarAlerta("No existe un paciente con ese documento");
            return;
        }

        Paciente paciente = pacienteOpt.get();

        // Llenar formulario
        txtNombrePaciente.setText(paciente.getNombre());
        txtDocumentoPaciente.setText(paciente.getDocumento());
        txtTelefonoPaciente.setText(paciente.getTelefono());

        cmbPacientes.setValue(paciente);
    }

    // ==========================================================
    //     MÉDICOS DISPONIBLES (con idIgnorar)
    // ==========================================================
    private void cargarMedicosDisponibles() {

        LocalDate fecha = dtFecha.getValue();

        LocalTime hora;
        try {
            hora = LocalTime.parse(txtHora.getText());
        } catch (Exception e) {
            // Hora inválida → mostrar TODOS los médicos
            cmbMedicos.setItems(medicoRepository.getMedicos());
            return;
        }

        if (fecha == null) {
            // Fecha sin seleccionar → mostrar todos
            cmbMedicos.setItems(medicoRepository.getMedicos());
            return;
        }

        // MÉTODO CORRECTO (incluye idIgnorar)
        cmbMedicos.setItems(
                medicoRepository.getMedicosDisponibles(fecha, hora)
        );
    }

    // ==========================================================
    //  GUARDAR CITA
    // ==========================================================
    @FXML
    private void onGuardarCita() {

        if (!validarCampos()) return;

        Paciente paciente = cmbPacientes.getValue();
        Medico medico = cmbMedicos.getValue();
        LocalDate fecha = dtFecha.getValue();
        LocalTime hora = LocalTime.parse(txtHora.getText());

        double precio;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (Exception e) {
            mostrarAlerta("Precio inválido.");
            return;
        }

        // Verificar conflicto de horario en el repositorio
        boolean conflicto = citaRepository.existeCitaEnHorario(medico, fecha, hora, String.valueOf(idIgnorar));

        if (conflicto) {
            mostrarAlerta("El médico ya tiene una cita en ese horario.");
            return;
        }

        Cita nueva = new Cita(
                medico,
                paciente,
                fecha,
                hora,
                precio,
                txtMotivo.getText()
        );

        citaRepository.guardarCita(nueva);

        cargarMedicosDisponibles();
        tblCitas.refresh();
        limpiarCampos();
        mostrarAlerta("Éxito", "Cita registrada correctamente", Alert.AlertType.INFORMATION);
    }


    // ==========================================================
    //  LIMPIAR CAMPOS
    // ==========================================================
    @FXML
    private void onLimpiarCampos() {
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtBuscarDocumento.clear();
        txtNombrePaciente.clear();
        txtDocumentoPaciente.clear();
        txtTelefonoPaciente.clear();

        cmbPacientes.setValue(null);
        cmbMedicos.setValue(null);

        txtMotivo.clear();
        txtObservaciones.clear();

        dtFecha.setValue(null);
        txtHora.clear();
        txtPrecio.clear();

        idIgnorar = null; // Reiniciar cuando se limpia
    }

    // ==========================================================
    //  VALIDACIONES
    // ==========================================================
    private boolean validarCampos() {

        if (cmbPacientes.getValue() == null) {
            mostrarAlerta("Debe seleccionar un paciente");
            return false;
        }
        if (dtFecha.getValue() == null) {
            mostrarAlerta("Debe seleccionar una fecha");
            return false;
        }
        if (txtHora.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar una hora");
            return false;
        }
        if (cmbMedicos.getValue() == null) {
            mostrarAlerta("Debe seleccionar un médico disponible");
            return false;
        }
        if (txtMotivo.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar el motivo de la cita");
            return false;
        }
        if (txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar un precio");
            return false;
        }

        return true;
    }
}
