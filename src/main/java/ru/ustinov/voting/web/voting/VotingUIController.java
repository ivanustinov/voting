package ru.ustinov.voting.web.voting;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ustinov.voting.model.User;
import ru.ustinov.voting.model.Vote;
import ru.ustinov.voting.web.AuthUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 25.10.2021
 */
@Hidden
@RestController
@RequestMapping(value = VotingUIController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingUIController extends AbstractVoteController {

    public static final String REST_URL = "/voting";

    @GetMapping
    public Vote getVote(@AuthenticationPrincipal AuthUser authUser) {
        return super.getVote(authUser.getUser(), LocalDate.now());
    }

    @GetMapping("/my_votes")
    public List<Vote> getVotes(@AuthenticationPrincipal AuthUser authUser) {
        return super.getVotes(authUser.getUser());
    }

    @GetMapping("/voting_time")
    public LocalTime getVotingTime(@AuthenticationPrincipal AuthUser authUser) {
        return super.getVotingTime(authUser.getUser());
    }

    @GetMapping("/voting_time_left")
    public Long getLeftVotingTime(@AuthenticationPrincipal AuthUser authUser) {
        final LocalTime votingTime = super.getVotingTime(authUser.getUser());
        final LocalTime now = LocalTime.now();
        final long minuteDifference = now.until(votingTime, ChronoUnit.MINUTES);
        if (minuteDifference < 0) {
            return 0L;
        }
        return minuteDifference;
    }

    @PostMapping
    public Vote vote(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurant_id) {
        final User user = authUser.getUser();
        return super.vote(user, restaurant_id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/time_zone")
    public Set<String> getTimezone() {
        return ZoneId.getAvailableZoneIds().stream().sorted().collect(TreeSet::new, Set::add, Set::addAll);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/time_zone")
    public ResponseEntity<String> setTimezone(String timeZone) {
        super.setTimeZone(timeZone);
        return ResponseEntity.ok("{\"currentTime\":\"" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + "\"}");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/email_sending")
    public ResponseEntity<String> setEmailSending(boolean sendEmails) {
        super.setMailSending(sendEmails);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email_sending")
    public ResponseEntity<Boolean> getEmailSending() {
        final boolean mailSending = super.getMailSending();
        return ResponseEntity.ok(mailSending);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/current_time")
    public ResponseEntity<String> getCurrentTime() {
        return ResponseEntity.ok("{\"currentTime\":\"" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + "\"}");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/current_time_zone")
    public TimeZone getCurrentTimezone() {
        return super.getTimeZone();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/set_time")
    public void setTime(@NonNull @RequestParam LocalTime time) {
        super.setTime(time);
    }
}
