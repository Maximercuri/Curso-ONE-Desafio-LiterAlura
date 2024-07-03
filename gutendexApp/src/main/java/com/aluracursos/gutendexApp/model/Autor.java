package com.aluracursos.gutendexApp.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    @ManyToMany(mappedBy = "autores",fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor(){
    }

    public Autor(DatosAutorAPI datosAutorAPI){

        this.nombre = datosAutorAPI.nombre().split(",")[1] + " " +  datosAutorAPI.nombre().split(",")[0];
        this.nacimiento = datosAutorAPI.AnoDeNacimiento();
        this.fallecimiento = datosAutorAPI.AnoDeFallecimiento();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getFallecimiento() {
        return fallecimiento;
    }

    public void setFallecimiento(Integer fallecimiento) {
        this.fallecimiento = fallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        if (fallecimiento == null){
        return nombre + " (" + nacimiento + " - ???)";
        } else {
            return nombre + " (" + nacimiento + " - " + fallecimiento + ")";
        }
    }
}
