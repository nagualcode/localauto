package br.nagualcode.clientes.controller;

import br.nagualcode.clientes.model.Cliente;
import br.nagualcode.clientes.repository.ClienteRepository;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.util.List;
import java.util.Optional;
import brave.spring.rabbit.SpringRabbitTracing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue clienteQueue;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringRabbitTracing springRabbitTracing;

    @PostMapping("/cadastro")
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente cliente) {
        logger.info("Cadastro: {}", cliente.getTelefone());
        try {
            if (clienteRepository.findByTelefone(cliente.getTelefone()).isPresent()) {
                logger.warn("Ja existe um cadastro para: {}", cliente.getTelefone());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Cliente novoCliente = new Cliente(cliente.getNome(), cliente.getTelefone(), cliente.getCpf(), cliente.getNascimento());
            Cliente _cliente = clienteRepository.save(novoCliente);

            logger.info("Registrado cliente: {}", _cliente.getId());


            String usuarioJson = objectMapper.writeValueAsString(_cliente);
            logger.info("Entrou na fla: {}", clienteQueue.getName());


            RabbitTemplate tracedRabbitTemplate = springRabbitTracing.newRabbitTemplate(connectionFactory);
            tracedRabbitTemplate.convertAndSend(clienteQueue.getName(), usuarioJson);
            logger.info("Enviado para RabbitMQ");

            return new ResponseEntity<>(_cliente, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            logger.error("Erro na deserealização", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarPerfil(@PathVariable("id") long id, @Valid @RequestBody Cliente cliente) {
        Optional<Cliente> clienteInfo = clienteRepository.findById(id);

        if (clienteInfo.isPresent()) {
            Cliente _cliente = clienteInfo.get();
            _cliente.setNome(cliente.getNome());
            _cliente.setTelefone(cliente.getTelefone());
            _cliente.setCpf(cliente.getCpf());
            _cliente.setNascimento(cliente.getNascimento());
            return new ResponseEntity<>(clienteRepository.save(_cliente), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes(@RequestParam(required = false) String nome) {
        try {
            List<Cliente> clientes;

            if (nome == null)
                clientes = clienteRepository.findAll();
            else
                clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);

            if (clientes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(clientes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable("id") long id) {
        Optional<Cliente> clienteInfo = clienteRepository.findById(id);

        return clienteInfo.map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCliente(@PathVariable("id") long id) {
        try {
            clienteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}