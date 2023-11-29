package diegosneves.github.exception;

public class MapearObjetoException extends RuntimeException {

    private static final ManipuladorDeErro MENSAGEM_ERRO = ManipuladorDeErro.FALHA_NO_MAPEAMENTO;

    public MapearObjetoException(String message) {
        super(MENSAGEM_ERRO.mensagemDeErro(message));
    }
}
