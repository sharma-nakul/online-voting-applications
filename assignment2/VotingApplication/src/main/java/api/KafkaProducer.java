package api;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.util.Properties;


public class KafkaProducer {
    private static final Logger logger = Logger.getLogger(KafkaProducer.class);

    private static Producer<String, String> producer;
    private final Properties properties = new Properties();

    public KafkaProducer(String kafkaURL) {
        properties.put("metadata.broker.list", kafkaURL);
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<>(new ProducerConfig(properties));
    }

    public void send(String topic, String msg) {
        KeyedMessage<String, String> data = new KeyedMessage<>(topic, msg);
        producer.send(data);
       // logger.info(msg);
        producer.close();
    }
}

