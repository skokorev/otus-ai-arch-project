package ru.yahoondex.archhelper.recommendations.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.recommendations.ai.tools.dto.Article;

import java.time.Instant;
import java.util.Set;

@Component
public class CustomTool {

    @Tool(description="Получить наборы для заданной группы")
    public String[] getCollectedSets(String groupId) {
        return new String[] {"Computer vision", "Vector search", "Artificial Intelligence", "Search process", "Nearest neighbor search"};
    }

    @Tool(description = "Получить необработанные статьи для заданной группы и даты")
    public Set<Article> getArticles(String groupId, Instant timepoint) {
        return Set.of(new Article());
    }


    //https://oaipmh.arxiv.org/oai?verb=ListSets
    //https://oaipmh.arxiv.org/oai?verb=ListRecords&metadataPrefix=arXivRaw
    //https://oaipmh.arxiv.org/oai?verb=ListRecords&metadataPrefix=arXiv&from=2026-09-10&until=2026-09-15&set=cs:cs

    //groupId c3dc5cbd-3356-4783-9692-db0581ba14d8
}