package diegosneves.github.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa o tipo de {@link diegosneves.github.model.Usuario usuário}.
 */
public enum TipoDeUsuario {

    @JsonProperty(value = "comum")
    COMUM,
    @JsonProperty(value = "lojista")
    LOJISTA;

}
