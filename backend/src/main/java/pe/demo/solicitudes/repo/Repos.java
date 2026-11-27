package pe.demo.solicitudes.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.demo.solicitudes.model.*;

public final class Repos {
    private Repos() {}

    public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
        Optional<Usuario> findByEmail(String email);
    }
    public interface ClienteRepo extends JpaRepository<Cliente, Long> {
        List<Cliente> findAllByOrderByIdDesc();
    }
    public interface SolicitudRepo extends JpaRepository<Solicitud, Long> {
        List<Solicitud> findTop100ByOrderByIdDesc();
    }
}
