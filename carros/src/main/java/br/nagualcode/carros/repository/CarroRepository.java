package br.nagualcode.carros.repository;

import br.nagualcode.carros.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {
    List<Carro> findByModeloContainingIgnoreCase(String modelo);

    List<Carro> findByMarcaContainingIgnoreCase(String marca);

    List<Carro> findByPlaca(String placa);
}
