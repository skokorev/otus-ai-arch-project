package ru.yahoondex.archhelper.recommendations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yahoondex.archhelper.recommendations.repositories.dao.Article;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, String> {
    @Query("select a from Article a inner join ArticleSet aset on aset.articleId = a.id where aset.setId = :setId and a.published between :dateFrom and :dateTo")
    public List<Article> findAllBySet(String setId, LocalDate dateFrom, LocalDate dateTo);
}
