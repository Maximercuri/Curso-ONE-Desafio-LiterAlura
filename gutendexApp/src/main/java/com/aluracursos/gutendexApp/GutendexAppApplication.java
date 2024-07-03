package com.aluracursos.gutendexApp;

import com.aluracursos.gutendexApp.principal.Principal;
import com.aluracursos.gutendexApp.repository.AutoresRepository;
import com.aluracursos.gutendexApp.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GutendexAppApplication implements CommandLineRunner {

	@Autowired
	private LibrosRepository repositoryLibro;
	@Autowired
	private AutoresRepository repositoryAutor;

	public static void main(String[] args)  {
		SpringApplication.run(GutendexAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(repositoryLibro,repositoryAutor);
		principal.menu();

	}

}
