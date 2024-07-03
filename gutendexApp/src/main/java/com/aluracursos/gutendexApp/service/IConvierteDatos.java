package com.aluracursos.gutendexApp.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}

