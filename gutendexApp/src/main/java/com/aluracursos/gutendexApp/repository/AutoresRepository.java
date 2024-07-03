package com.aluracursos.gutendexApp.repository;

import com.aluracursos.gutendexApp.model.Autor;
import com.aluracursos.gutendexApp.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoresRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    List<Autor> findByFallecimientoGreaterThanAndNacimientoLessThanEqual(Integer anioMax, Integer anioMin);
}
