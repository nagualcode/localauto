package br.nagualcode.carros.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.nagualcode.carros.model.Carro;
import br.nagualcode.carros.repository.CarroRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/api/carros")
public class CarrosController {

    private static final Logger logger = LoggerFactory.getLogger(CarrosController.class);

    @Autowired
    CarroRepository carroRepository;

    @PostMapping
    public ResponseEntity<Carro> addCarro(@Valid @RequestBody Carro carro) {
        logger.info("Adicionado Carro: {}", carro.getModelo());
        try {
            Carro _carro = carroRepository.save(new Carro(carro.getModelo(), carro.getMarca(), carro.getPlaca(),
                    carro.getCor()));
            logger.info("Adicionado: {}", _carro.getId());
            return new ResponseEntity<>(_carro, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erro: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Carro>> getAllCarros(@RequestParam(required = false) String modelo) {
        logger.info("Carros por modelo: {}", modelo);
        try {
            List<Carro> carros;

            if (modelo == null)
                carros = carroRepository.findAll();
            else
                carros = carroRepository.findByModeloContainingIgnoreCase(modelo);

            if (carros.isEmpty()) {
                logger.info("Garagem vazia para este modelo");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Total {} carros", carros.size());
            return new ResponseEntity<>(carros, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erro: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> getCarroById(@PathVariable("id") long id) {
        var carroInfo = carroRepository.findById(id);

        return carroInfo.map(carro -> new ResponseEntity<>(carro, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> updateCarro(@PathVariable("id") long id, @Valid @RequestBody Carro carro) {
        var carroInfo = carroRepository.findById(id);

        if (carroInfo.isPresent()) {
            Carro _carro = carroInfo.get();
            _carro.setModelo(carro.getModelo());
            _carro.setMarca(carro.getMarca());
            _carro.setPlaca(carro.getPlaca());
            _carro.setCor(carro.getCor());
            return new ResponseEntity<>(carroRepository.save(_carro), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCarro(@PathVariable("id") long id) {
        try {
            carroRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}