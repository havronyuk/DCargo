package dccargo.dcargoservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RolesHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String rolesHeader = request.getHeader("X-Roles");
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");

        System.out.println(">>> RolesHeaderFilter: входящий запрос " + request.getMethod() + " " + request.getRequestURI());
        System.out.println(">>> X-User-Id=" + userId);
        System.out.println(">>> X-Username=" + username);
        System.out.println(">>> X-Roles=" + rolesHeader);

        if (rolesHeader != null) {
            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader
                            .replaceAll("[\\[\\]]", "")  // убираем квадратные скобки
                            .split(","))                  // разделяем по запятой
                    .map(String::trim)             // убираем пробелы
                    .filter(r -> !r.isEmpty())     // убираем пустые строки
                    .map(SimpleGrantedAuthority::new) // НЕ добавляем "ROLE_"
                    .collect(Collectors.toList());

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
            );

            ((UsernamePasswordAuthenticationToken) auth).setDetails(username);

            SecurityContextHolder.getContext().setAuthentication(auth);

            System.out.println(">>> Установлен Authentication:");
            System.out.println("    principal=" + auth.getPrincipal());
            System.out.println("    details=" + auth.getDetails());
            System.out.println("    authorities=" + authorities.stream()
                    .map(SimpleGrantedAuthority::getAuthority)
                    .collect(Collectors.joining(",")));
        } else {
            System.out.println(">>> X-Roles не найден — SecurityContext пуст");
        }

        filterChain.doFilter(request, response);

        Authentication after = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(">>> После filterChain Authentication=" + after);
    }
}