package com.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction<T> consumerFunction;

    public KafkaService(String consumerGroup, String topic, ConsumerFunction<T> consumerFunction, Map<String, String> properties) {
        this(consumerFunction, consumerGroup, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String consumerGroup, Pattern pattern, ConsumerFunction<T> consumerFunction, Map<String, String> properties) {
        this(consumerFunction, consumerGroup, properties);
        consumer.subscribe(pattern);
    }

    private KafkaService(ConsumerFunction<T> consumerFunction, String consumerGroup, Map<String, String> properties) {
        this.consumerFunction = consumerFunction;
        this.consumer = new KafkaConsumer<>(consumerProperties(consumerGroup, properties));
    }

    private Properties consumerProperties(String consumerGroup, Map<String, String> extraProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.putAll(extraProperties);
        return properties;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Found " + records.count() + " records");
                for (var record : records){
                    try {
                        consumerFunction.parse(record);
                    } catch (Exception e) {
                        // this is a dangerous decision (exception, but we eill do it since its
                        // a framework to handle it
                        // simple implementation for now just logging
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}
