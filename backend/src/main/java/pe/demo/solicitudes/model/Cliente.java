package pe.demo.solicitudes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String doc;
    private String email;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDoc() { return doc; }
    public void setDoc(String doc) { this.doc = doc; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
