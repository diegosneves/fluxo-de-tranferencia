package diegosneves.github.model;

import diegosneves.github.enums.TipoDeUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * A classe Usuario representa um usuário no sistema.
 * Contém informações como CPF, nome completo, CPF, e-mail, senha,
 * tipo de usuário e saldo.
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * Usuario usuario = new Usuario();
 * usuario.setNomeCompleto("John Doe");
 * usuario.setCpf("123456789");
 * usuario.setEmail("john.doe@example.com");
 * usuario.setSenha("password123");
 * usuario.setTipoDeUsuario(TipoDeUsuario.COMUM);
 * usuario.setSaldo(BigDecimal.valueOf(1000));
 * }</pre>
 */
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
    private String senha;
    private TipoDeUsuario tipoDeUsuario;
    private BigDecimal saldo;

}
