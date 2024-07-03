package com.aluracursos.gutendexApp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibroAPI(@JsonAlias("title") String titulo,
                            @JsonAlias("authors") List<DatosAutorAPI> autores,
                            @JsonAlias("languages") List<String> idiomas,
                            @JsonAlias("download_count") Integer descargas) {
}
