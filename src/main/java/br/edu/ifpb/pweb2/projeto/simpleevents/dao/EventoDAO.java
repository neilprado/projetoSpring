package br.edu.ifpb.pweb2.projeto.simpleevents.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.pweb2.projeto.simpleevents.model.Evento;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Usuario;
import br.edu.ifpb.pweb2.projeto.simpleevents.model.Vaga;

public interface EventoDAO extends JpaRepository<Evento, Long> {
    List<Evento> findByNomeContainingIgnoreCase(String nome);

    List<Evento> findAllByVagas(Vaga vaga);

    List<Evento> findAllByDono(Usuario dono);

    @Transactional
    @Modifying
    @Query("UPDATE Evento e SET e.isFinalizado = :status WHERE e.id = :eventoId")
    int updateEvento(@Param("eventoId") Long eventoId, @Param("status") boolean status);

}
