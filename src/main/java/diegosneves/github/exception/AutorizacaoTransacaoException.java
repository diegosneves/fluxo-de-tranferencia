package diegosneves.github.exception;

public class AutorizacaoTransacaoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.TRANSACAO_NAO_AUTORIZADA;

    public AutorizacaoTransacaoException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public AutorizacaoTransacaoException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
