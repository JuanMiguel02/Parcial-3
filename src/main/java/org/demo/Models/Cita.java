package org.demo.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa una venta realizada en el sistema.
 * Contiene la información del cliente, producto, cantidad, precio y total,
 * además de generar automáticamente un identificador único y registrar la fecha de la transacción.
 */
public class Cita {

    private String id;
    private Medico medico;
    private Paciente paciente;
    private LocalDate fecha;
    private LocalTime hora;
    private double precio;
    private String motivo;

    public Cita(Medico medico, Paciente paciente, LocalDate fecha, LocalTime hora, double precio, String motivo) {
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.id = generarIdCita();
        this.motivo = motivo;
    }

    public String getId() { return id; }
    public Medico getMedico() { return medico; }
    public Paciente getPaciente() { return paciente; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public double getPrecio() { return precio; }
    public String getMotivo(){return motivo;}

    public void setMedico(Medico medico) { this.medico = medico; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setMotivo(String motivo){this.motivo = motivo; }

    public String getMedicoNombre() {
        return medico.getNombre();
    }

    public String getPacienteNombre() {
        return paciente.getNombre();
    }

    private static String generarIdCita() {
        return "CITA-" + System.currentTimeMillis() + "-" +
                ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getHoraFormateada() {
        return hora.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id='" + id + '\'' +
                ", medico=" + medico +
                ", paciente=" + paciente +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", precio=" + precio +
                '}';
    }
}