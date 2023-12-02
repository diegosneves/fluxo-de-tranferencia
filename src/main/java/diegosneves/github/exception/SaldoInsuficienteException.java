package diegosneves.github.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.SALDO_INSUFICIENTE;

    public SaldoInsuficienteException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public SaldoInsuficienteException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
