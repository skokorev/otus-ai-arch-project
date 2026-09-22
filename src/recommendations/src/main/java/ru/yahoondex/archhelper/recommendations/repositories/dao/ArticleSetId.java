package ru.yahoondex.archhelper.recommendations.repositories.dao;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter @Setter
public class ArticleSetId implements Serializable {
    private String articleId;
    private String setId;
}
