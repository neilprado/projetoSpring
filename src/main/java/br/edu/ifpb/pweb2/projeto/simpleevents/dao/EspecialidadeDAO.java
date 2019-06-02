package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Especialidade;

public interface EspecialidadeDAO extends JpaRepository<Especialidade, Long>{
    Especialidade findByNomeIgnoreCase(String nome);
}
