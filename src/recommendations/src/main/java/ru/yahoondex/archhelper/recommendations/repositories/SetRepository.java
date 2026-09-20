package ru.yahoondex.archhelper.recommendations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yahoondex.archhelper.recommendations.repositories.dao.NameSet;

import java.util.List;

public interface SetRepository extends JpaRepository<NameSet, String> {
    @Query("select ns from NameSet ns inner join GroupSet gs on ns.id = gs.setId where gs.groupId = :groupId")
    public List<NameSet> findAllByGroupId(String groupId);
}
