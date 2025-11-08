package org.demo.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.demo.Models.Venta;

public class VentaRepository {
    private static VentaRepository instancia;
    private final ObservableList<Venta> ventas;

    private VentaRepository(){
        ventas = FXCollections.observableArrayList();
    }

    public static VentaRepository getInstancia(){
        if(instancia == null){
            instancia = new VentaRepository();
        }
        return instancia;
    }

    public ObservableList<Venta> getVentas() {
        return ventas;
    }

    public void guardarVenta(Venta venta){
        ventas.add(venta);
    }

    private void cargarDatosEjemplo(){

    }
}
