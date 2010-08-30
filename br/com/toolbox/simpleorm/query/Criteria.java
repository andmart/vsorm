package br.com.toolbox.simpleorm.query;

public class Criteria {

	public static final int EQUAL = 1;
	public static final int NOT_EQUAL = 2;
	public static final int LESS_THAN = 3;
	public static final int GREATER_THAN = 4;
	public static final int STARTS_WITH = 5;
	public static final int ENDS_WITH = 6;
	public static final int CONTAINS = 7;
	public static final int NOT_STARTS_WITH = 8;
	public static final int NOT_ENDS_WITH = 9;
	public static final int NOT_CONTAINS = 10;
	
	
	private String field;
	private int operator;
	private Object value;

	private static final String LIKE_CHAR = "%";

	public Criteria(String field, int operator, Object value) {
		super();
		this.field = field;
		this.operator = operator;
		this.value = value;

	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperatorSql() {

		String retorno = null;

		switch (this.operator) {
		case EQUAL:
			retorno = " = ";
			break;
		case NOT_EQUAL:
			retorno = " != ";
			break;
		case GREATER_THAN:
			retorno = " > ";
			break;
		case LESS_THAN:
			retorno = " < ";
			break;
		case STARTS_WITH:
		case ENDS_WITH:
		case CONTAINS:
			retorno = " LIKE ";
			break;
		case NOT_STARTS_WITH:
		case NOT_ENDS_WITH:
		case NOT_CONTAINS:
			retorno = " NOT LIKE ";
			break;
		}

		return retorno;

	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public Object getValue() {

		Object retorno = value;

		if (value.getClass().equals(String.class)
				|| value.getClass().equals(Character.class)
				|| value.getClass().equals(char.class)) {

			if (getOperator() == STARTS_WITH || getOperator() == NOT_STARTS_WITH)
				retorno = retorno + LIKE_CHAR;
			else if (getOperator() == ENDS_WITH || getOperator() == NOT_ENDS_WITH)
				retorno = LIKE_CHAR + retorno;
			else if (getOperator() == CONTAINS || getOperator() ==  NOT_CONTAINS)
				retorno = LIKE_CHAR + retorno + LIKE_CHAR;

			retorno = String.format("\"%s\"", retorno.toString());
		}

		return retorno;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
