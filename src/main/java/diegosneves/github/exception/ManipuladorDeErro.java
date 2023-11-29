package diegosneves.github.exception;

public enum ManipuladorDeErro {

    USUARIO_NAO_ENCONTRADO("O usuário de CPF [ %s ] não existe."),
    FALHA_NO_MAPEAMENTO("Ocorreu um erro ao tentar mapear a classe [ %s ]."),
    CONTRUTOR_PADRAO_NAO_DEFINIDO("Classe [ %s ] deve declarar um construtor padrão.");

    private final String mensagem;

    ManipuladorDeErro(String mensagem) {
        this.mensagem = mensagem;
    }

    public String mensagemDeErro(String valor) {
        return String.format(this.mensagem, valor);
    }

}
