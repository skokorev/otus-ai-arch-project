package ru.yahoondex.archhelper.communicator.kafkaports;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;
import ru.yahoondex.archhelper.communicator.repositories.NameSetRepository;
import ru.yahoondex.archhelper.communicator.repositories.dao.NameSet;

@Component
@Slf4j
public class NameSetListener {
    private final NameSetRepository nameSetRepository;
    public NameSetListener(NameSetRepository nameSetRepository) {
        this.nameSetRepository = nameSetRepository;
    }

    @KafkaListener(topics = {"set-topic"},
            groupId = "mail-sender",
            containerFactory = "setListenerContainerFactory"
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