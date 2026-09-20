package ru.yahoondex.archhelper.recommendations.kafkaports;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;
import ru.yahoondex.archhelper.recommendations.repositories.NameSetRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.NameSet;

@Component
@Slf4j
public class NameSetChangeListener {
    private final NameSetRepository nameSetRepository;
    public NameSetChangeListener(NameSetRepository nameSetRepository) {
        this.nameSetRepository = nameSetRepository;
    }

    @KafkaListener(topics = {"set-topic"},
            groupId = "recommendations",
            containerFactory = "nameSetConcurrentKafkaListenerContainerFactory"
    )
    @Transactional
    public void listen(NameSetDto data) {
        if (data == null) {
            log.debug("Set is null");
            return;
        }
        switch (data.getOperation()) {
            case CREATE -> nameSetRepository.save(new NameSet(data.getId(), data.getName()));
            case UPDATE ->
                nameSetRepository.findById(data.getId()).ifPresentOrElse(ns ->
                    nameSetRepository.save(new NameSet(ns.getId(), data.getName())),
                        () -> log.debug("Unable to find group {}", data.getId()));
            case DELETE -> nameSetRepository.deleteById(data.getId());
        }
    }
}
