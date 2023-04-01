package br.luiztoni.demo.kafka;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class DemoApplicationTests {

	@Autowired
    private MessageConsumer consumer;

    @Autowired
    private MessageProducer producer;

    @Value("${testkafka.topic.test}")
    private String topic;

    @Test
    public void givenKafkaBrokerWhenSendingWithSimpleProducerThenMessageReceived() throws Exception {
        var data = "Sending with our own simple KafkaProducer";
        CustomMessage message = new CustomMessage();
        message.setAuthor("Luiz");
        message.setContent(data);
        producer.sendObject(topic, message);
        
        boolean messageConsumed = consumer.getLatch().await(60, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertTrue(consumer.getPayload().contains(data));
    }
}

