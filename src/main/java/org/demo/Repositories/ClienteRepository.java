package org.demo.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.demo.Models.Cliente;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar los datos de los clientes registrados en el sistema.
 * Implementa el patrón Singleton para mantener una única instancia compartida
 * y utiliza una lista observable para permitir la sincronización con la interfaz gráfica.
 */
public class ClienteRepository {
    private static ClienteRepository instancia;
    private final ObservableList<Cliente> clientes;

    /**
     * Constructor privado.
     * Inicializa la lista de clientes y carga datos de ejemplo.
     */
    private ClienteRepository() {
        clientes = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    /**
     * Devuelve la instancia única del repositorio.
     * Si no existe, se crea una nueva.
     *
     * @return instancia única de {@code ClienteRepository}.
     */
    public static ClienteRepository getInstancia(){
        if(instancia == null){
            instancia = new ClienteRepository();
        }
        return instancia;
    }

    /**
     * Retorna la lista observable de clientes.
     * Puede ser utilizada para enlazar datos con componentes de la interfaz gráfica.
     *
     * @return lista observable de clientes.
     */
    public ObservableList<Cliente> getClientes() {
        return clientes;
    }

    /**
     * Guarda un nuevo cliente en el repositorio.
     * Lanza una excepción si el documento o correo ya están registrados.
     *
     * @param cliente cliente a registrar.
     */
    public void guardarCliente(Cliente cliente){
        if(existeClienteConDocumento(cliente.getDocumento()) || existeClienteConCorreo(cliente.getCorreo())){
            throw new RuntimeException("Este cliente ya se encuentra registrado");
        }
        clientes.add(cliente);
    }

    /**
     * Elimina un cliente existente identificado por su correo electrónico.
     *
     * @param cliente cliente a eliminar.
     */
    public void eliminarCliente(Cliente cliente){
        clientes.removeIf(c -> c.getCorreo().equalsIgnoreCase(cliente.getCorreo()));
    }

    /**
     * Actualiza los datos de un cliente existente basándose en su ID.
     * Si el cliente existe, sus datos son reemplazados por los nuevos valores.
     *
     * @param cliente cliente con la información actualizada.
     */
    public void actualizarCliente(Cliente cliente){
        Optional<Cliente> clienteExistenteOpt = buscarClientePorId(cliente.getId());
        if(clienteExistenteOpt.isPresent()){
            Cliente existente =  clienteExistenteOpt.get();

            existente.setDocumento(cliente.getDocumento());
            existente.setCorreo(cliente.getCorreo());
            existente.setNombre(cliente.getNombre());
            existente.setTelefono(cliente.getTelefono());
            existente.setDireccion(cliente.getDireccion());
        }
    }

    /**
     * Verifica si existe un cliente registrado con un documento determinado.
     *
     * @param documento documento a verificar.
     * @return {@code true} si el cliente ya existe, {@code false} en caso contrario.
     */
    public boolean existeClienteConDocumento(String documento){
        return clientes.stream().
                anyMatch(c -> c.getDocumento().equalsIgnoreCase(documento.trim()));
    }

    /**
     * Verifica si existe un cliente registrado con un correo determinado.
     *
     * @param correo correo a verificar.
     * @return {@code true} si el cliente ya existe, {@code false} en caso contrario.
     */
    public boolean existeClienteConCorreo(String correo){
        return clientes.stream().
                anyMatch(c -> c.getCorreo().equalsIgnoreCase(correo.trim()));
    }

    /**
     * Verifica si existe un cliente registrado con un teléfono determinado.
     *
     * @param telefono número de teléfono a verificar.
     * @return {@code true} si el cliente ya existe, {@code false} en caso contrario.
     */
    public boolean existeClienteConTelefono(String telefono){
        return clientes.stream().
                anyMatch(c -> c.getTelefono().equalsIgnoreCase(telefono.trim()));
    }

    /**
     * Busca un cliente por su identificador único.
     *
     * @param id identificador del cliente.
     * @return un {@code Optional<Cliente>} con el cliente si se encuentra.
     */
    public Optional<Cliente> buscarClientePorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    /**
     * Retorna el número total de clientes registrados.
     *
     * @return cantidad de clientes en el repositorio.
     */
    public int contarClientes(){
        return clientes.size();
    }

    /**
     * Carga datos de ejemplo al iniciar el repositorio.
     * Los datos son utilizados únicamente con fines demostrativos.
     */
    private void cargarDatosEjemplo(){
        Cliente c1 = new Cliente("Simón Bolívar", "1092313", "3142141", "Armenia", "simon@gmail.com");
        Cliente c2 = new Cliente("Armando Casas", "10924213", "3144541", "Armenia", "casas@gmail.com");
        Cliente c3 = new Cliente("Chino Moreno", "42142132", "31241241", "Armenia", "chino@gmail.com");

        guardarCliente(c1);
        guardarCliente(c2);
        guardarCliente(c3);
    }
}
