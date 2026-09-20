package ru.yahoondex.archhelper.crawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.crawler.repositories.dao.GroupSet;
import ru.yahoondex.archhelper.crawler.repositories.dao.GroupSetId;

public interface GroupSetRepository extends JpaRepository<GroupSet, GroupSetId> {
    void deleteAllByGroupId(String groupId);
}
