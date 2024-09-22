package br.nagualcode.carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "carro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "marca")
    private String marca;

    @NotBlank
    @Column(name = "modelo")
    private String modelo;

    @Size(max = 20)
    @Column(name = "placa")
    private String placa;

    @Column(name = "cor")
    private String cor;


    public Carro(String modelo, String marca, String placa, String cor) {
        this.marca = modelo;
        this.modelo = marca;
        this.placa = placa;
        this.cor = cor;
    }
}