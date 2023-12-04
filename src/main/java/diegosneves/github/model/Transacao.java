package diegosneves.github.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}
