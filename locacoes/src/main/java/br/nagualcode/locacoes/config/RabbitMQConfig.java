package br.nagualcode.locacoes.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import brave.Tracing;
import brave.spring.rabbit.SpringRabbitTracing;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue clienteQueue() {
        return new Queue("clienteQueue", true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SpringRabbitTracing springRabbitTracing(Tracing tracing) {
        return SpringRabbitTracing.newBuilder(tracing).build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory tracingRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SpringRabbitTracing springRabbitTracing,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return springRabbitTracing.decorateSimpleRabbitListenerContainerFactory(factory);
    }
}