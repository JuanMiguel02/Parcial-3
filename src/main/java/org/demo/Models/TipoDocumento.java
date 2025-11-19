package org.demo.Models;

public enum TipoDocumento {
    CC("Cédula de Ciudadanía"),
    CE("Cédula de extranjería"),
    PASAPORTE("Pasaporte"),
    REGISTROCIVIL("Registro Civil");

    private final String tipoDocumento;

    TipoDocumento(String tipoDocumento){
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoDocumento(){
        return this.tipoDocumento;
    }

    @Override
    public String toString() {
        return tipoDocumento;
    }
}
