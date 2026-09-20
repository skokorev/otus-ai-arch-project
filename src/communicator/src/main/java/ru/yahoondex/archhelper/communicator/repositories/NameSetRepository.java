package ru.yahoondex.archhelper.communicator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.communicator.repositories.dao.NameSet;

public interface NameSetRepository extends JpaRepository<NameSet, String> {

}
