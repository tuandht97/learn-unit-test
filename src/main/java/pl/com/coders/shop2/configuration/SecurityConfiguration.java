package pl.com.coders.shop2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;

@Configuration
public class SecurityConfiguration {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();
        UserDetails user = User.withUsername("john@example.com")
                .password(encoder.encode("pass1"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/users/**").authenticated()
                .antMatchers("/products/**").authenticated()
                .antMatchers("/categories/**").permitAll()
                .antMatchers("/carts/**").authenticated()
                .antMatchers(HttpMethod.POST, "/carts/{productTitle}/{amount}/addProductToCart").authenticated()
                .antMatchers(HttpMethod.POST, "/orders/saveOrder/{userEmail}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/carts/{cartIndex}/{cartId}").authenticated()
                .antMatchers(HttpMethod.GET, "/orders/byUser/{username}").authenticated()
                .antMatchers("/orders/**").authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
