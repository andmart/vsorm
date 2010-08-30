package br.com.toolbox.simpleorm.teste;

import br.com.toolbox.simpleorm.annotation.Column;
import br.com.toolbox.simpleorm.annotation.Table;

@Table(name="Pai")
public class Pai {

	@Column(name="id", primaryKey=true, generator=Column.AUTO)
	private Long id;
	
	@Column(name="nome_pai")
	private String nome;

	public Pai() {}

	
	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public Pai(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
