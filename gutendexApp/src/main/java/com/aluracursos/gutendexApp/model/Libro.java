package com.aluracursos.gutendexApp.model;

import com.aluracursos.gutendexApp.repository.AutoresRepository;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ElementCollection(targetClass = Idioma.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_Idioma", joinColumns = @JoinColumn(name = "libro_id"))
    @Enumerated(EnumType.STRING)
    private List<Idioma> idioma;

    private Integer descargas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "libro_autor",
               joinColumns = @JoinColumn(name = "libro_id"),
               inverseJoinColumns = @JoinColumn(name="autor_id", nullable = false))
    private List<Autor> autores;

    public Libro(DatosLibroAPI datosLibroAPI, List<Autor> autores){
        this.titulo = datosLibroAPI.titulo();
        this.descargas = datosLibroAPI.descargas();
        this.autores = autores;

        for (String idiomaString : datosLibroAPI.idiomas()) {
            this.agregarIdioma(idiomaString);
        }

    }

    public Libro(){
    }

    public void agregarIdioma(String idiomaInput){
        if (this.idioma == null){
            this.idioma = new ArrayList<>();
        }

        this.idioma.add(Idioma.fromString(idiomaInput));

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Idioma> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<Idioma> idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    @Override
    public String toString() {
        return "\n" + "Titulo: " + titulo + '\n' +
                      "Autores: " + autores.toString() + "\n" +
                      "Idiomas: " + idioma +  "\n" +
                      "Descargas: " + descargas + "\n";
    }
}
