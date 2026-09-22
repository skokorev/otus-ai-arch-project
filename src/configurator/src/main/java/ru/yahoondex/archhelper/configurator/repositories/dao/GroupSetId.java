package ru.yahoondex.archhelper.configurator.repositories.dao;

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
