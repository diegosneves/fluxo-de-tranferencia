package diegosneves.github.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    private static final ManipuladorDeErro MENSAGEM_ERRO = ManipuladorDeErro.USUARIO_NAO_ENCONTRADO;

    public UsuarioNaoEncontradoException(String valor) {
        super(MENSAGEM_ERRO.mensagemDeErro(valor));
    }

}
