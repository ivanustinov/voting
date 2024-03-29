package ru.ustinov.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.ustinov.voting.util.validation.ValidationUtil;

// https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query.spel-expressions
    @Modifying
    @Query("DELETE FROM #{#entityName} u WHERE u.id=:id")
    int delete(int id);

    @Transactional
    default void deleteExisted(int id) {
        ValidationUtil.checkModification(delete(id), id);
    }
}