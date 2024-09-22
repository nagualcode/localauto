package br.nagualcode.locacoes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dados_cliente")
@Data
@NoArgsConstructor
public class DadosCliente {

    @Id
    private Long id;
    private String nome;
    private String telefone;

    public DadosCliente(Long id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }
}