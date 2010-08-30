package br.com.toolbox.simpleorm.query;

public class Conector {

	public final static int AND = 1;
	public final static int OR = 2;

	private int tipo;

	public Conector(int tipo) {
		this.tipo = tipo;
	}

	public String getSql() {
		String retorno = null;
		switch (tipo) {
		case AND:
			retorno = " AND ";
			break;
		case OR:
			retorno = " OR ";
			break;
		}

		return retorno;
	}
}
