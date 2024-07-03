package com.aluracursos.gutendexApp.principal;

import com.aluracursos.gutendexApp.model.*;
import com.aluracursos.gutendexApp.repository.AutoresRepository;
import com.aluracursos.gutendexApp.repository.LibrosRepository;
import com.aluracursos.gutendexApp.service.ConsumoAPI;
import com.aluracursos.gutendexApp.service.ConversorDeDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI busqueda = new ConsumoAPI();
    private ConversorDeDatos conversor = new ConversorDeDatos();
    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    @Autowired
    private LibrosRepository repositorioLibro;

    @Autowired
    private AutoresRepository repositorioAutor;

    @Autowired
    public Principal(LibrosRepository repositoryBook, AutoresRepository repositoryAuthor) {
        this.repositorioLibro = repositoryBook;
        this.repositorioAutor = repositoryAuthor;
    }

    public void menu() {

        var opcion = -1;
        while (opcion != 0) {

            var menu = """
                       ********************************************************************************
                       Ingrese el numero de la opción elegida:
                       
                       1) Buscar un Libro por su Titulo
                       2) Mostrar una Lista de los Libros Registrados en la Base de Datos
                       3) Mostrar una Lista de los Autores Registrados en la Base de Datos
                       4) Buscar Autores de la Base de Datos, Vivos en Determinado Año
                       5) Mostrar una Lista de los Libros Registrados en la Base de Datos Según Idioma
                       
                       0) Salir
                       ********************************************************************************
                       """;

            System.out.println("\n" + menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;

                case 2:
                    mostrarLibrosEnBaseDeDatos();
                    break;

                case 3:
                    mostrarAutoresEnBaseDeDatos();
                    break;

                case 4:
                    mostrarAutoresVivos();
                    break;

                case 5:
                    mostrarLibrosSegunIdioma();
                    break;

                case 0:
                    System.out.println("\n" + "Saliendo del programa.");
                    break;

                default:
                    System.out.println("\n" + "Opción invalida, por favor elige otra.");
            }
        }

        System.out.println("\n" + "Fin del Programa, ¡Hasta Luego!");

    }

    private DatosLibroAPI getDatosLibro(){

        System.out.println("\n" + "Por favor, ingrese el nombre del libro que desea buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = busqueda.obtenerDatosDeAPI(URL_BASE + nombreLibro.replace(" ","%20"));
        var resultados = conversor.obtenerDatos(json, ResultadosAPI.class);

        return resultados.librosResultados().stream()
                .filter(e -> e.titulo().equalsIgnoreCase(nombreLibro) || e.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .max(Comparator.comparing(DatosLibroAPI::descargas))
                .orElse(null);

    }

    private void buscarLibro() {

        var libroAPI = getDatosLibro();

        if (libroAPI == null) {

            System.out.println("\n" + "No se encontró el libro especificado.");
            return;

        }

        Optional<Libro> libroExistente = repositorioLibro.findByTituloIgnoreCase(libroAPI.titulo());

        if (libroExistente.isPresent()){

            System.out.printf("""
                              
                              Libro Encontrado:
                              
                              Titulo: %s
                              Autor/es: %s
                              Idiomas: %s
                              Descargas: %s
                              
                              """, libroAPI.titulo(),
                                   libroAPI.autores(),
                                   libroAPI.idiomas(),
                                   libroAPI.descargas());

            System.out.println("Ya Existe el Libro en la Base de Datos");

        } else {

            System.out.println("\n" + "¡Libro Encontrado! " + "\n");

            guardarEnBaseDeDatos(libroAPI);

        }
    }

    @Transactional
    public void guardarEnBaseDeDatos(DatosLibroAPI datosLibros) {
//        // Verificar si el libro ya existe en la base de datos
//        Optional<Libro> libroExistente = repositorioLibro.findByTituloIgnoreCase(datosLibros.titulo());
//
//        if (libroExistente.isPresent()) {
//            System.out.println("No se puede registrar el libro, ya existe");
//        } else {
//            // Buscar y/o guardar autores
//            List<Autor> autores = datosLibros.autores().stream()
//                    .map(datosAutor -> {
//                        Optional<Autor> autorExistente = repositorioAutor.findByNombre(datosAutor.nombre());
//                        return autorExistente.orElseGet(() -> repositorioAutor.save(new Autor(datosAutor)));
//                    })
//                    .collect(Collectors.toList());
//
//            // Crear el nuevo libro
//            Libro nuevoLibro = new Libro(datosLibros, autores);
//            repositorioLibro.save(nuevoLibro);
//            System.out.println("Libro guardado exitosamente: " + "\n" + nuevoLibro);
//        }

        // Verificar si el libro ya existe en la base de datos

        Optional<Libro> libroExistente = repositorioLibro.findByTituloIgnoreCase(datosLibros.titulo());

        if (libroExistente.isPresent()) {
            System.out.println(libroExistente.get());
            System.out.println("No se puede registrar el libro, ya existe");
        } else {
            List<Autor> autores = datosLibros.autores().stream()
                    .map(datosAutor -> {
                        Optional<Autor> autorExistente = repositorioAutor.findByNombre(datosAutor.nombre());
                        if (autorExistente.isPresent()) {
                            return autorExistente.get();
                        } else {
                            Autor nuevoAutor = new Autor(datosAutor);
                            return repositorioAutor.save(nuevoAutor);
                        }
                    })
                    .collect(Collectors.toList());

            Libro nuevoLibro = new Libro();
            nuevoLibro.setTitulo(datosLibros.titulo());
            nuevoLibro.setAutores(autores);
            nuevoLibro.setIdioma(datosLibros.idiomas().stream()
                    .map(Idioma::fromString)
                    .collect(Collectors.toList()));
            nuevoLibro.setDescargas(datosLibros.descargas());

            repositorioLibro.save(nuevoLibro);

            // Sincronizar la relación bidireccional
            for (Autor autor : autores) {
                if (!autor.getLibros().contains(nuevoLibro)) {
                    autor.getLibros().add(nuevoLibro);
                    repositorioAutor.save(autor); // Actualiza el autor en la base de datos
                }
            }

            System.out.println("Libro guardado exitosamente: " + nuevoLibro);
        }
    }


    private void mostrarLibrosEnBaseDeDatos() {

        List<Libro> librosEnBaseDeDatos = repositorioLibro.encontrarTodoIncluyendoAutores();

        if (librosEnBaseDeDatos.isEmpty()){
            System.out.println("No hay libros Registrados");
        } else {

            librosEnBaseDeDatos.forEach(libro -> System.out.printf("""

                                                               ****** LIBRO ******
                                                               %s
                                                               *******************
                                                               """, libro.toString()));
        }

    }

    private void mostrarAutoresEnBaseDeDatos() {

        List<Autor> autores = repositorioAutor.findAll();

        Map<String, Autor> autorMap = new HashMap<>();

        for (Autor autor : autores) {
            String nombreAutor = autor.getNombre();
            if (autorMap.containsKey(nombreAutor)) {
                Autor autorExistente = autorMap.get(nombreAutor);
                autorExistente.getLibros().addAll(autor.getLibros());
            } else {
                autorMap.put(nombreAutor, autor);
            }
        }

        for (Autor autor : autorMap.values()) {
            System.out.println("\n" + "****** AUTOR ******");
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Año de Nacimiento: " + autor.getNacimiento());
            System.out.println("Año de Fallecimiento: " + autor.getFallecimiento());
            System.out.print("Libros Asociados: ");
            autor.getLibros().forEach(libro -> System.out.print("'" + libro.getTitulo() + "', "));
            System.out.println("\n" + "*******************");
        }
    }

    private void mostrarAutoresVivos() {

        System.out.println("\n" + "Ingrese el Año de Búsqueda Para Hallar los Autores Vivos: ");
        int aniovivo = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autoresVivos = repositorioAutor.findByFallecimientoGreaterThanAndNacimientoLessThanEqual(aniovivo, aniovivo);

        if (autoresVivos.isEmpty()){
            System.out.println("\n" + "No se Hallaron Autores Vivos Registrados en la Base de Datos en ese Año ");
        } else {

            Map<String, Autor> autorMap = new HashMap<>();

            for (Autor autor : autoresVivos) {
                String nombreAutor = autor.getNombre();
                if (autorMap.containsKey(nombreAutor)) {
                    Autor autorExistente = autorMap.get(nombreAutor);
                    autorExistente.getLibros().addAll(autor.getLibros());
                } else {
                    autorMap.put(nombreAutor, autor);
                }
            }

            for (Autor autor : autorMap.values()) {
                System.out.println("\n" + "****** AUTOR ******");
                System.out.println("Nombre: " + autor.getNombre());
                System.out.println("Año de Nacimiento: " + autor.getNacimiento());
                System.out.println("Año de Fallecimiento: " + autor.getFallecimiento());
                System.out.print("Libros Asociados: ");
                autor.getLibros().forEach(libro -> System.out.print("'" + libro.getTitulo() + "', "));
                System.out.println("\n*******************");

            }

        }

    }

    private void mostrarLibrosSegunIdioma() {

        System.out.println("\n" + "Ingrese el idioma de búsqueda: ");
        var idiomaBuscado = teclado.nextLine().trim();

        Idioma idioma;
        try {
            idioma = Idioma.fromEspanol(idiomaBuscado);
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no válido.");
            return;
        }

        // Buscar libros que contienen el idioma especificado
        List<Libro> libros = repositorioLibro.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            libros.forEach(libro -> {
                System.out.printf("""
                              
                              ****** LIBRO ******
                              Titulo: %s
                              Idiomas: %s
                              Descargas: %d
                              *******************
                              
                              """,
                        libro.getTitulo(),
                        libro.getIdioma(),
                        libro.getDescargas());
            });
        }
    }


}
