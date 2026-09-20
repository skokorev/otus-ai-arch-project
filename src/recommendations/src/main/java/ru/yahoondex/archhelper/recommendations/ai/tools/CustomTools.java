package ru.yahoondex.archhelper.recommendations.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.recommendations.repositories.ArticleRepository;
import ru.yahoondex.archhelper.recommendations.repositories.SetRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.Article;
import ru.yahoondex.archhelper.recommendations.repositories.dao.NameSet;

import java.time.LocalDate;
import java.util.List;

@Component
public class CustomTools {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private SetRepository setRepository;

    @Tool(description="Получить наборы для заданной группы")
    public List<NameSet> getCollectedSets(String groupId) {
        return setRepository.findAllByGroupId(groupId);
    }

    @Tool(description = "Получить статьи для заданного набора и интервала дат")
    public List<Article> getArticles(String setId, LocalDate dateFrom, LocalDate dateTo) {
        return articleRepository.findAllBySet(setId, dateFrom, dateTo);
    }
}