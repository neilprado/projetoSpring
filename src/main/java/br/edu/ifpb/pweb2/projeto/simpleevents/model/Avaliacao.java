package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_AVALIACAO")
public class Avaliacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int notaEvento;
	
	@ManyToOne
	private Evento evento;
	
	@ManyToOne
	private Usuario usuario;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNotaEvento() {
		return notaEvento;
	}
	public void setNotaEvento(int notaEvento) {
		this.notaEvento = notaEvento;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	@Override
	public String toString() {
		return "Avaliacao [id=" + id + ", notaEvento=" + notaEvento + ", evento=" + evento + ", usuario=" + usuario
				+ "]";
	}
	
	
}