package ru.yahoondex.archhelper.crawler.repositories.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "sets")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameSet {
    @Id
    @Getter
    @Setter
    private String id;
    @Getter @Setter
    private String name;
}
