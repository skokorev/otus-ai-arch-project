package ru.yahoondex.archhelper.communicator.repositories.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(UserGroupId.class)
@Table(name = "user_groups")
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDao {
    @Id
    @Column(name = "email")
    @Getter
    @Setter
    @NonNull
    private String email;
    @Id
    @Column(name = "group_id")
    @Getter @Setter
    @NonNull
    private String groupId;
}
