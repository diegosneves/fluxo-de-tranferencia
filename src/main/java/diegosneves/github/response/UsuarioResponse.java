package diegosneves.github.response;

import diegosneves.github.enums.TipoDeUsuario;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioResponse {

    private Long id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private TipoDeUsuario tipoDeUsuario;
    private BigDecimal saldo;

}
