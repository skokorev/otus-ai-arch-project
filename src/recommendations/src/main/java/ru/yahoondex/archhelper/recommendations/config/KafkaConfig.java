package ru.yahoondex.archhelper.recommendations.config;

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
import ru.yahoondex.archhelper.commons.contracts.kafka.ArticleDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.GroupDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.RecommendationListDto;


import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, RecommendationListDto> recommendationsProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
          bootstrapServers);
        configProps.put(
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
          StringSerializer.class);
        configProps.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
          JacksonJsonDeserializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private ConsumerFactory<String, GroupDto> groupConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>());
    }

    private ConsumerFactory<String, NameSetDto> nameSetConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>());
    }

    private ConsumerFactory<String, ArticleDto> articleConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GroupDto> groupConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GroupDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(groupConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NameSetDto> nameSetConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NameSetDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(nameSetConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ArticleDto> articleConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ArticleDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(articleConsumerFactory());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, RecommendationListDto> recommendationKafkaTemplate() {
        return new KafkaTemplate<>(recommendationsProducerFactory());
    }

    @Bean
    public NewTopic recommendationTopic() {
        return TopicBuilder.name("recommendation-topic").build();
    }


    
}