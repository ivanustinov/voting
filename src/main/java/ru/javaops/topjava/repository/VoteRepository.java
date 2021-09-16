package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 03.09.2021
 */
public interface VoteRepository extends BaseRepository<Vote>{

    @EntityGraph(attributePaths = {"restaurant", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select v from Vote v where v.user =:user and v.date =:date order by v.date")
    Vote getVoteByUserAndDate(User user, LocalDate date);


    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select v from Vote v where v.user =:user and v.date =:date order by v.date")
    List<Vote> getVotesByUserAndDate(User user, LocalDate date);

    List<Vote> findAll();

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
   List<Vote> getVotesByUser(User user);
}