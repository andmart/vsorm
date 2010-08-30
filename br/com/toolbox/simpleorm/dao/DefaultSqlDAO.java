package br.com.toolbox.simpleorm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.toolbox.simpleorm.annotation.Column;
import br.com.toolbox.simpleorm.exception.SimpleOrmException;
import br.com.toolbox.simpleorm.meta.FieldDescriptor;
import br.com.toolbox.simpleorm.meta.ObjectDescriptor;
import br.com.toolbox.simpleorm.query.Criteria;
import br.com.toolbox.simpleorm.query.Query;

public abstract class DefaultSqlDAO implements Dao {

	private String SELECT = "SELECT * FROM @table ";
	private String INSERT = "INSERT INTO @table(@fields) VALUES(@values)";
	private String DELETE = "DELETE FROM @table WHERE @conditions";
	private String UPDATE = "UPDATE @table SET @values @where";

	private Map properties = new HashMap<Object, Object>();

	protected Connection conn;

	protected abstract Connection connect() throws ClassNotFoundException,
			SQLException;


	@Override
	public void setProperties(Map<Object, Object> props)
			throws SimpleOrmException {

		this.properties = props;

	}

	@Override
	public Map<Object, Object> getProperties() throws SimpleOrmException {

		return properties;
	}

	@Override
	public Object getProperty(Object key) throws SimpleOrmException {

		return properties.get(key);
	}

	public Long insert(ObjectDescriptor params, Object o)
			throws SimpleOrmException {

		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		String sql = INSERT.replaceFirst("@table", params.getTable());
		Long id = 0L;

		try {

			for (FieldDescriptor field : (List<FieldDescriptor>) params
					.getFields()) {
				if (field.getGenerator() == Column.AUTO)
					continue;
				fields.append(field.getColumnName() + ",");
				values.append(field.valueDB(field.getAttribute().get(o)) + ",");
			}
			sql = sql.replaceFirst("@fields", fields.toString().substring(0,
					fields.toString().length() - 1));
			sql = sql.replaceFirst("@values", values.toString().substring(0,
					values.toString().length() - 1));

			Statement stmt = connect().createStatement();
			stmt.execute(sql);

			ResultSet rs = stmt.getGeneratedKeys();
			id = rs.getLong(1);

			stmt.close();

		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}

		return id;
	}

	public void update(ObjectDescriptor params, Object o)
			throws SimpleOrmException {

		StringBuilder fields = new StringBuilder();
		StringBuilder where = new StringBuilder(" WHERE ");

		try {

			String sql = UPDATE.replaceFirst("@table", params.getTable());

			for (FieldDescriptor field : (List<FieldDescriptor>) params
					.getFields()) {
				fields.append(String.format("%s=%s,", field.getColumnName(),
						field.valueDB(field.getAttribute().get(o))));

			}
			sql = sql.replaceFirst("@values", fields.toString().substring(0,
					fields.toString().length() - 1));

			boolean first = true;

			for (FieldDescriptor fd : params.getPrimaryKey()) {
				if (first) {
					where.append(fd.getColumnName() + " = "
							+ fd.valueDB(fd.getAttribute().get(o)));
					first = false;
				} else {
					where.append(" AND " + fd.getColumnName() + " = "
							+ fd.valueDB(fd.getAttribute().get(o)));
				}
			}

			sql = sql.replace("@where", where.toString());

			Statement stmt = connect().createStatement();
			stmt.execute(sql);
			stmt.close();

		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}

	}

	private Query translateQuery(Query q, ObjectDescriptor od) {

		Query retorno = q;

		for (Criteria c : q.getCriterias()) {
			for (FieldDescriptor fd : od.getFields()) {
				if (fd.getAttribute().getName().equals(c.getField())) {
					c.setField(fd.getColumnName());
					break;
				}
			}
		}

		return retorno;
	}

	public void delete(Class cls) {

	}

	public void delete(Class cls, Object o) {

	}

	public void delete(Class cls, List<Object> criterias) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<?> select(ObjectDescriptor o, Query q)
			throws SimpleOrmException {

		List retorno = new ArrayList();

		String sql = SELECT.replace("@table", o.getTable());
		try {

			Statement stmt = connect().createStatement();

			if (q != null) {
				sql = sql + " WHERE " + translateQuery(q, o).query();
			}
			System.out.println(sql);

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next())
				retorno.add(rs2Object(o, rs));

		} catch (Exception e) {
			throw new SimpleOrmException(e);
		}

		return retorno;
	}

	private List<String> rowNames(ResultSet rs) throws SQLException {
		List<String> lstRetorno = new ArrayList<String>();

		int index = rs.getMetaData().getColumnCount();
		for (int i = 1; i < index + 1; i++)
			lstRetorno.add(rs.getMetaData().getColumnName(i));
		return lstRetorno;
	}

	private Object getDbValue(ResultSet rs, Class cls, String column)
			throws SQLException {

		Object retorno = null;

		if (cls.equals(Long.class) || cls.equals(long.class))
			retorno = rs.getLong(column);
		else if (cls.equals(int.class) || cls.equals(Integer.class))
			retorno = rs.getInt(column);
		else if (cls.equals(Short.class) || cls.equals(short.class))
			retorno = rs.getShort(column);
		else if (cls.equals(Byte.class) || cls.equals(byte.class))
			retorno = rs.getByte(column);
		else if (cls.equals(float.class) || cls.equals(Float.class))
			retorno = rs.getFloat(column);
		else if (cls.equals(double.class) || cls.equals(Double.class))
			retorno = rs.getDouble(column);
		else
			retorno = rs.getObject(column);

		return retorno;
	}

	private Object rs2Object(ObjectDescriptor od, ResultSet rs)
			throws SQLException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException {

		Object retorno = od.getClazz().newInstance();
		List<String> columnNames = rowNames(rs);
		java.lang.reflect.Field fld;
		for (FieldDescriptor field : od.getFields()) {

			for (String column : columnNames) {
				if (field.getColumnName().equals(column)) {
					fld = retorno.getClass().getDeclaredField(
							field.getAttribute().getName());
					fld.setAccessible(true);
					fld.set(retorno, getDbValue(rs, field.getType(), column));
					break;
				}
			}

		}
		return retorno;
	}

}
