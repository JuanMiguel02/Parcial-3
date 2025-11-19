package org.demo.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.demo.Models.Paciente;
import org.demo.Models.TipoDocumento;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar los datos de los pacientes registrados en el sistema.
 * Implementa el patrón Singleton para mantener una única instancia compartida
 * y utiliza una lista observable para permitir la sincronización con la interfaz gráfica.
 */
public class PacienteRepository {
    private static PacienteRepository instancia;
    private final ObservableList<Paciente> pacientes;

    /**
     * Constructor privado.
     * Inicializa la lista de pacientes y carga datos de ejemplo.
     */
    private PacienteRepository() {
        pacientes = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    /**
     * Devuelve la instancia única del repositorio.
     *
     * @return instancia única de {@code PacienteRepository}.
     */
    public static PacienteRepository getInstancia(){
        if(instancia == null){
            instancia = new PacienteRepository();
        }
        return instancia;
    }

    /**
     * Retorna la lista observable de pacientes.
     *
     * @return lista observable de pacientes.
     */
    public ObservableList<Paciente> getPacientes() {
        return pacientes;
    }

    /**
     * Guarda un nuevo paciente en el repositorio.
     * Lanza una excepción si el documento o correo ya están registrados.
     *
     * @param paciente paciente a registrar.
     */
    public void guardarPaciente(Paciente paciente){
        if(existePacienteConDocumento(paciente.getNumDocumento()) ||
                existePacienteConCorreo(paciente.getCorreo())){
            throw new RuntimeException("Este paciente ya se encuentra registrado");
        }
        pacientes.add(paciente);
    }

    /**
     * Elimina un paciente existente identificado por su correo electrónico.
     *
     * @param paciente paciente a eliminar.
     */
    public void eliminarPaciente(Paciente paciente){
        pacientes.removeIf(p -> p.getCorreo().equalsIgnoreCase(paciente.getCorreo()));
    }

    /**
     * Actualiza los datos de un paciente existente basándose en su ID.
     *
     * @param paciente paciente con información actualizada.
     */
    public void actualizarPaciente(Paciente paciente){
        Optional<Paciente> pacienteExistenteOpt = buscarPacientePorId(paciente.getId());
        if(pacienteExistenteOpt.isPresent()){
            Paciente existente = pacienteExistenteOpt.get();

            existente.setNumDocumento(paciente.getNumDocumento());
            existente.setCorreo(paciente.getCorreo());
            existente.setNombre(paciente.getNombre());
            existente.setTelefono(paciente.getTelefono());
            existente.setDireccion(paciente.getDireccion());
        }
    }

    /**
     * Verifica si existe un paciente registrado con un documento determinado.
     */
    public boolean existePacienteConDocumento(String documento){
        return pacientes.stream()
                .anyMatch(p -> p.getNumDocumento().equalsIgnoreCase(documento.trim()));
    }

    /**
     * Verifica si existe un paciente registrado con un correo determinado.
     */
    public boolean existePacienteConCorreo(String correo){
        return pacientes.stream()
                .anyMatch(p -> p.getCorreo().equalsIgnoreCase(correo.trim()));
    }

    /**
     * Verifica si existe un paciente registrado con un teléfono determinado.
     */
    public boolean existePacienteConTelefono(String telefono){
        return pacientes.stream()
                .anyMatch(p -> p.getTelefono().equalsIgnoreCase(telefono.trim()));
    }

    /**
     * Busca un paciente por su ID.
     */
    public Optional<Paciente> buscarPacientePorId(int id) {
        return pacientes.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public Optional<Paciente> buscarPorDocumento(String documento){
        return pacientes.stream()
                .filter(p -> p.getNumDocumento().equalsIgnoreCase(documento))
                .findFirst();
    }

    /**
     * Carga datos de ejemplo al iniciar el repositorio.
     */
    private void cargarDatosEjemplo(){

        Paciente p3 = new Paciente("Chino Moreno", TipoDocumento.CC,"42142132", "31241241", "Armenia", "chino@gmail.com", "08/06/2004", "Dolor de cabeza");

        guardarPaciente(p3);
    }
}
