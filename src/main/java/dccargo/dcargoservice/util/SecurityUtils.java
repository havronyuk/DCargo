package dccargo.dcargoservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {


    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getDetails() == null) {
            return null;
        }

        return String.valueOf(auth.getDetails());
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object principal = auth.getPrincipal();

        if (principal instanceof Long) {
            return (Long) principal;
        }

        if (principal instanceof String) {
            return Long.valueOf((String) principal);
        }

        throw new IllegalStateException("Unknown principal type: " + principal);
    }
}