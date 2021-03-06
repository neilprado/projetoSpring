package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);
}
