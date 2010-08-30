package br.com.toolbox.simpleorm.meta;

import java.util.ArrayList;
import java.util.List;

public class ObjectDescriptor {

	private Class clazz;
	private String table;
	private List<FieldDescriptor> fields;
	private List<FieldDescriptor> primaryKey;
	private int generator;
//	private String selectFields;
//	private String selectTables;
//	private String selectJoins;
//	
	
	
	public ObjectDescriptor(String table, List<FieldDescriptor> fields,
			List<FieldDescriptor> primaryKey, int generator) {
		super();
		this.table = table;
		this.fields = fields;
		this.primaryKey = primaryKey;
		this.generator = generator;
	}
	
	
	
	public Class getClazz() {
		return clazz;
	}



	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}



	public ObjectDescriptor() {
	}


	public int getGenerator() {
		return generator;
	}

	public void setGenerator(int generator) {
		this.generator = generator;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<FieldDescriptor> getFields() {
		return fields;
	}

	public void setFields(List<FieldDescriptor> fields) {
		primaryKey = new ArrayList<FieldDescriptor>();
		for (FieldDescriptor fieldDescriptor : fields) {
			if (fieldDescriptor.isPrimaryKey()) {
				primaryKey.add(fieldDescriptor);
				generator = fieldDescriptor.getGenerator();
			}
		}
		this.fields = fields;
	}

	public List<FieldDescriptor> getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(List<FieldDescriptor> primaryKey) {
		this.primaryKey = primaryKey;
	}

}
