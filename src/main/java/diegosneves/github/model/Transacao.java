package diegosneves.github.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A classe Transacao representa uma transação financeira entre dois {@link Usuario usuários}.
 * Contém informações sobre o ID da transação, {@link Usuario pagador}, {@link Usuario beneficiário}, valor da transação,
 * data da transação e {@link diegosneves.github.enums.HashEncoder hash} da transação.
 */
@Entity(name = "transacoes")
@Table(name = "transacoes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pagador_id", nullable = false)
    private Usuario pagador;

    @ManyToOne
    @JoinColumn(name = "recebedor_id", nullable = false)
    private Usuario recebedor;

    @Column(name = "valor_transacao", nullable = false)
    private BigDecimal valorTransacao;

    @Column(name = "data_transacao", nullable = false)
    private LocalDateTime dataTransacao;

    @Column(name = "hash_transacao", nullable = false)
    private String hashTransacao;

}
