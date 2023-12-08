package diegosneves.github.repository;

import diegosneves.github.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A interface TransacaoRepository é responsável por realizar operações CRUD na entidade {@link Transacao}.
 * Estende a interface {@link JpaRepository}, que fornece métodos genéricos para trabalhar com entidades em um banco de dados.
 */
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findTransacaoByPagador_Cpf(String cpf);

    List<Transacao> findTransacaoByRecebedor_Cpf(String cpf);


}
