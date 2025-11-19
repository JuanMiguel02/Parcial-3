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
    @FXML
    private TextField txtNombrePaciente;
    @FXML
    private TextField txtDocumentoPaciente;
    @FXML
    private TextField txtTelefonoPaciente;
    @FXML
    private TextField txtCorreoPaciente;

    @FXML
    private TextField txtBuscarDocumento;

    @FXML
    private ComboBox<Paciente> cmbPacientes;
    @FXML
    private ComboBox<Medico> cmbMedicos;

    @FXML
    private TextField txtMotivo;
    @FXML
    private TextArea txtObservaciones;

    @FXML
    private DatePicker dtFecha;
    @FXML
    private TextField txtHora;
    @FXML
    private TextField txtPrecio;

    // --- TABLA ---
    @FXML
    private TableView<Cita> tblCitas;
    @FXML
    private TableColumn<Cita, Integer> colId;
    @FXML
    private TableColumn<Cita, String> colFecha;
    @FXML
    private TableColumn<Cita, String> colHora;
    @FXML
    private TableColumn<Cita, String> colPaciente;
    @FXML
    private TableColumn<Cita, String> colMedico;
    @FXML
    private TableColumn<Cita, String> colConsultorio;
    @FXML
    private TableColumn<Cita, Double> colPrecio;
    @FXML
    private TableColumn<Cita, String> colMotivo;

    // --- REPOSITORIOS ---
    private CitaRepository citaRepository;
    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;

    // idCita para edición de citas
    private String idCita = null;

    @FXML
    public void initialize() {
        citaRepository = CitaRepository.getInstancia();
        medicoRepository = MedicoRepository.getInstancia();
        pacienteRepository = PacienteRepository.getInstancia();

        // Configurar tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaFormateada"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("pacienteNombre"));
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medicoNombre"));
        colConsultorio.setCellValueFactory(new PropertyValueFactory<>("consultorio"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));

        tblCitas.setItems(citaRepository.getCitas());

        tblCitas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, nueva) -> {
            if (nueva != null) {
                cargarDatosCita(nueva);
            }
        });

        // Pacientes
        cmbPacientes.setItems(pacienteRepository.getPacientes());
        cmbPacientes.valueProperty().addListener((obs, oldV, newV) -> cargarDatosPaciente(newV));

        // Listeners para recargar médicos cuando cambie fecha u hora
        dtFecha.valueProperty().addListener((obs, oldV, newV) -> cargarMedicosDisponibles());
        txtHora.textProperty().addListener((obs, oldV, newV) -> cargarMedicosDisponibles());

        //siempre carga los medicos disponibles
        cargarMedicosDisponibles();
        configurarValidaciones();
    }

    // ==========================================================
    //   BUSCAR PACIENTE
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
        cargarDatosPaciente(paciente);

        cmbPacientes.setValue(paciente);
    }

    // ==========================================================
    //  ELIMINAR CITA
    // ==========================================================
    @FXML
    private void onEliminarCita() {
        Cita citaSeleccionada = tblCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada == null) {
            mostrarAlerta("Debe seleccionar una cita para eliminar");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText("¿Desea eliminar esta cita?");
        confirm.setContentText("Paciente: " + citaSeleccionada.getPacienteNombre() + " - " + "Medico: " + citaSeleccionada.getMedicoNombre() + " ( " + citaSeleccionada.getHoraFormateada() + " )");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                citaRepository.eliminarCita(citaSeleccionada);
                cargarCitas();
                mostrarAlerta("Éxito", "Cita eliminada", Alert.AlertType.INFORMATION);
            }
        });
    }

    // ==========================================================
    //  ACTUALIZAR CITA
    // ==========================================================
    @FXML
    private void onActualizarCita() {
        Cita citaSeleccionada = tblCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada == null) {
            mostrarAlerta("Debe seleccionar una cita para actualizar");
            return;
        }

        if (validarCampos()) return;

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

        // Validar conflicto de horario (permitiendo ignorar esta cita)
        if (citaRepository.existeCitaEnHorario(medico, fecha, hora, String.valueOf(idCita))) {
            mostrarAlerta("El médico ya tiene una cita en ese horario.");
            return;
        }

        // *** Actualizar valores ***
        citaSeleccionada.setPaciente(paciente);
        citaSeleccionada.setMedico(medico);
        citaSeleccionada.setFecha(fecha);
        citaSeleccionada.setHora(hora);
        citaSeleccionada.setMotivo(txtMotivo.getText());
        citaSeleccionada.setObservaciones(txtObservaciones.getText());
        citaSeleccionada.setPrecio(precio);

        citaRepository.actualizarCita(citaSeleccionada);

        tblCitas.refresh();
        limpiarCampos();

        mostrarAlerta("Éxito", "Cita actualizada correctamente", Alert.AlertType.INFORMATION);
    }


    private void cargarCitas() {
        tblCitas.setItems(citaRepository.getCitas());
    }

    // ==========================================================
    //     MÉDICOS DISPONIBLES
    // ==========================================================
    private void cargarMedicosDisponibles() {

        LocalDate fecha = dtFecha.getValue();

        LocalTime hora;
        try {
            hora = LocalTime.parse(txtHora.getText());
        } catch (Exception e) {
            // Hora inválida  (mostrar TODOS los médicos)
            cmbMedicos.setItems(medicoRepository.getMedicos());
            return;
        }

        if (fecha == null) {
            // Fecha sin seleccionar - mostrar todos
            cmbMedicos.setItems(medicoRepository.getMedicos());
            return;
        }

        cmbMedicos.setItems(
                medicoRepository.getMedicosDisponibles(fecha, hora)
        );
    }

    // ==========================================================
    //  GUARDAR CITA
    // ==========================================================
    @FXML
    private void onGuardarCita() {

        if (validarCampos()) return;

        if (citaRepository.existeCita(idCita)) {
            mostrarAlerta("Esta cita ya está registrada");
            return;
        }

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
        boolean conflicto = citaRepository.existeCitaEnHorario(medico, fecha, hora, String.valueOf(idCita));

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
                txtMotivo.getText(),
                txtObservaciones.getText()
        );

        citaRepository.guardarCita(nueva);

        cargarMedicosDisponibles();
        tblCitas.refresh();
        limpiarCampos();
        mostrarAlerta("Éxito", "Cita registrada correctamente", Alert.AlertType.INFORMATION);
    }

    private void cargarDatosPaciente(Paciente paciente) {
        if (paciente == null) return;

        txtNombrePaciente.setText(paciente.getNombre());
        txtDocumentoPaciente.setText(paciente.getDocumento());
        txtTelefonoPaciente.setText(paciente.getTelefono());
        txtCorreoPaciente.setText(paciente.getCorreo());
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

        idCita = null; // Reiniciar cuando se limpia
    }

    // ==========================================================
    //  VALIDACIONES
    // ==========================================================
    private boolean validarCampos() {

        if (cmbPacientes.getValue() == null) {
            mostrarAlerta("Debe seleccionar un paciente");
            return true;
        }
        if (dtFecha.getValue() == null) {
            mostrarAlerta("Debe seleccionar una fecha");
            return true;
        }
        if (txtHora.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar una hora");
            return true;
        }
        if (cmbMedicos.getValue() == null) {
            mostrarAlerta("Debe seleccionar un médico disponible");
            return true;
        }
        if (txtMotivo.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar el motivo de la cita");
            return true;
        }
        if (txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Debe ingresar un precio");
            return true;
        }

        return false;
    }

    private void cargarDatosCita(Cita c) {
        idCita = String.valueOf(c.getId());

        cmbPacientes.setValue(c.getPaciente());
        cargarDatosPaciente(c.getPaciente());

        dtFecha.setValue(c.getFecha());
        txtHora.setText(c.getHoraFormateada());
        txtMotivo.setText(c.getMotivo());
        txtPrecio.setText(String.valueOf(c.getPrecio()));
        txtObservaciones.setText(c.getObservaciones());
        cmbMedicos.setValue(c.getMedico());
    }

    private void configurarValidaciones() {

        // --- SOLO NÚMEROS PARA DOCUMENTO DE BÚSQUEDA ---
        txtBuscarDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtBuscarDocumento.setText(oldVal);
            }
        });

        // --- HORA FORMATO HH:MM ---
        txtHora.textProperty().addListener((obs, oldVal, newVal) -> {

            // Solo números y dos puntos
            if (!newVal.matches("[0-9:]*")) {
                txtHora.setText(oldVal);
                return;
            }

            // Máximo 5 caracteres → "HH:MM"
            if (newVal.length() > 5) {
                txtHora.setText(oldVal);
                return;
            }

            // Si tiene ":" debe estar en la posición 3 (HH:MM)
            if (newVal.contains(":") && newVal.indexOf(":") != 2) {
                txtHora.setText(oldVal);
            }
        });

        // --- SOLO NÚMEROS Y DECIMALES PARA PRECIO ---
        txtPrecio.textProperty().addListener((obs, oldVal, newVal) -> {

            // Permitir números y un decimal
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                txtPrecio.setText(oldVal);
            }
        });

        // --- SOLO LETRAS PARA MOTIVO ---
        txtMotivo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]*")) {
                txtMotivo.setText(oldVal);
            }
        });

        // --- SOLO LETRAS EN OBSERVACIONES (pero permite comas y puntos) ---
        txtObservaciones.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ0-9.,\\s]*")) {
                txtObservaciones.setText(oldVal);
            }
        });
    }

}
