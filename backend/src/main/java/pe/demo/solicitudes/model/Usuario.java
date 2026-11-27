package pe.demo.solicitudes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String pwd;

    public Usuario() {}
    public Usuario(String email, String pwd) { this.email = email; this.pwd = pwd; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPwd() { return pwd; }
}
