package ru.yahoondex.archhelper.configurator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.configurator.repositories.dao.Group;

public interface GroupRepository extends JpaRepository<Group, String> {
}
