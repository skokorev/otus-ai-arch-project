package ru.yahoondex.archhelper.recommendations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.GroupSet;
import ru.yahoondex.archhelper.recommendations.repositories.dao.GroupSetId;

public interface GroupSetRepository extends JpaRepository<GroupSet, GroupSetId> {
    void deleteAllByGroupId(String groupId);
}
