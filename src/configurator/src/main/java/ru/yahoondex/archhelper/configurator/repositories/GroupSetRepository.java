package ru.yahoondex.archhelper.configurator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.configurator.repositories.dao.GroupSet;
import ru.yahoondex.archhelper.configurator.repositories.dao.GroupSetId;

import java.util.List;

public interface GroupSetRepository extends JpaRepository<GroupSet, GroupSetId> {
    List<GroupSet> findAllByGroupId(String groupId);
    void deleteAllByGroupId(String groupId);
}
