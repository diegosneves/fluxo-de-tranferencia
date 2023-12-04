package diegosneves.github.exception;

public class MapearObjetoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.FALHA_NO_MAPEAMENTO;

    public MapearObjetoException(String message) {
        super(ERRO.mensagemDeErro(message));
    }

    public MapearObjetoException(String message, Throwable throwable) {
        super(ERRO.mensagemDeErro(message), throwable);
    }

}
