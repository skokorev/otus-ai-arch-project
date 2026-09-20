package ru.yahoondex.archhelper.crawler.config;

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
import ru.yahoondex.archhelper.commons.contracts.kafka.ArticleDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.GroupDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private ConsumerFactory<String, GroupDto> groupConsumerFactory() {
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
    public ProducerFactory<String, ArticleDto> articleProducerFactory() {
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
    public ProducerFactory<String, NameSetDto> setProducerFactory() {
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
    public NewTopic articleTopic() {
        return TopicBuilder.name("article-topic").build();
    }

    @Bean
    public NewTopic setTopic() {
        return TopicBuilder.name("set-topic").build();
    }

    @Bean
    public KafkaTemplate<String, ArticleDto> articleTemplate() {
        return new KafkaTemplate<>(articleProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, NameSetDto> setTemplate() {
        return new KafkaTemplate<>(setProducerFactory());
    }
}
