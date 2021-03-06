package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Especialidade;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Vaga;

public interface VagaDAO extends JpaRepository<Vaga, Long>{
    List<Vaga> findByEspecialidade(Especialidade esp);
    Vaga findByEventoAndEspecialidade(Evento evento, Especialidade espec);
}

