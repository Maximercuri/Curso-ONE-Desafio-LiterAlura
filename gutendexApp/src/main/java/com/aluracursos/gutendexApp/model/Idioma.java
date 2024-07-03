package com.aluracursos.gutendexApp.model;

import jakarta.persistence.Id;

public enum Idioma {
    SPANISH("es","Español"),
    ENGLISH("en", "Ingles"),
    ITALIAN("it","Italiano"),
    JAPANESE("jp", "Japones"),
    CHINESE("zh", "Chino"),
    GREEK("el", "Griego"),
    PORTUGUESE("pt", "Portugues"),
    FRENCH("fr", "Frances"),
    DEUTSCH("de", "Aleman"),
    KOREAN("ko","Coreano"),
    RUSSIAN("ru","Ruso"),
    NORWEGIAN("no","Noruego");

    private String idiomaLocal;
    private String idiomaUsuario;

    Idioma(String idiomaAPI, String idiomaEspanol) {
        this.idiomaLocal = idiomaAPI;
        this.idiomaUsuario = idiomaEspanol;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaLocal.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma no Soportado: " + text);
    }

    public static Idioma fromEspanol(String text){
        for (Idioma idioma : Idioma.values()){
            if (idioma.idiomaUsuario.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningún idioma fue encontrado con la etiqueta de: " + text);
    }

    @Override
    public String toString(){

        return idiomaUsuario + " (" + idiomaLocal + ")";

    }

}
