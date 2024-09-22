package br.nagualcode.clientes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.nagualcode.clientes.model.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    Optional<Cliente> findByTelefone(String telefone);
}
