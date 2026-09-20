package ru.yahoondex.archhelper.recommendations.repositories.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sets")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NameSet {
    @Id
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String name;
}
