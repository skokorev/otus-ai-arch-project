package ru.yahoondex.archhelper.recommendations.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.recommendations.repositiries.dto.NameSet;

public interface SetRepository extends JpaRepository<NameSet, String> {

}
