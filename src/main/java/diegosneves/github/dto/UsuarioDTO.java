package diegosneves.github.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioDTO {

    private String nomeCompleto;
    private String cpf;

}
