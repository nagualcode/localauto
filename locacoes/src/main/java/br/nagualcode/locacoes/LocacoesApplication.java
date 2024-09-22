package br.nagualcode.locacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LocacoesApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocacoesApplication.class, args);
    }

}
