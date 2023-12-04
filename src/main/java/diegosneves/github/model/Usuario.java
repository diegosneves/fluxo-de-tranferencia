package diegosneves.github.model;

import diegosneves.github.enums.TipoDeUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "pagador")
    private List<Transacao> transacoesPagas;
    @OneToMany(mappedBy = "recebedor")
    private List<Transacao> transacoesRecebidas;

    {
        this.transacoesPagas = new ArrayList<>();
        this.transacoesRecebidas = new ArrayList<>();
    }

}
