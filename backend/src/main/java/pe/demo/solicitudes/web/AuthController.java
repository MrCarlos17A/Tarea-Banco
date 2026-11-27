package pe.demo.solicitudes.web;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.demo.solicitudes.repo.Repos.UsuarioRepo;
import pe.demo.solicitudes.security.JwtService;

@RestController
@RequestMapping("/api")
public class AuthController {
    public record LoginReq(String email, String password) {}

    private final UsuarioRepo usuarios;
    private final JwtService jwt;
    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepo usuarios, JwtService jwt) {
        this.usuarios = usuarios;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq r) {
        return usuarios.findByEmail(r.email())
                .filter(u -> r.password() != null && enc.matches(r.password(), u.getPwd()))
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of("token", jwt.generar(u.getEmail()))))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "credenciales invalidas")));
    }
}
