package ru.yahoondex.archhelper.communicator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yahoondex.archhelper.communicator.repositories.dao.UserDao;
import ru.yahoondex.archhelper.communicator.repositories.dao.UserGroupId;

import java.util.List;

public interface UsersRepository extends JpaRepository<UserDao, UserGroupId> {
    List<UserDao> findAllByGroupId(String groupId);
    List<UserDao> findAllByEmail(String email);
}
