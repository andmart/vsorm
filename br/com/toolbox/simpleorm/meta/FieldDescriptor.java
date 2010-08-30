package br.com.toolbox.simpleorm.meta;

import java.lang.reflect.Field;

public class FieldDescriptor {

	private String columnName;
	private Field attribute;
	private Class type;
	private boolean primaryKey;
	private int generator = 0;

	public FieldDescriptor(String field, Field attribute, Class type,
			boolean primaryKey, int generator) {
		super();
		this.columnName = field;
		this.attribute = attribute;
		this.type = type;
		this.generator = generator;
		this.primaryKey = primaryKey;
	}

	
	
	public int getGenerator() {
		return generator;
	}



	public void setGenerator(int generator) {
		this.generator = generator;
	}



	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String field) {
		this.columnName = field;
	}

	public Field getAttribute() {
		return attribute;
	}

	public void setAttribute(Field attribute) {
		this.attribute = attribute;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}


	public Object valueDB(Object o) {
		
		Object retorno = o;
		
		if (o.getClass().equals(String.class)
				|| o.getClass().equals(Character.class)
				|| o.getClass().equals(char.class)) {

			retorno = String.format("\"%s\"", o);
			
		}		
		return retorno;
	}
}
