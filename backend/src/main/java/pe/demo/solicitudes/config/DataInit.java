package pe.demo.solicitudes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pe.demo.solicitudes.model.Usuario;
import pe.demo.solicitudes.repo.Repos.UsuarioRepo;

@Configuration
public class DataInit {
    @Bean
    CommandLineRunner admin(UsuarioRepo repo, @Value("${app.admin-password}") String pwd) {
        return args -> {
            if (repo.findByEmail("admin@demo.pe").isEmpty())
                repo.save(new Usuario("admin@demo.pe", new BCryptPasswordEncoder().encode(pwd)));
        };
    }
}
