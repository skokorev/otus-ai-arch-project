package ru.yahoondex.archhelper.configurator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yahoondex.archhelper.commons.contracts.kafka.GroupDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.Operation;
import ru.yahoondex.archhelper.commons.contracts.kafka.UserDto;
import ru.yahoondex.archhelper.configurator.controllers.dto.Group;
import ru.yahoondex.archhelper.configurator.repositories.GroupRepository;
import ru.yahoondex.archhelper.configurator.repositories.GroupSetRepository;
import ru.yahoondex.archhelper.configurator.repositories.UserGroupRepository;
import ru.yahoondex.archhelper.configurator.repositories.dao.GroupSet;
import ru.yahoondex.archhelper.configurator.repositories.dao.GroupSetId;
import ru.yahoondex.archhelper.configurator.repositories.dao.UserGroup;
import ru.yahoondex.archhelper.configurator.repositories.dao.UserGroupId;
import ru.yahoondex.archhelper.configurator.services.dto.GroupLite;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupService {

    private GroupRepository groupRepository;
    private UserGroupRepository userGroupRepository;
    private GroupSetRepository groupSetRepository;
    private KafkaTemplate<String, GroupDto> groupTemplate;
    private KafkaTemplate<String, UserDto> userTemplate;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        UserGroupRepository userGroupRepository,
                        GroupSetRepository groupSetRepository,
                        KafkaTemplate<String, GroupDto> groupTemplate,
                        KafkaTemplate<String, UserDto> userTemplate) {
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
        this.groupSetRepository = groupSetRepository;
        this.groupTemplate = groupTemplate;
        this.userTemplate = userTemplate;
    }

    @Transactional
    public List<GroupLite> getGroups() {
        return groupRepository.findAll().stream().map(g -> new GroupLite(g.getId(), g.getName())).collect(Collectors.toList());
    }

    @Transactional
    public Group getGroup(String id) {
        return groupRepository.findById(id).map(g -> {
            final List<String> setIds = groupSetRepository.findAllByGroupId(g.getId()).stream().map(GroupSet::getSetId).collect(Collectors.toList());
            return new Group(g.getId(), g.getName(), setIds);
        }).orElseThrow();
    }

    @Transactional
    public void createGroup(String name) {
        final String id = UUID.randomUUID().toString();
        ru.yahoondex.archhelper.configurator.repositories.dao.Group group = new ru.yahoondex.archhelper.configurator.repositories.dao.Group(id, name);
        groupRepository.save(group);
        groupTemplate.send("group-topic", id, new GroupDto(id, name, new String[]{}, Operation.CREATE));
    }

    @Transactional
    public void renameGroup(String groupId, String newName) {
        if (groupId == null) {
            log.error("Group ID is null");
            return;
        }
        groupRepository.findById(groupId).ifPresent(g -> {
            groupRepository.save(new ru.yahoondex.archhelper.configurator.repositories.dao.Group(g.getId(), g.getName()));
            String[] setIds = groupSetRepository.findAllByGroupId(groupId).stream().map(GroupSet::getSetId).toArray(String[]::new);
            groupTemplate.send("group-topic", groupId, new GroupDto(groupId, newName, setIds, Operation.UPDATE));
        });
    }

    @Transactional
    public void deleteGroup(String groupId) {
        if (groupId == null) {
            log.error("Group ID is null");
            return;
        }
        groupSetRepository.deleteAllByGroupId(groupId);
        groupRepository.deleteById(groupId);
        groupTemplate.send("group-topic", groupId, new GroupDto(groupId, null, new String[] {}, Operation.DELETE));
    }

    @Transactional
    public void addNameSet(String groupId, String setId) {
        if (setId == null) {
            log.error("SetId is null");
            return;
        }
        if (groupId == null) {
            log.error("Group ID is null");
            return;
        }
        groupRepository.findById(groupId).ifPresentOrElse(group -> {
            List<String> setIds = new LinkedList<>(groupSetRepository.findAllByGroupId(groupId).stream().map(GroupSet::getSetId).collect(Collectors.toList()));
            if (setIds.stream().noneMatch(setId::equals)) {
                setIds.add(setId);
                groupSetRepository.save(new GroupSet(groupId, setId));
                groupTemplate.send("group-topic", groupId, new GroupDto(groupId, group.getName(), setIds.toArray(String[]::new), Operation.UPDATE));
            }
        }, () -> {
            log.debug("Group {} not found", groupId);
        });
    }

    @Transactional
    public void deleteNameSet(String groupId, String setId) {
        if (groupId == null) {
            log.error("Group ID is null");
            return;
        }
        if (setId == null) {
            log.error("SetId is null");
            return;
        }
        groupRepository.findById(groupId).ifPresentOrElse(group -> {
            List<String> setIds = new LinkedList<>(groupSetRepository.findAllByGroupId(groupId).stream().map(GroupSet::getSetId).collect(Collectors.toList()));
            if (setIds.stream().anyMatch(setId::equals)) {
                groupSetRepository.deleteById(new GroupSetId(groupId, setId));
                setIds.removeIf(setId::equals);
                groupTemplate.send("group-topic", groupId, new GroupDto(groupId, group.getName(), setIds.toArray(String[]::new), Operation.UPDATE));
            }
        }, () -> {
            log.debug("Group {} not found", groupId);
        });
    }

    @Transactional
    public void addUser(String groupId, String email) {
        if (groupId == null) {
            log.error("Group ID is null");
            return;
        }
        if (email == null) {
            log.error("Email is null");
            return;
        }
        final List<UserGroup> userGroups = userGroupRepository.findAllByEmail(email);
        if (userGroups.stream().noneMatch(g -> groupId.equals(g.getGroupId()))) {
            userGroupRepository.save(new UserGroup(groupId, email));
            List<String> groupIds = new LinkedList<>(userGroups.stream().map(UserGroup::getGroupId).collect(Collectors.toList()));
            groupIds.add(groupId);
            userTemplate.send("user-topic", groupId, new UserDto(email, groupIds.toArray(String[]::new), Operation.UPDATE));
        }
    }

    @Transactional
    public void removeUser(String groupId, String email) {
        if (groupId == null) {
            log.error("Group ID is null");
            return;
        }
        if (email == null) {
            log.error("Email is null");
            return;
        }
        final List<UserGroup> userGroups = userGroupRepository.findAllByEmail(email);
        if (userGroups.stream().anyMatch(g -> groupId.equals(g.getGroupId()))) {
            List<String> groupIds = new LinkedList<>(userGroups.stream().map(UserGroup::getGroupId).collect(Collectors.toList()));
            groupIds.removeIf(groupId::equals);
            userGroupRepository.deleteById(new UserGroupId(groupId, email));
            userTemplate.send("user-topic", groupId, new UserDto(email, groupIds.toArray(String[]::new), Operation.UPDATE));
        }
    }
}
