package org.demo.Models;

public abstract class Persona {
    private static int contador = 1;
    private int id;
    private String nombre;
    private TipoDocumento tipoDocumento;
    private String numDocumento;
    private String telefono;
    private String direccion;
    private String correo;

    public Persona(String nombre, TipoDocumento documento, String numDocumento, String telefono, String direccion, String correo) {
        this.id = contador++;
        this.tipoDocumento = documento;
        this.numDocumento = numDocumento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Persona(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Persona.contador = contador;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public String getTipoDocumentoFormateado(){
        return tipoDocumento.getTipoDocumento();
    }

    public void setTipoDocumento(TipoDocumento tipoDocumentoo) {
        this.tipoDocumento = tipoDocumentoo;
    }

    @Override
    public String toString() {
        return " " +
                  nombre +
                ", numDocumento: " + numDocumento +
                ", teléfono: " + telefono +
                ", dirección: " + direccion +
                ", correo: " + correo + "\n"
                ;
    }
}
