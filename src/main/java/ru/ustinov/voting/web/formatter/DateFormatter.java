package ru.ustinov.voting.web.formatter;

import lombok.Data;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 05.12.2021
 */

@Data
public class DateFormatter {

    public static final String DATE_FORMAT_PROPERTY = "app.date-format";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static String STATIC_ACTIVE_DATE_FORMAT = DEFAULT_DATE_FORMAT;

    private final Environment environment;

    public DateFormatter(Environment environment) {
        this.environment = environment;
    }

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(STATIC_ACTIVE_DATE_FORMAT));
    }

    @PostConstruct
    private void postConstr() {
        STATIC_ACTIVE_DATE_FORMAT = environment.getProperty(DATE_FORMAT_PROPERTY, DEFAULT_DATE_FORMAT);
    }
}
