package ru.yahoondex.archhelper.communicator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.RecommendationListDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.UserDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private ConsumerFactory<String, RecommendationListDto> recommendationConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>());
    }

    private ConsumerFactory<String, UserDto> userConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>());
    }

    private ConsumerFactory<String, NameSetDto> setConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RecommendationListDto> recommendationListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RecommendationListDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(recommendationConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDto> userListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NameSetDto> setListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NameSetDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(setConsumerFactory());
        return factory;
    }
}
