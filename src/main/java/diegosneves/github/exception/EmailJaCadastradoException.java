package diegosneves.github.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.EMAIL_DUPLICADO;
    public EmailJaCadastradoException(String email) {
        super(ERRO.mensagemDeErro(email));
    }
}
