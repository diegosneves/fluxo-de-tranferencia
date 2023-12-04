package diegosneves.github.exception;

public class EnvioNotificacaoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.ERRO_API_NOTIFICACAO;
    public EnvioNotificacaoException(String url) {
        super(ERRO.mensagemDeErro(url));
    }

    public EnvioNotificacaoException(String url, Throwable e) {
        super(ERRO.mensagemDeErro(url), e);
    }

}
