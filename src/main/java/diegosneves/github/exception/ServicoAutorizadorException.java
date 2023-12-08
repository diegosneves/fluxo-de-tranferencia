package diegosneves.github.exception;

/**
 * Classe de exceção customizada que representa uma exceção lançada pelo ServicoAutorizador(Serviço Externo).
 * Estende a classe {@link RuntimeException}.
 */
public class ServicoAutorizadorException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.ERRO_API_AUTORIZADOR;
    public ServicoAutorizadorException(String url) {
        super(ERRO.mensagemDeErro(url));
    }

    public ServicoAutorizadorException(String url, Throwable e) {
        super(ERRO.mensagemDeErro(url), e);
    }

}
