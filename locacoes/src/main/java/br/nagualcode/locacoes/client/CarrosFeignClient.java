package br.nagualcode.locacoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "carros", url = "http://localhost:8082")
public interface CarrosFeignClient {

    @GetMapping("/api/carros/{id}")
    CarroDTO getCarroById(@PathVariable("id") Long id);
}

