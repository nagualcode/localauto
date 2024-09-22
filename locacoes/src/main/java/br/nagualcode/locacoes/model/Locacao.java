package br.nagualcode.locacoes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locacao")
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "cliente_id")
    private Long clienteId;

    @NotNull
    @Column(name = "carro_id")
    private Long carroId;

    @Column(name = "inicio_locacao")
    private LocalDate inicioLocacao
    ;

    @Column(name = "fim_locacao")
    private LocalDate fimLocacao;

    @Column(name = "pago")
    private boolean pago;

    @Column(name = "modelo_carro")
    private String modeloCarro;

    @Column(name = "marca_carro")
    private String marcaCarro;

    public Locacao(Long clienteId, Long carroId, LocalDate inicioLocacao, String modeloCarro, String marcaCarro) {
        this.clienteId = clienteId;
        this.carroId = carroId;
        this.inicioLocacao = inicioLocacao;
        this.pago = false;
        this.modeloCarro = modeloCarro;
        this.marcaCarro = marcaCarro;
    }
}