package br.edu.ifpb.pweb2.projeto.simpleevents.service;

import br.edu.ifpb.pweb2.projeto.simpleevents.dao.UsuarioDAO;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioDAO repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = repository.findByEmail(email);
        CustomUserDetails userDetails;
        if (user != null) {
            userDetails = new CustomUserDetails();
            userDetails.setUser(user);
        } else {
            throw new UsernameNotFoundException("User not exist with name : " + email);
        }
        return userDetails;

    }

}