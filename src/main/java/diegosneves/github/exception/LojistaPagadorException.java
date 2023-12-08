package diegosneves.github.exception;

/**
 * Esta classe representa uma exceção que é lançada quando um {@link diegosneves.github.model.Usuario Lojista} tenta realizar um pagamento.
 *
 * @see RuntimeException
 */
public class LojistaPagadorException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.LOJISTA_PAGADOR;

    public LojistaPagadorException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public LojistaPagadorException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
