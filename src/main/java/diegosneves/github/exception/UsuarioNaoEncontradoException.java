package diegosneves.github.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.USUARIO_NAO_ENCONTRADO;

    public UsuarioNaoEncontradoException(String valor) {
        super(ERRO.mensagemDeErro(valor));
    }

}
