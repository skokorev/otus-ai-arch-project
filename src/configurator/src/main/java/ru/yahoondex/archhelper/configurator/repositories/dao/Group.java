package ru.yahoondex.archhelper.configurator.repositories.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "groups")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String name;
}
