package ro.tuc.ds2020.rabbitMQ;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.DeviceMessage;

@RequiredArgsConstructor
@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
    private final RabbitTemplate rabbitTemplate;


    public void sendMessage(DeviceMessage deviceMessage){
        LOGGER.info(String.format("Sent message: %s", deviceMessage));
        rabbitTemplate.convertAndSend(exchange, routingKey, convertObjectToJson(deviceMessage));
    }


    private String convertObjectToJson(DeviceMessage deviceMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(deviceMessage);
        } catch (JsonProcessingException error) {
            throw new RuntimeException("Error converting Device to JSON", error);
        }
    }
}

