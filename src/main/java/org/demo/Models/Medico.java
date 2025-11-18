package org.demo.Models;

public class Medico extends Persona{
    private String especialidad;
    private String consultorio;
    private String horario;
    private boolean disponible;

    public Medico(String nombre, String documento, String telefono, String direccion, String correo, String especialidad, String consultorio, String horario){
        super(nombre, documento, telefono, direccion, correo);
        this.consultorio = consultorio;
        this.especialidad = especialidad;
        this.horario = horario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}

