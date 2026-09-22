package ru.yahoondex.archhelper.communicator.repositories.dao;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter @Setter
public class UserGroupId implements Serializable {
    private String email;
    private String groupId;
}
