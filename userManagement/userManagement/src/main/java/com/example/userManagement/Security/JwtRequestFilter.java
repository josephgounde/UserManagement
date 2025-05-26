package com.example.userManagement.Security;

import com.example.userManagement.service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Récupérer l'en-tête Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Vérifier si l'en-tête Authorization commence par "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Supprimer "Bearer "
            try {
                // 3. Extraire le nom d'utilisateur du token JWT
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                // Token Expired.
            } catch (Exception e) {
                // Invalid token.
            }
        }

        // 4. Vérifier si un nom d'utilisateur a été extrait et si l'authentification n'est pas déjà configurée
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Charger les détails de l'utilisateur à partir du service UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 6. Valider le token JWT
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // 7. Créer un objet UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. Configurer l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 9. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
