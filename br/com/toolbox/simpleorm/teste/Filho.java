package br.com.toolbox.simpleorm.teste;

import br.com.toolbox.simpleorm.annotation.Column;
import br.com.toolbox.simpleorm.annotation.Table;

@Table(name="Filho")
public class Filho {

	@Column(name="filhoId", primaryKey=true, generator=Column.AUTO)
	private Long id;
	@Column(name="nome_filho")
	private String nome;
	
	private Pai pai;
	
	public Filho(){}
	
	public Filho(String nome, Pai pai) {
		super();
		this.nome = nome;
		this.pai = pai;
	}
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Pai getPai() {
		return pai;
	}
	public void setPai(Pai pai) {
		this.pai = pai;
	}
	
}
