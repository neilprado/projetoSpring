package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="TB_VAGA")
public class Vaga {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int quantidade;
	
	@ManyToOne
	private Evento evento;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="vaga", cascade = CascadeType.ALL)
	private List<Candidato> candidatos;
	
	@OneToOne
	private Especialidade especialidade;

}