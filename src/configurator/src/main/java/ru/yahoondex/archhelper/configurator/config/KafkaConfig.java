package ru.yahoondex.archhelper.configurator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import ru.yahoondex.archhelper.commons.contracts.kafka.GroupDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.UserDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private ConsumerFactory<String, NameSetDto> nameSetConsumerFactory() {
        JacksonJsonDeserializer<NameSetDto> deserializer = new JacksonJsonDeserializer<>();
        deserializer.addTrustedPackages("ru.yahoondex.archhelper.commons.contracts.kafka");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NameSetDto> nameSetConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NameSetDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(nameSetConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, GroupDto> groupProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, UserDto> userProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public NewTopic groupTopic() {
        return TopicBuilder.name("group-topic").build();
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name("user-topic").build();
    }

    @Bean
    public KafkaTemplate<String, GroupDto> groupTemplate() {
        return new KafkaTemplate<>(groupProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, UserDto> userTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }
}
