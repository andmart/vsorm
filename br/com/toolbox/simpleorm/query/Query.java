package br.com.toolbox.simpleorm.query;

import java.util.ArrayList;
import java.util.List;

public class Query {

	private List<Criteria> criterias;
	private List<Conector> conectors;

	public Query() {
		criterias = new ArrayList<Criteria>();
		conectors = new ArrayList<Conector>();
	}

	public void addCriteria(Criteria c) {
		this.criterias.add(c);

	}

	public void addConector(Conector c) {
		this.conectors.add(c);
	}
	
	public List<Criteria> getCriterias(){
		return this.criterias;
	}
	

	public String query() {
		StringBuilder retorno = new StringBuilder();
		int i = 0;
		for (Criteria criteria : criterias) {

			retorno.append(criteria.getField() + criteria.getOperatorSql()
					+ criteria.getValue());
			if (conectors != null && !conectors.isEmpty() && i < conectors.size()) {

					retorno.append(conectors.get(i).getSql());
					i++;
			}

		}

		System.out.println(retorno.toString());
		return retorno.toString();
	}
}
