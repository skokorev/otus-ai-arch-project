package ru.yahoondex.archhelper.communicator.kafkaports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.UserDto;

@Component
@Slf4j
public class UserGroupListener {

    @KafkaListener(topics = {"user-topic"},
            groupId = "mail-sender",
            containerFactory = "userListenerContainerFactory"
    )
    public void userChanged(UserDto user) {

    }

}
