package diegosneves.github.exception;

public class CpfJaCadastradoException extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.CPF_DUPLICADO;
    public CpfJaCadastradoException(String cpf) {
        super(ERRO.mensagemDeErro(cpf));
    }
}
