package br.com.toolbox.simpleorm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.toolbox.simpleorm.annotation.Column;

import br.com.toolbox.simpleorm.annotation.Table;
import br.com.toolbox.simpleorm.dao.Dao;
import br.com.toolbox.simpleorm.dao.DefaultSqliteDAO;
import br.com.toolbox.simpleorm.exception.SimpleOrmException;
import br.com.toolbox.simpleorm.meta.FieldDescriptor;
import br.com.toolbox.simpleorm.meta.ObjectDescriptor;

import br.com.toolbox.simpleorm.query.Query;

import br.com.toolbox.simpleorm.util.Session;

public class DBManager {

	private Dao dao = null;

	public static final int SQLITE = 1;

	public static final String DBNAME = "dbname";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String JDBCURL = "jdbcurl";

	private HashMap<String, ObjectDescriptor> objectDescriptorCache;

	private void init() {

		objectDescriptorCache = new HashMap<String, ObjectDescriptor>();
		Session.getSession()
				.set("objectDescriptorCache", objectDescriptorCache);
	}

	public DBManager() {
		init();
	}

	public DBManager(int dbType, Map<String, String> params) {

		init();
		switch (dbType) {
		case SQLITE:
			this.setDao(new DefaultSqliteDAO(params));
		}
	}

	public void register(List<Class> c) throws SimpleOrmException {
		for (Class class1 : c) {
			register(class1);
		}

	}

	public void register(Class c) throws SimpleOrmException {
		try {
			objectDescriptorCache.put(c.getName(), readAnnotations(c));
		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
	}

	private String readTable(Class c) throws SimpleOrmException {

		String retorno = null;

		try {
			Class cls = Class
					.forName("br.com.toolbox.simpleorm.annotation.Table");
			Table table = (Table) c.getAnnotation(cls);
			retorno = table.name();
		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
		return retorno;
	}

	public void createAllTables() throws SimpleOrmException {

		ObjectDescriptor od = null;

		for (String key : objectDescriptorCache.keySet()) {
			od = objectDescriptorCache.get(key);
			dao.createTable(od.getTable(), od.getFields());

		}

	}

	private ObjectDescriptor getObjectDescriptorForClass(Class c)
			throws SimpleOrmException {
		ObjectDescriptor od = null;
		if (!objectDescriptorCache.containsKey(c.getName())) {
			od = readAnnotations(c);
			objectDescriptorCache.put(c.getName(), od);
		} else {
			od = objectDescriptorCache.get(c.getName());
		}
		return od;
	}

	public void createTable(Class c) throws SimpleOrmException {

		ObjectDescriptor od = getObjectDescriptorForClass(c);

		dao.createTable(od.getTable(), od.getFields());

	}

	private List<FieldDescriptor> readFields(Class c) throws SimpleOrmException {

		List<FieldDescriptor> retorno = new ArrayList<FieldDescriptor>();

		try {

			Class cls = Class
					.forName("br.com.toolbox.simpleorm.annotation.Column");

			java.lang.reflect.Field[] fields = c.getDeclaredFields();

			String fieldName = null;

			FieldDescriptor fd;

			Column fldField;

			for (java.lang.reflect.Field field : fields) {

				if (field.isAnnotationPresent(cls)) {

					fldField = ((Column) (field.getAnnotation(cls)));

					fieldName = fldField.name();

					field.setAccessible(true);

					fd = new FieldDescriptor(fieldName, field, field.getType(),
							fldField.primaryKey(), fldField.generator());

					retorno.add(fd);
				}

			}

		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}

		return retorno;
	}

		private ObjectDescriptor readAnnotations(Class c) throws SimpleOrmException {

		ObjectDescriptor retorno = new ObjectDescriptor();

		retorno.setTable(readTable(c));

		retorno.setClazz(c);

		retorno.setFields(readFields(c));

	
		return retorno;

	}

	
	public void create(Object o) throws SimpleOrmException {

		try {
			ObjectDescriptor params = getObjectDescriptorForClass(o.getClass());
			Object id = this.dao.insert(params, o);

			if (params.getGenerator() != Column.NONE) {
				params.getPrimaryKey().get(0).getAttribute()
						.setAccessible(true);
				params.getPrimaryKey().get(0).getAttribute().set(o, id);
			}

		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
	}

	public void update(Object o) throws SimpleOrmException {

		try {
			ObjectDescriptor params = getObjectDescriptorForClass(o.getClass());
			this.dao.update(params, o);

		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
	}

	public List<?> select(Class c) throws SimpleOrmException {
		try {
			List retorno = dao.select(getObjectDescriptorForClass(c), null);
			return retorno;
		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
	}

	public List<?> select(Class c, Query q) throws SimpleOrmException {
		try {
			List retorno = dao.select(getObjectDescriptorForClass(c), q);
			return retorno;
		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
	}

	public void update(Object o, Query q) throws SimpleOrmException {
		try {
			List<Object> lst = (List<Object>) select(o.getClass(), q);
			for (Object object : lst) {
				for (FieldDescriptor fd : objectDescriptorCache.get(
						o.getClass().getName()).getFields()) {
					if (!fd.isPrimaryKey())
						fd.getAttribute().set(object, fd.getAttribute().get(o));
				}
				update(object);
			}
		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}
	}

	public void delete(Object o) {
	}

	public void delete(Object o, Query q) {
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public Dao getDao() {
		return this.dao;
	}

}
