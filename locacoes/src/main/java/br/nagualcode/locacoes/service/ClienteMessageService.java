package br.nagualcode.locacoes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.nagualcode.locacoes.model.DadosCliente;
import br.nagualcode.locacoes.repository.DadosClienteRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClienteMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteMessageService.class);

    @Autowired
    private DadosClienteRepository userInfoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "clienteQueue", containerFactory = "tracingRabbitListenerContainerFactory")
    public void receiveUserMessage(String message) throws JsonProcessingException {
        logger.info("RabbitMQ: {}", message);
        try {
            DadosCliente clienteInfo = objectMapper.readValue(message, DadosCliente.class);
            logger.info("Cliente ID: {}", clienteInfo.getId());
            userInfoRepository.save(clienteInfo);
            logger.info("saved in repo.");
        } catch (Exception e) {
            logger.error("Error", e);
            throw e;
        }
    }
}