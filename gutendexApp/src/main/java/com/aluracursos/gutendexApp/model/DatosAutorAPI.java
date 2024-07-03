package com.aluracursos.gutendexApp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutorAPI(@JsonAlias("name") String nombre,
                            @JsonAlias("birth_year") Integer AnoDeNacimiento,
                            @JsonAlias("death_year") Integer AnoDeFallecimiento) {

    @Override
    public String toString() {
        if (AnoDeFallecimiento == null){
            return nombre.split(",")[1] + " " + nombre.split(",")[0] + " (" + AnoDeNacimiento + " - ???)";
        } else {
            return nombre.split(",")[1] + " " +  nombre.split(",")[0] + " (" + AnoDeNacimiento + " - " + AnoDeFallecimiento + ')';
        }
    }
}
