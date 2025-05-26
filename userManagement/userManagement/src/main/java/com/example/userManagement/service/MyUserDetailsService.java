package com.example.userManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService  {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Récupérer l'utilisateur par son email depuis le service UserService
        List<com.example.userManagement.model.User> userList = userService.getUserByEmail(email);

        // 2. Vérifier si l'utilisateur existe
        if (!userList.isEmpty()) {
            // 3. Si l'utilisateur existe, récupérer l'utilisateur
            com.example.userManagement.model.User user = userList.get(0);

            // 4. Créer un objet UserDetails à partir de l'utilisateur récupéré
            //    (Spring Security utilise UserDetails pour l'authentification)
            return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
        } else {
            // 5. Si l'utilisateur n'existe pas, lancer une exception UsernameNotFoundException
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
