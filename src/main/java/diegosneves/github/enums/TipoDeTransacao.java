package diegosneves.github.enums;

/**
 * Enum representando o tipo de transação.
 */
public enum TipoDeTransacao {

    ENVIADA("Transação enviada para o CPF [ %s ]!"),
    RECEBIDA("Transação recebida do CPF [ %s ]!");

    private final String mensagem;

    TipoDeTransacao(String mensagem) {
        this.mensagem = mensagem;
    }

    public String enviar(String cpf) {
        return String.format(this.mensagem, cpf);
    }

}
