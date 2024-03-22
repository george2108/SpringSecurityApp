package com.app.config;

import com.app.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// para configurar spring security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // es el primer filtro que inicia al realizar una peticion http
    // aqui definimos nuestras validaciones de que rutas deben tener autorizacion
    /*@Bean
    public SecurityFilterChain securityFilterChain(
            // con este objeto creamos el comportamiento de nuestro filtro
            HttpSecurity httpSecurity
    ) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // deshabilitar el manejo de sesion de spring boot y guiarnos con el de jwt
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy((SessionCreationPolicy.STATELESS))
                )
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(http -> {
                    // configurar endpoints publicos
                    http.requestMatchers(HttpMethod.GET, "/auth/hello").permitAll();
                    // configurar endpoints privados
                    http.requestMatchers(HttpMethod.GET, "/auth/hello-secured").hasAuthority("CREATE");
                    // configurar el resto de endpoints no especificados
                    http.anyRequest().authenticated();
                })
                .build();
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(
            // con este objeto creamos el comportamiento de nuestro filtro
            HttpSecurity httpSecurity
    ) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // deshabilitar el manejo de sesion de spring boot y guiarnos con el de jwt
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy((SessionCreationPolicy.STATELESS))
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    // encargado de la autenticacion
    // necesita providers para conocer los usuarios
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // encargado del tipo de autenticacion
    // necesita dos componentes
    // uno para la codificacion y decodificacion de la contraseña
    // otro para obtener los usuarios
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsServiceImpl userDetailsService
    ){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    // encargado de la codificacion y decodificacion de la contraseña
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }


}
