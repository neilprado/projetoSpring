package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="TB_USUARIO")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="Campo nome é obrigatório")
	private String nome;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message="Data inválida, não é possível cadastrar-se em um evento que já passou")
	private Date dataNascimento;
	
	@Pattern(regexp = "^\\([1-9]{2}\\) (?:[2-8]|9[1-9])[0-9]{3}\\-[0-9]{4}$", message = "Informe um telefone [(83) 99999-8888]")
	private String telefone;
	
	private String email;
	
	@Size(min=8, message="A senha deve ter no mínimo oito caracteres")
	private String senha;
	private boolean admin = false;
	
	@OneToMany(mappedBy="dono", cascade = CascadeType.ALL)
	private List<Evento> eventos;
	
	@OneToMany(mappedBy="usuario", cascade = CascadeType.ALL)
	private List<Candidato> candidato;
	
	@OneToMany(mappedBy="usuario", cascade = CascadeType.ALL)
	private List<Avaliacao> avaliacao;
	
	
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public List<Evento> getEventos() {
		return eventos;
	}
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public List<Candidato> getCandidato() {
		return candidato;
	}
	public void setCandidato(List<Candidato> candidato) {
		this.candidato = candidato;
	}
	public List<Avaliacao> getAvaliacao() {
		return avaliacao;
	}
	public void setAvaliacao(List<Avaliacao> avaliacao) {
		this.avaliacao = avaliacao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", dataNascimento=" + dataNascimento + ", telefone=" + telefone
				+ ", email=" + email + ", senha=" + senha + ", admin=" + admin + ", eventos=" + eventos + ", candidato="
				+ candidato + ", avaliacao=" + avaliacao + "]";
	}
	
}