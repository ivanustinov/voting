package ru.ustinov.voting.web.user;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.ustinov.voting.repository.UserRepository;
import ru.ustinov.voting.to.UserTo;
import ru.ustinov.voting.web.GlobalExceptionHandler;
import ru.ustinov.voting.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class UniqueMailUserToValidator implements org.springframework.validation.Validator {

    private final UserRepository repository;

    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserTo userTo = ((UserTo) target);
        if (StringUtils.hasText(userTo.getEmail())) {
            repository.getByEmail(userTo.getEmail().toLowerCase())
                    .ifPresent(dbUser -> {
                        Assert.notNull(request, "HttpServletRequest missed");
                        if (request.getMethod().equals("PUT") || (request.getMethod().equals("POST"))) {  // UPDATE
                            int dbId = dbUser.id();
                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if ((dbId == SecurityUtil.authId() && requestURI.contains("/profile")))
                                return;
                        }
                        errors.rejectValue("email", GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL);
                    });
        }
    }
}
