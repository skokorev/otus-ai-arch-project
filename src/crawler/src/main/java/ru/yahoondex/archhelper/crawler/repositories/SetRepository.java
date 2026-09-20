package ru.yahoondex.archhelper.crawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.crawler.repositories.dao.NameSet;

public interface SetRepository extends JpaRepository<NameSet, String> {
}
