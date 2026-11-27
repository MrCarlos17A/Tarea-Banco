package pe.demo.solicitudes.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.demo.solicitudes.model.Cliente;
import pe.demo.solicitudes.repo.Repos.ClienteRepo;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    public record ClienteReq(@NotBlank String nombre, @NotBlank String doc, String email) {}

    private final ClienteRepo repo;

    public ClienteController(ClienteRepo repo) { this.repo = repo; }

    @GetMapping
    public List<Cliente> listar() {
        return repo.findAllByOrderByIdDesc();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ClienteReq r) {
        Cliente c = new Cliente();
        c.setNombre(r.nombre());
        c.setDoc(r.doc());
        c.setEmail(r.email());
        return ResponseEntity.status(201).body(Map.of("id", repo.save(c).getId()));
    }
}
