package diegosneves.github.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TipoDeUsuario {

    @JsonProperty(value = "comum")
    COMUM,
    @JsonProperty(value = "Lojista")
    LOJISTA;

}
