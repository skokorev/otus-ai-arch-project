package ru.yahoondex.archhelper.recommendations.repositiries.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sets")
public class NameSet {
    @Id
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String name;
}
