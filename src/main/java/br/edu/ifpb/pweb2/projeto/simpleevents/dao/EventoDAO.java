package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Vaga;

public interface EventoDAO extends JpaRepository<Evento, Long> {
    List<Evento> findByNomeContainingIgnoreCase(String nome);

    List<Evento> findAllByVagas(Vaga vaga);

    List<Evento> findAllByDono(Usuario dono);
}
