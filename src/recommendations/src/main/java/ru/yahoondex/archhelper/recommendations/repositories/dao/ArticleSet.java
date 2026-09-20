package ru.yahoondex.archhelper.recommendations.repositories.dao;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Entity
@IdClass(ArticleSetId.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article_set_mapping")
public class ArticleSet {
    @Id
    @Column(name = "article_id")
    @Getter @Setter
    private String articleId;
    @Id
    @Column(name = "set_id")
    @Getter @Setter
    private String setId;
}
