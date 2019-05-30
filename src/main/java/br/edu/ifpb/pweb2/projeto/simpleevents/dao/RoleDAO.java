package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Long> {

    Role findByRole(String role);
}
