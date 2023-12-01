package diegosneves.github.exception;

public class ServicoAutorizadorException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.ERRO_API_AUTORIZADOR;
    public ServicoAutorizadorException(String url) {
        super(ERRO.mensagemDeErro(url));
    }

    public ServicoAutorizadorException(String url, Throwable e) {
        super(ERRO.mensagemDeErro(url), e);
    }

}
