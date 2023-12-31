package diegosneves.github.exception;

/**
 * Exceção lançada quando um ID inválido é fornecido.
 */
public class IdInvalidoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.ID_USUARIO_NAO_ENCONTRADO;

    public IdInvalidoException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public IdInvalidoException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
