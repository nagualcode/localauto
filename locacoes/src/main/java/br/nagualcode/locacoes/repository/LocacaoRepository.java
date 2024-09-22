package br.nagualcode.locacoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.nagualcode.locacoes.model.Locacao;

import java.util.List;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    List<Locacao> findByClienteId(Long clienteId);

    List<Locacao> findByCarroId(Long carroId);

    List<Locacao> findByPago(boolean pago);
}