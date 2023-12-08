package diegosneves.github.exception;

import diegosneves.github.model.Usuario;

/**
 * Exceção lançada quando um {@link Usuario usuário} com o CPF informado não é encontrado.
 * @see RuntimeException
 */
public class CpfNaoEncontradoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.CPF_USUARIO_NAO_ENCONTRADO;

    public CpfNaoEncontradoException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public CpfNaoEncontradoException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
