package br.com.toolbox.simpleorm.dao;

import java.util.List;
import java.util.Map;

import br.com.toolbox.simpleorm.exception.SimpleOrmException;
import br.com.toolbox.simpleorm.meta.FieldDescriptor;
import br.com.toolbox.simpleorm.meta.ObjectDescriptor;
import br.com.toolbox.simpleorm.query.Query;

public interface Dao {

	// public final static String TABLE = "table";
	// public final static String FIELDS_LIST = "fields";

	Long insert(ObjectDescriptor params, Object o) throws SimpleOrmException;

	void update(ObjectDescriptor params, Object o) throws SimpleOrmException;

	List<?> select(ObjectDescriptor o, Query q) throws SimpleOrmException;

	void delete(Class cls, List<Object> criterias) throws SimpleOrmException;

	void createTable(String table, List<FieldDescriptor> fields)throws
	 SimpleOrmException ;

	void setProperties(Map<Object, Object> props) throws SimpleOrmException;

	Map<Object, Object> getProperties() throws SimpleOrmException;

	Object getProperty(Object key) throws SimpleOrmException;

}
