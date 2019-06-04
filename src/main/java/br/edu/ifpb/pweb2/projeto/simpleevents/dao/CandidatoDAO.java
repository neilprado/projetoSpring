package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Candidato;

import java.util.List;

public interface CandidatoDAO extends JpaRepository<Candidato, Long> {

    List<Candidato> findByVaga_Evento(Evento evento);

}
