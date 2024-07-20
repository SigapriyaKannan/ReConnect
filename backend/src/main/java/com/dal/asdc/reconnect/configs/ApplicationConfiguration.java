package com.dal.asdc.reconnect.configs;

import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class ApplicationConfiguration
{
    private final UsersRepository userRepository;

    public ApplicationConfiguration(UsersRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService(  )
    {
//        return userEmail -> userRepository.findByUserEmail(userEmail)
//                .map(user -> User
//                        .withUsername(user.getUsername())
//                        .password(user.getPassword())
//                        .authorities(user.getAuthorities())
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return username -> userRepository.findByUserDetailsUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
