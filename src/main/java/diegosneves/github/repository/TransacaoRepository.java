package diegosneves.github.repository;

import diegosneves.github.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findTransacaoByPagador_Cpf(String cpf);


}
