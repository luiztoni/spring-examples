package br.luiztoni.demo.kafka;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    //@KafkaListener(topics = "${testkafka.topic.test}")
    public void receive(ConsumerRecord<?, ?> record) {
        LOGGER.info("received key='{}', value='{}'", record.key(), record.value());
        payload = record.toString();
        latch.countDown();
    }

    @KafkaListener(topics = "${testkafka.topic.test}")
    public void receive(@Payload CustomMessage data, @Headers MessageHeaders headers) {
        LOGGER.info("received data='{}'", data);
        payload = data.toString();
        headers.keySet().forEach(key -> {
            LOGGER.info("{}: {}", key, headers.get(key));
        });
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

	public CountDownLatch getLatch() {
		return latch;
	}
    
	public String getPayload() {
		return payload;
	}
    
}