package pe.demo.solicitudes.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.demo.solicitudes.model.Solicitud;
import pe.demo.solicitudes.repo.Repos.ClienteRepo;
import pe.demo.solicitudes.repo.Repos.SolicitudRepo;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    public record SolicitudReq(@NotNull Long clienteId, @NotBlank String producto) {}

    private final SolicitudRepo repo;
    private final ClienteRepo clientes;

    public SolicitudController(SolicitudRepo repo, ClienteRepo clientes) {
        this.repo = repo;
        this.clientes = clientes;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody SolicitudReq r) {
        if (!clientes.existsById(r.clienteId()))
            return ResponseEntity.badRequest().body(Map.of("error", "cliente no existe"));
        Solicitud s = new Solicitud();
        s.setClienteId(r.clienteId());
        s.setProducto(r.producto());
        return ResponseEntity.status(201).body(Map.of("id", repo.save(s).getId()));
    }

    @GetMapping
    public List<Solicitud> listar() { return repo.findTop100ByOrderByIdDesc(); }
}
