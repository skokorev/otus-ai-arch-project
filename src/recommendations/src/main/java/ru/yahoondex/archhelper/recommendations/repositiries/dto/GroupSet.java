package ru.yahoondex.archhelper.recommendations.repositiries.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "group_sets")
@EqualsAndHashCode
public class GroupSet {
    @Id
    @Column(name = "group_id")
    @Getter @Setter
    private String groupId;
    @Column(name = "set_id")
    @Getter @Setter
    private String setId;
}
