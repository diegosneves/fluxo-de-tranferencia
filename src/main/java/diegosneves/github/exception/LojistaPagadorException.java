package diegosneves.github.exception;

public class LojistaPagadorException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.LOJISTA_PAGADOR;

    public LojistaPagadorException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public LojistaPagadorException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
