package com.aluracursos.gutendexApp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadosAPI(@JsonAlias("results") List<DatosLibroAPI> librosResultados) {
}
