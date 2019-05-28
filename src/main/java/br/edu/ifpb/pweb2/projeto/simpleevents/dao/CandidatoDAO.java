package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Candidato;

public interface CandidatoDAO extends JpaRepository<Candidato, Long> {

}
