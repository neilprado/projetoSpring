package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="TB_EVENTO")
public class Evento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="Campo descrição não deve estar vazio")
	private String descricao;
	
	@NotNull(message="Campo data é obrigatório")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Future(message="O evento deverá ocorrer em uma data futura")
	private Date data;
	
	private String local;
	private boolean isFinalizado;
	
	@ManyToOne
	private Usuario dono;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "evento", cascade = CascadeType.ALL)
	private List<Vaga> vagas = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="evento", cascade = CascadeType.ALL)
	private List<Avaliacao> avaliacao = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public boolean isFinalizado() {
		return isFinalizado;
	}
	public void setFinalizado(boolean isFinalizado) {
		this.isFinalizado = isFinalizado;
	}
	public Usuario getDono() {
		return dono;
	}
	public void setDono(Usuario dono) {
		this.dono = dono;
	}
	@Override
	public String toString() {
		return "Evento [id=" + id + ", descricao=" + descricao + ", data=" + data + ", local=" + local
				+ ", isFinalizado=" + isFinalizado + ", dono=" + dono + "]";
	}
	
	
}