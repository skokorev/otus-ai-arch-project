package ru.yahoondex.archhelper.configurator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.configurator.repositories.dao.UserGroup;
import ru.yahoondex.archhelper.configurator.repositories.dao.UserGroupId;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    List<UserGroup> findAllByEmail(String email);
}
