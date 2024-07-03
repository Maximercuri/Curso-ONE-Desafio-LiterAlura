package com.aluracursos.gutendexApp.repository;

import com.aluracursos.gutendexApp.model.Autor;
import com.aluracursos.gutendexApp.model.Idioma;
import com.aluracursos.gutendexApp.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibrosRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloIgnoreCase(String titulo);

    @Query("SELECT libro FROM Libro libro LEFT JOIN FETCH libro.autores")
    List<Libro> encontrarTodoIncluyendoAutores();

    @Query("SELECT l FROM Libro l JOIN l.idioma i WHERE i = :idioma")
    List<Libro> findByIdioma(@Param("idioma") Idioma idioma);

}
