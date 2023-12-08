package diegosneves.github.exception;

/**
 * Classe de exceção personalizada para mapeamento de objetos.
 *
 * @see RuntimeException
 */
public class MapearObjetoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.FALHA_NO_MAPEAMENTO;

    public MapearObjetoException(String message) {
        super(ERRO.mensagemDeErro(message));
    }

    public MapearObjetoException(String message, Throwable throwable) {
        super(ERRO.mensagemDeErro(message), throwable);
    }

}
