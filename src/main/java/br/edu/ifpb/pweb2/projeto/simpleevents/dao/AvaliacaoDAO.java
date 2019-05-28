package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Avaliacao;

public interface AvaliacaoDAO extends JpaRepository<Avaliacao, Long> {

}
