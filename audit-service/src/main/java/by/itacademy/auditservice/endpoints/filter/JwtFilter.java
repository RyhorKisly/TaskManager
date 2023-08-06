package by.itacademy.auditservice.endpoints.filter;

import by.itacademy.auditservice.core.dto.UserShortDTO;
import by.itacademy.auditservice.endpoints.utils.JwtTokenHandler;
import by.itacademy.auditservice.service.UserInteractService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenHandler jwtHandler;
    private final UserInteractService userInteractService;

    public JwtFilter(
            JwtTokenHandler jwtHandler,
            UserInteractService userInteractService
            ) {
        this.jwtHandler = jwtHandler;
        this.userInteractService = userInteractService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtHandler.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        String userName = jwtHandler.getUsername(token);

        UserDetails userDetails;
        if(userName.equals("System")) {
            userDetails = User.builder()
                    .username(userName)
                    .password("System")
                    .roles("SYSTEM")
                    .build();
        } else {
            UserShortDTO userShortDTO = userInteractService.sendAndGet(token);
            userDetails = User.builder()
                    .username(userShortDTO.getMail())
                    .password("123")
                    .roles(userShortDTO.getRole().name())
                    .build();
        }

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}