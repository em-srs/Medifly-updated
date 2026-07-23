package com.medifly.security;

import com.medifly.model.User;
import com.medifly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String idOrEmail) throws UsernameNotFoundException {
        User user = null;
        try {
            Long id = Long.parseLong(idOrEmail);
            user = userRepository.findById(id).orElse(null);
        } catch (NumberFormatException ignored) {}

        if (user == null) {
            user = userRepository.findByEmail(idOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id/email: " + idOrEmail));
        }

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
        );
    }
}
