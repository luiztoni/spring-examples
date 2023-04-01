package br.luiztoni.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
	private KafkaTemplate<String, CustomMessage> kafkaTemplate;

	public void sendObject(String topic, CustomMessage data) {
		Message<CustomMessage> message = MessageBuilder
				.withPayload(data)
				.setHeader(KafkaHeaders.TOPIC, topic)
				.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
				.build();
		LOGGER.info("sending data='{}' to topic='{}'", data, topic);

		kafkaTemplate.send(message).whenComplete((result, exception) -> {
			if (exception != null) {
				System.out.println("Execution failed" + exception.getMessage());
			} else {
				System.out.println(
						"Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}
		});

	}
}