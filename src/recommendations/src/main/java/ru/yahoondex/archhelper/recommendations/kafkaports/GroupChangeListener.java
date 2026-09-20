package ru.yahoondex.archhelper.recommendations.kafkaports;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.GroupDto;
import ru.yahoondex.archhelper.recommendations.repositories.GroupSetRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.GroupSet;

import java.util.Arrays;

@Component
@Slf4j
public class GroupChangeListener {
    private final GroupSetRepository groupSetRepository;

    public GroupChangeListener(GroupSetRepository groupSetRepository) {
        this.groupSetRepository = groupSetRepository;
    }

    @KafkaListener(topics = {"group-topic"},
            groupId = "recommendations",
            containerFactory = "groupConcurrentKafkaListenerContainerFactory"
    )
    @Transactional
    public void listen(GroupDto data) {
        if (data == null) {
            log.debug("Group is null");
            return;
        }
        switch (data.getOperation()) {
            case CREATE ->
                Arrays.stream(data.getSets()).forEach(setId -> groupSetRepository.save(new GroupSet(data.getId(), setId)));
            case UPDATE -> {
                groupSetRepository.deleteAllByGroupId(data.getId());
                Arrays.stream(data.getSets()).forEach(setId -> groupSetRepository.save(new GroupSet(data.getId(), setId)));
            }
            case DELETE -> groupSetRepository.deleteAllByGroupId(data.getId());
        }
    }
}
