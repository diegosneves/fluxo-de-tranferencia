package diegosneves.github.exception;

public class CpfNaoEncontradoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.CPF_USUARIO_NAO_ENCONTRADO;

    public CpfNaoEncontradoException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

    public CpfNaoEncontradoException(String valor, Throwable throwable) {
        super(ERRO.mensagemDeErro(valor), throwable);
    }

}
