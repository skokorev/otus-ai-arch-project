package ru.yahoondex.archhelper.recommendations.repositories.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@EqualsAndHashCode
@Table(name = "articles")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @Getter @Setter
    private String id;
    @Getter @Setter
    private LocalDate published;
    @Column(name = "abstract")
    @Getter @Setter
    private String abstr;
    @Getter @Setter
    private String title;
}
