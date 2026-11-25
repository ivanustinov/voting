package ru.ustinov.voting.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ustinov.voting.service.MenuService;
import ru.ustinov.voting.to.MenusTo;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 17.11.2021
 */
@Slf4j
public class AbstractMenusController {

    @Autowired
    private MenuService menuService;

    public void delete(LocalDate date) {
        log.info("Delete menus by date {}", date);
        menuService.delete(date);
    }

    public List<MenusTo> getAll() {
        log.info("get all menus");
        return menuService.getAll();
    }

    public void copy(LocalDate date) {
        log.info("copy menus by date {}", date);
        menuService.copy(date);
    }
}
