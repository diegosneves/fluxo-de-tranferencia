package diegosneves.github.dto;

import diegosneves.github.model.Usuario;
import lombok.*;

/**
 * Objeto de Transferência de Dados representando uma entidade {@link Usuario usuário}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioDTO {

    private String nomeCompleto;
    private String cpf;

}
