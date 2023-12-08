package diegosneves.github.exception;

import diegosneves.github.model.Usuario;

/**
 * A classe SaldoInsuficienteException representa uma exceção que é lançada
 * quando um {@link Usuario usuário} não possui saldo suficiente para uma determinada operação.
 *
 * Esta exceção estende a classe {@link RuntimeException}.
 *
 * @see RuntimeException
 */
public class SaldoInsuficienteException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.SALDO_INSUFICIENTE;

    public SaldoInsuficienteException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public SaldoInsuficienteException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
