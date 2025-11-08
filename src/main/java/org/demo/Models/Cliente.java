package org.demo.Models;

public class Cliente {
    private static int contador = 1;
    private int id;
    private String nombre;
    private String documento;
    private String telefono;
    private String direccion;
    private String correo;

    public Cliente(String nombre, String documento, String telefono, String direccion, String correo) {
        this.id = contador++;
        this.documento = documento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Cliente(){}

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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    @Override
    public String toString() {
        return "Cliente:" +
                  nombre + '\'' +
                ", documento: " + documento + '\'' +
                ", telefono: " + telefono + '\'' +
                ", direccion: " + direccion + '\'' +
                ", correo: " + correo + "\n"
                ;
    }
}
