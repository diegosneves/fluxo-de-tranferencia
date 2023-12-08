package diegosneves.github.exception;

/**
* Classe de exceção personalizada que representa um erro quando um construtor padrão não está definido em uma classe.
 * @see RuntimeException
 */
public class ConstrutorPadraoNaoDefinido extends RuntimeException {

    public static final ManipuladorDeErro ERRO = ManipuladorDeErro.CONTRUTOR_PADRAO_NAO_DEFINIDO;

    public ConstrutorPadraoNaoDefinido(String message) {
        super(ERRO.mensagemDeErro(message));
    }

    public ConstrutorPadraoNaoDefinido(String message, Throwable e) {
        super(ERRO.mensagemDeErro(message), e);
    }
}
