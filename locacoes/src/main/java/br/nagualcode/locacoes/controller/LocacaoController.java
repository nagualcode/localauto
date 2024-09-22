package br.nagualcode.locacoes.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import br.nagualcode.locacoes.client.CarroDTO;
import br.nagualcode.locacoes.model.Locacao;
import br.nagualcode.locacoes.repository.LocacaoRepository;
import br.nagualcode.locacoes.service.CarroService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/locacoes")
public class LocacaoController {

    @Autowired
    LocacaoRepository locacaoRepository;

    @Autowired
    CarroService carroService;

    @PostMapping
    public ResponseEntity<Locacao> inicarLocacao(@Valid @RequestBody Locacao locacao) {
        try {

            CarroDTO carro = carroService.getCarroById(locacao.getCarroId());

            LocalDate inicioLocacao = LocalDate.now();

            Locacao novoLocacao = new Locacao(
                    locacao.getClienteId(),
                    locacao.getCarroId(),
                    inicioLocacao,
                    carro.getMarca(),
                    carro.getModelo()
            );

            Locacao _locacao = locacaoRepository.save(novoLocacao);
            return new ResponseEntity<>(_locacao, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/fimlocacao")
    public ResponseEntity<Locacao> fimLocacao(@PathVariable("id") long id) {
        var locacaoInfo = locacaoRepository.findById(id);

        if (locacaoInfo.isPresent()) {
            Locacao _locacao = locacaoInfo.get();
            _locacao.setPago(true);
            _locacao.setFimLocacao(LocalDate.now());
            return new ResponseEntity<>(locacaoRepository.save(_locacao), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Locacao>> getAllLocacoes(@RequestParam(required = false) Boolean pago) {
        try {
            List<Locacao> locacoes;

            if (pago == null)
                locacoes = locacaoRepository.findAll();
            else
                locacoes = locacaoRepository.findByPago(pago);

            if (locacoes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(locacoes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locacao> getLocacaoById(@PathVariable("id") long id) {
        var locacaoInfo = locacaoRepository.findById(id);

        return locacaoInfo.map(locacao -> new ResponseEntity<>(locacao, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Locacao>> getLocacoesByCliente(@PathVariable("clienteId") long clienteId) {
        List<Locacao> locacoes = locacaoRepository.findByClienteId(clienteId);

        if (locacoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(locacoes, HttpStatus.OK);
    }

    @GetMapping("/carro/{carroId}")
    public ResponseEntity<List<Locacao>> getLocacoesByCarro(@PathVariable("carroId") long carroId) {
        List<Locacao> locacoes = locacaoRepository.findByCarroId(carroId);

        if (locacoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(locacoes, HttpStatus.OK);
    }
}