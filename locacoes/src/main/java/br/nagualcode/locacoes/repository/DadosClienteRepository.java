package br.nagualcode.locacoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.nagualcode.locacoes.model.DadosCliente;

public interface DadosClienteRepository extends JpaRepository<DadosCliente, Long> {
}