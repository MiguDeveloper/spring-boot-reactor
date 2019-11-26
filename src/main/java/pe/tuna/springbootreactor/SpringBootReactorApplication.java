package pe.tuna.springbootreactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.tuna.springbootreactor.models.Usuario;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Miguel chinchay");
        usuariosList.add("Pedro Fulano");
        usuariosList.add("Maria Sultano");
        usuariosList.add("Diego Mengano");
        usuariosList.add("Juan fulano");
        usuariosList.add("Bruce Lee");
        usuariosList.add("Bruce willis");

        // Original
        // Podemos usar la declaracion de metodo y hacer el print mas corto e.g.: System.out::println
        // Flux<String> nombres = Flux.just("Miguel chinchay", "Pedro Fulano", "Maria Sultano", "Diego Mengano", "Juan fulano", "Bruce Lee", "Bruce willis");

        // Desde un List
        Flux<String> nombres = Flux.fromIterable(usuariosList);

        Flux<Usuario> usuarios = nombres.map(nombre -> {
                    return new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase());
                })
                .filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Nombres no puede estar vacio");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        // Este es el observador: OJO tambien podemos usa la declaracion de método de java 8: log::info
        usuarios.subscribe(elemento -> log.info(elemento.toString()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecucion del observable con éxito");
                    }
                });

    }
}
