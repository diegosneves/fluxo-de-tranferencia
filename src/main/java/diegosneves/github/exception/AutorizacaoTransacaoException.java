package diegosneves.github.exception;

/**
 * Esta classe representa uma exceção lançada quando uma {@link diegosneves.github.model.Transacao transação} não é autorizada.
 * @see RuntimeException
 */
public class AutorizacaoTransacaoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.TRANSACAO_NAO_AUTORIZADA;

    public AutorizacaoTransacaoException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public AutorizacaoTransacaoException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
