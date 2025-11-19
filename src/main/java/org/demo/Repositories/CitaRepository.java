package org.demo.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.demo.Models.Cita;
import org.demo.Models.Medico;
import org.demo.Models.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Repositorio encargado de gestionar las citas del sistema.
 * Implementa un patrón Singleton para garantizar una única instancia compartida.
 * Utiliza una lista observable para sincronización con la interfaz gráfica.
 */
public class CitaRepository {

    private static CitaRepository instancia;
    private final ObservableList<Cita> citas;

    /**
     * Constructor privado que inicializa la lista de citas.
     */
    private CitaRepository() {
        citas = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    /**
     * Devuelve la instancia única del repositorio.
     */
    public static CitaRepository getInstancia() {
        if (instancia == null) {
            instancia = new CitaRepository();
        }
        return instancia;
    }

    /**
     * Retorna la lista observable de citas.
     */
    public ObservableList<Cita> getCitas() {
        return citas;
    }

    /**
     * Guarda una nueva cita en el repositorio.
     */
    public void guardarCita(Cita cita) {
        citas.add(cita);
    }

    /**
     * Elimina una cita según su ID.
     */
    public void eliminarCita(Cita cita) {
        citas.remove(cita);
    }

    public void actualizarCita(Cita citaActualizada) {
        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getId().equals(citaActualizada.getId())) {
                citas.set(i, citaActualizada); // reemplazar en la lista observable
                return;
            }
        }
    }

    /**
     * Busca una cita por su ID.
     */
    public boolean existeCite(String idCita) {
        return citas.stream()
                .anyMatch(cita ->  cita.getId().equals(idCita));
    }

    /**
     * Verifica horario pero ignorando una cita por ID (útil para actualizar).
     */
    public boolean existeCitaEnHorario(Medico medico, LocalDate fecha, LocalTime hora, String idIgnorar) {
        return citas.stream().anyMatch(c ->
                (!c.getId().equals(idIgnorar)) &&
                        c.getMedico().getId() == medico.getId() &&
                        c.getFecha().equals(fecha) &&
                        c.getHora().equals(hora)
        );
    }


    /**
     * Carga datos de ejemplo usando Médicos y Pacientes.
     */
    private void cargarDatosEjemplo() {

        // Pacientes de ejemplo
        Paciente pa1 = new Paciente("Julian Casablancas", "2131231", "312312", "Armenia", "julian@gmail.com", "08/30/2000", "Dolor de Cabeza");
        Paciente pa2 = new Paciente("Jonathan Davis", "213532", "31253212", "Armenia", "jonathan@gmail.com", "08/30/2001", "Dolor de Rodilla");
        PacienteRepository.getInstancia().guardarPaciente(pa1);
        PacienteRepository.getInstancia().guardarPaciente(pa2);

        // Médicos de ejemplo
        Medico m1 = new Medico("Dr. Carlos Ramírez", "108654", "3101234967", "Armenia",
                "carlosR@hospital.com", "Cardiología", "Consultorio 12");

        Medico m2 = new Medico("Dra. Lola Mento", "2023458", "3111876543", "Armenia",
                "lola@hospital.com", "Pediatría", "Consultorio 5");

        MedicoRepository.getInstancia().guardarMedico(m1);
        MedicoRepository.getInstancia().guardarMedico(m2);

        // Citas de ejemplo
        Cita c1 = new Cita(m1, pa1, LocalDate.of(2025, 11, 19), LocalTime.of(14, 5), 2000, "El paciente presenta dolor de cabeza", "");
        Cita c2 = new Cita(m2, pa2, LocalDate.of(2025, 11, 19), LocalTime.of(14, 5), 2000, "El paciente presenta dolor de rodilla", "");

        guardarCita(c1);
        guardarCita(c2);
    }
}
