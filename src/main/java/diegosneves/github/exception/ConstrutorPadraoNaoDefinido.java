package diegosneves.github.exception;

public class ConstrutorPadraoNaoDefinido extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.CONTRUTOR_PADRAO_NAO_DEFINIDO;

    public ConstrutorPadraoNaoDefinido(String message) {
        super(ERRO.mensagemDeErro(message));
    }
}
