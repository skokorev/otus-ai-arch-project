package ru.yahoondex.archhelper.communicator.kafkaports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.UserDto;
import ru.yahoondex.archhelper.communicator.repositories.UsersRepository;
import ru.yahoondex.archhelper.communicator.repositories.dao.UserDao;
import ru.yahoondex.archhelper.communicator.repositories.dao.UserGroupId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserGroupListener {

    private final UsersRepository usersRepository;
    public UserGroupListener(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @KafkaListener(topics = {"user-topic"},
            groupId = "mail-sender",
            containerFactory = "userListenerContainerFactory"
    )
    public void userChanged(UserDto user) {
        List<UserDao> userGroups = usersRepository.findAllByEmail(user.getEmail());
        List<String> currentGroupIds = userGroups.stream().map(UserDao::getGroupId).collect(Collectors.toList());
        List<String> futureGroupIds = Arrays.asList(user.getGroups());
        List<String> groupsToAdd = new LinkedList<>();
        List<String> groupsToRemove = new LinkedList<>();
        currentGroupIds.stream().forEach(currentGroupId -> {
            if (futureGroupIds.stream().noneMatch(currentGroupId::equals)) {
                groupsToRemove.add(currentGroupId);
            }
        });
        futureGroupIds.stream().forEach(futureGroupId -> {
            if (currentGroupIds.stream().noneMatch(futureGroupId::equals)) {
                groupsToAdd.add(futureGroupId);
            }
        });
        groupsToRemove.forEach(groupId -> usersRepository.deleteById(new UserGroupId(user.getEmail(), groupId)));
        groupsToAdd.forEach(groupId -> usersRepository.save(new UserDao(user.getEmail(), groupId)));
    }

}
