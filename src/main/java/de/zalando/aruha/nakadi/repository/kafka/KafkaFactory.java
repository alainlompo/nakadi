package de.zalando.aruha.nakadi.repository.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

import kafka.javaapi.consumer.SimpleConsumer;

@Component
class KafkaFactory {

    @Value("${KAFKA_BROKERS:'127.0.0.1:9092'}")
    private String kafkaAddress;

    @Value("${ZK_BROKERS:'ZK_BROKERS=127.0.0.1:2181'}")
    private String zookeeperAddress;

    @Bean
    public Producer<String, String> createProducer() {
        return new KafkaProducer<>(getProps());
    }

    @Bean(name = "kafkaProperties")
    private Properties getProps() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", kafkaAddress);
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }

    public Consumer<String, String> getConsumer() {
        return new KafkaConsumer<>(getProps());
    }

    public SimpleConsumer getSimpleConsumer() {
        final String[] split = kafkaAddress.split(":");
        return new SimpleConsumer(split[0], Integer.parseInt(split[1]), 1000, 64 * 1024, "leaderlookup");
    }

}
