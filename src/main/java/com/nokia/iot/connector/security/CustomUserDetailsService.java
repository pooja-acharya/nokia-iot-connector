package com.nokia.iot.connector.security;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        String password = null;
        if (username != null) {
        } else {
            throw new UsernameNotFoundException("Username not found :"+ username);
        }

        return new User(username, password, true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

}
