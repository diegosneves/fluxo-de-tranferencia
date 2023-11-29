package diegosneves.github.exception;

public class ConstrutorPadraoNaoDefinido extends RuntimeException {

    private static final ManipuladorDeErro MENSAGEM_ERRO = ManipuladorDeErro.CONTRUTOR_PADRAO_NAO_DEFINIDO;

    public ConstrutorPadraoNaoDefinido(String message) {
        super(MENSAGEM_ERRO.mensagemDeErro(message));
    }
}
