package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="TB_USUARIO")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long user_id;
	
	@NotEmpty(message="Campo nome é obrigatório")
	private String nome;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataNascimento;
	
	@Pattern(regexp = "^\\([1-9]{2}\\) (?:[2-8]|9[1-9])[0-9]{3}\\-[0-9]{4}$", message = "Informe um telefone [(83) 99999-8888]")
	private String telefone;

	//@Size(min=8, message="A senha deve ter no mínimo oito caracteres")
	private String password;
	private String email;
	
	@OneToMany(mappedBy="dono", cascade = CascadeType.ALL)
	private List<Evento> eventos;
	
	@OneToMany(mappedBy="usuario", cascade = CascadeType.ALL)
	private List<Candidato> candidato;
	
	@OneToMany(mappedBy="usuario", cascade = CascadeType.ALL)
	private List<Avaliacao> avaliacao;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
}