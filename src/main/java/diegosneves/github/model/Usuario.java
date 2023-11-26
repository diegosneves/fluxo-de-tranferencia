package diegosneves.github.model;

import diegosneves.github.enums.TipoDeUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "usuarios")
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String nomeCompleto;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
    private TipoDeUsuario tipoDeUsuario;

}
