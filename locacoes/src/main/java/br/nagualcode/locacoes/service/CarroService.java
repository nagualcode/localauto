package br.nagualcode.locacoes.service;

import org.springframework.stereotype.Service;
import br.nagualcode.locacoes.client.CarrosFeignClient;
import br.nagualcode.locacoes.client.CarroDTO;

@Service
public class CarroService {

    private final CarrosFeignClient carroClient;

    public CarroService(CarrosFeignClient carroClient) {
        this.carroClient = carroClient;
    }

    public CarroDTO getCarroById(Long id) {
        return carroClient.getCarroById(id);
    }
}