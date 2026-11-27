package pe.demo.solicitudes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pe.demo.solicitudes.security.JwtService;

class JwtServiceTest {
    private final JwtService jwt = new JwtService("clave-de-prueba-con-mas-de-32-caracteres!!");

    @Test
    void generaYValidaToken() {
        assertEquals("a@b.pe", jwt.validar(jwt.generar("a@b.pe")));
    }

    @Test
    void rechazaTokenAlterado() {
        String t = jwt.generar("a@b.pe") + "x";
        assertThrows(Exception.class, () -> jwt.validar(t));
    }
}
