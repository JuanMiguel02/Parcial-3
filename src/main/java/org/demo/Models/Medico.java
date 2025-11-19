package org.demo.Models;

public class Medico extends Persona{
    private String especialidad;
    private String consultorio;
    private String horario;


    public Medico(String nombre, TipoDocumento tipoDocumento, String documento, String telefono, String direccion, String correo, String especialidad, String consultorio){
        super(nombre, tipoDocumento, documento, telefono, direccion, correo);
        this.consultorio = consultorio;
        this.especialidad = especialidad;
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

    @Override
    public String toString() {
        return "Medico: " + super.toString() +
                "especialidad:'" + especialidad + '\'' +
                ", consultorio:'" + consultorio + '\'' + "\n";
    }
}

