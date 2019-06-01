package br.edu.ifpb.pweb2.projeto.simpleevents.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="tb_role")
@Getter
@Setter
@NoArgsConstructor
public class Role {
	@Id
	@GeneratedValue
	private Long role_id;
	private String role;
	
	@ManyToMany(mappedBy="roles", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List <Usuario> usuarios = new ArrayList<>();
}