package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_CANDIDATO")
public class Candidato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int nota;
	
	@ManyToOne
	private Vaga vaga;
	
	@OneToOne
	private Usuario usuario;
	private Status aprovacao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public Vaga getVaga() {
		return vaga;
	}
	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Status getAprovacao() {
		return aprovacao;
	}
	public void setAprovacao(Status aprovacao) {
		this.aprovacao = aprovacao;
	}
	@Override
	public String toString() {
		return "Candidato [id=" + id + ", nota=" + nota + ", vaga=" + vaga + ", usuario=" + usuario + ", aprovacao="
				+ aprovacao + "]";
	}
}