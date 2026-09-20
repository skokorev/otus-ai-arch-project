package ru.yahoondex.archhelper.recommendations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.ArticleSet;
import ru.yahoondex.archhelper.recommendations.repositories.dao.ArticleSetId;

public interface ArticleSetRepository extends JpaRepository<ArticleSet, ArticleSetId> {
}
