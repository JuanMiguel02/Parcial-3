package org.demo.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.demo.Models.Medico;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Repositorio encargado de gestionar los médicos registrados en el sistema.
 * Implementa el patrón Singleton para garantizar una única instancia compartida.
 * Utiliza una lista observable para permitir la sincronización con la interfaz gráfica.
 */
public class MedicoRepository {
    private static MedicoRepository instancia;
    private final ObservableList<Medico> medicos;

    /**
     * Constructor privado.
     * Inicializa la lista de médicos y carga datos de ejemplo.
     */
    private MedicoRepository(){
        medicos = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    /**
     * Devuelve la instancia única del repositorio.
     */
    public static MedicoRepository getInstancia(){
        if(instancia == null){
            instancia = new MedicoRepository();
        }
        return instancia;
    }

    /**
     * Retorna la lista observable de médicos.
     */
    public ObservableList<Medico> getMedicos(){
        return medicos;
    }

    /**
     * Guarda un nuevo médico en el repositorio.
     * Lanza excepción si documento o correo ya existen.
     */
    public void guardarMedico(Medico medico){
        if(existeMedicoConDocumento(medico.getDocumento()) ||
                existeMedicoConCorreo(medico.getCorreo())) {

            throw new RuntimeException("Ya existe un médico registrado con este documento o correo");
        }
        medicos.add(medico);
    }

    /**
     * Elimina un médico según su correo.
     */
    public void eliminarMedico(Medico medico){
        medicos.removeIf(m -> m.getCorreo().equalsIgnoreCase(medico.getCorreo()));
    }

    /**
     * Actualiza un médico existente basado en su ID.
     */
    public void actualizarMedico(Medico medico){
        Optional<Medico> medicoExistenteOpt = buscarMedicoPorId(medico.getId());
        if(medicoExistenteOpt.isPresent()){
            Medico existente = medicoExistenteOpt.get();

            existente.setNombre(medico.getNombre());
            existente.setDocumento(medico.getDocumento());
            existente.setTelefono(medico.getTelefono());
            existente.setDireccion(medico.getDireccion());
            existente.setCorreo(medico.getCorreo());
            existente.setEspecialidad(medico.getEspecialidad());
            existente.setConsultorio(medico.getConsultorio());
            existente.setHorario(medico.getHorario());
        }
    }

    /**
     * Verifica si existe un médico con un documento determinado.
     */
    public boolean existeMedicoConDocumento(String documento){
        return medicos.stream()
                .anyMatch(m -> m.getDocumento().equalsIgnoreCase(documento.trim()));
    }

    /**
     * Verifica si existe un médico con un correo determinado.
     */
    public boolean existeMedicoConCorreo(String correo){
        return medicos.stream()
                .anyMatch(m -> m.getCorreo().equalsIgnoreCase(correo.trim()));
    }

    /**
     * Busca un médico por ID.
     */
    public Optional<Medico> buscarMedicoPorId(int id){
        return medicos.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
    }

    /**
     * Cantidad total de médicos registrados.
     */
    public int contarMedicos(){
        return medicos.size();
    }

    /**
     * MÉDICOS DISPONIBLES (SIN CITA EN EL MISMO HORARIO)
     */
    public ObservableList<Medico> getMedicosDisponibles(LocalDate fecha, LocalTime hora){
        if (fecha == null || hora == null) {
            // Si no hay fecha/hora seleccionada, devolvemos todos (o podrías devolver FXCollections.emptyObservableList())
            return FXCollections.unmodifiableObservableList(medicos);
        }

        return medicos.filtered(m ->
                !CitaRepository.getInstancia().existeCitaEnHorario(m, fecha, hora, null)
        );
    }

    /**
     * Carga médicos de ejemplo.
     */
    private void cargarDatosEjemplo(){
        Medico m1 = new Medico(
                "Carlos López", "101234", "3101234567",
                "Armenia", "carlos@hospital.com",
                "Cardiología", "Consultorio 12"
        );

        Medico m2 = new Medico(
                "María Pérez", "202345", "3119876543",
                "Armenia", "maria@hospital.com",
                "Pediatría", "Consultorio 5"
        );

        Medico m3 = new Medico(
                "Juan Gómez", "303456", "3107654321",
                "Armenia", "juan@hospital.com",
                "Neurología", "Consultorio 8"
        );

        guardarMedico(m1);
        guardarMedico(m2);
        guardarMedico(m3);
    }
}
