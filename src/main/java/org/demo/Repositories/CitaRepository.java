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
    public void eliminarCita(String idCita) {
        citas.removeIf(c -> c.getId().equals(idCita));
    }

    /**
     * Busca una cita por su ID.
     */
    public Cita buscarCitaPorId(String idCita) {
        return citas.stream()
                .filter(c -> c.getId().equals(idCita))
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica horario pero ignorando una cita por ID (útil para actualizar).
     */
    public boolean existeCitaEnHorario(Medico medico, LocalDate fecha, LocalTime hora, String idIgnorar) {
        return citas.stream().anyMatch(c ->
                (idIgnorar == null || !c.getId().equals(idIgnorar)) &&
                        c.getMedico().getId() == medico.getId() &&
                        c.getFecha().equals(fecha) &&
                        c.getHora().equals(hora)
        );
    }


    /**
     * Carga datos de ejemplo usando Médicos y Pacientes.
     */
    private void cargarDatosEjemplo() {

//        // Pacientes de ejemplo
//        Paciente pa1 = new Paciente("Julian Casablancas", "2131231", "312312", "Armenia", "julian@gmail.com");
//        Paciente pa2 = new Paciente("Jonathan Davis", "213532", "31253212", "Armenia", "jonathan@gmail.com");
//
//        // Médicos de ejemplo
//        Medico m1 = new Medico("Dr. Carlos López", "101234", "3101234567", "Armenia",
//                "carlos@hospital.com", "Cardiología", "Consultorio 12", "8am - 2pm");
//
//        Medico m2 = new Medico("Dra. María Pérez", "202345", "3119876543", "Armenia",
//                "maria@hospital.com", "Pediatría", "Consultorio 5", "9am - 4pm");
//
//        // Citas de ejemplo
//        Cita c1 = new Cita(m1, pa1, 150000, LocalDateTime.of(2025, 1, 20, 10, 0));
//        Cita c2 = new Cita(m2, pa2, 180000, LocalDateTime.of(2025, 1, 21, 14, 30));
//
//        guardarCita(c1);
//        guardarCita(c2);
    }
}
