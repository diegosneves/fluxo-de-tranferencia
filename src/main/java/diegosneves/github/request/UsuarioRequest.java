package diegosneves.github.request;

import diegosneves.github.enums.TipoDeUsuario;
import lombok.*;

import java.math.BigDecimal;


/**
 * A classe UsuarioRequest representa o objeto de solicitação de criação de um novo {@link diegosneves.github.model.Usuario usuário}.
 *
 * <p>Exemplos de uso:</p>
 * <pre>{@code
 * UsuarioRequest request = UsuarioRequest.builder()
 *             .cpf("23496374250")
 *             .email("teste@teste.com.br")
 *             .nomeCompleto("Fulano da Silva")
 *             .senha("Teste@123")
 *             .tipoDeUsuario(TipoDeUsuario.LOJISTA)
 *             .saldo(BigDecimal.valueOf(200.0))
 *             .build();
 * }</pre>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioRequest {

    private String nomeCompleto;
    private String cpf;
    private String email;
    private String senha;
    private TipoDeUsuario tipoDeUsuario;
    private BigDecimal saldo;

}
