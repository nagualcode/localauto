package br.nagualcode.clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "nome")
    private String nome;

    @NotBlank
    @Column(name = "telefone", unique = true)
    private String telefone;
    
    @NotBlank
    @Column(name = "cpf", unique = true)
    private String cpf;

    @NotBlank
    @Column(name = "nascimento", unique = true)
    private LocalDate nascimento;
    
    
    public Cliente(String nome, String telefone, String cpf, LocalDate nascimento) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.nascimento = nascimento;
    }
}