package com.app.services;


import com.app.dto.AuthLoginRequestDTO;
import com.app.dto.AuthResponseDTO;
import com.app.persistance.entities.UserEntity;
import com.app.persistance.repositories.UserRepository;
import com.app.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario no existe"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> authorityList
                        .add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
                );
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermission().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList
        );
    }

    public AuthResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) {
        String username = authLoginRequestDTO.username();
        String password = authLoginRequestDTO.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponseDTO(
                username,
                "User login succesfully",
                accessToken,
                true
        );
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(
                username,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }
}