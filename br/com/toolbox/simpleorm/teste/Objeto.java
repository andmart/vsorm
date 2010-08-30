package br.com.toolbox.simpleorm.teste;

import br.com.toolbox.simpleorm.annotation.Column;
import br.com.toolbox.simpleorm.annotation.Table;

@Table(name="Objeto2")
public class Objeto {

	@Column(name="tipo")
	private String tipo;
	@Column(name="nome")
	private String nome;
	@Column(name="idade")
	private int idade;
	@Column(name="id", primaryKey=true, generator=Column.AUTO)
	private Long id;
	
	public Objeto(){}
	
	public Objeto(String tipo, String nome, int idade){
		this.tipo = tipo;
		this.idade = idade;
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
