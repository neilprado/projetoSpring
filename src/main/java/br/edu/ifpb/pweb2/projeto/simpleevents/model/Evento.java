package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "TB_EVENTO")
@Getter
@Setter
@ToString
public class Evento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Campo descrição não deve estar vazio")
	private String descricao;

	@NotNull(message = "Campo data é obrigatório")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Future(message = "O evento deverá ocorrer em uma data futura")
	private LocalDateTime data;
	@NotNull(message = "Campo data é obrigatório")
	private String nome;
	private String local;
	private boolean isFinalizado;

	@ManyToOne
	private Usuario dono;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "evento", cascade = CascadeType.ALL)
	@ElementCollection
	private List<Vaga> vagas = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "evento", cascade = CascadeType.ALL)
	private List<Avaliacao> avaliacao = new ArrayList<>();

	public void addVaga(Vaga vaga) {
		this.vagas.add(vaga);
	}

	public void removeVaga(Vaga vaga) {
		this.vagas.remove(vaga);
	}
}