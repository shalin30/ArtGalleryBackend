package org.example.security;

import org.example.repository.BlacklistRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final BlacklistRepository blacklistRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, BlacklistRepository blacklistRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {

            if (blacklistRepository.existsByToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }
            String username = jwtTokenProvider.getUsernameFromToken(token);
            System.out.println("Username: " + username);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);
            System.out.println("Roles: " + roles);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            System.out.println("Authorities: " + authorities);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);  // Assuming no credentials are needed here

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            Authentication authenticationInContext = SecurityContextHolder.getContext().getAuthentication();
            if (authenticationInContext != null) {
                for (GrantedAuthority authority : authenticationInContext.getAuthorities()) {
                    System.out.println("Authority in context: " + authority.getAuthority());
                }
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
