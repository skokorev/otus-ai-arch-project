package ru.yahoondex.archhelper.recommendations.repositories.dao;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter @Setter
public class GroupSetId implements Serializable {
    private String groupId;
    private String setId;
}
