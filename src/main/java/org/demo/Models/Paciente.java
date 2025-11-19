package org.demo.Models;

public class Paciente extends Persona{
    private String fechaNacimiento;
    private String enfermedad;

    public Paciente(String nombre, String documento, String telefono, String direccion, String correo, String fechaNacimiento, String enfermedad) {
        super(nombre, documento, telefono, direccion, correo);
        this.fechaNacimiento = fechaNacimiento;
        this.enfermedad = enfermedad;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Paciente: " +
                getNombre() +
                ", documento: " + getDocumento() +
                ", teléfono: " + getTelefono() +
                ", dirección: " + getDireccion() +
                ", correo: " + getCorreo() + '\n';
    }

}
