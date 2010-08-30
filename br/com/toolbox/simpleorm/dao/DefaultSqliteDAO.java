package br.com.toolbox.simpleorm.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import br.com.toolbox.simpleorm.DBManager;
import br.com.toolbox.simpleorm.annotation.Column;
import br.com.toolbox.simpleorm.exception.SimpleOrmException;
import br.com.toolbox.simpleorm.meta.FieldDescriptor;
import br.com.toolbox.simpleorm.meta.ObjectDescriptor;

import br.com.toolbox.simpleorm.query.*;

public class DefaultSqliteDAO extends DefaultSqlDAO {

	private String dbName = "data.db";
	protected Connection conn;
	private String CREATE_TABLE = "CREATE TABLE @table (@fields)";

	public DefaultSqliteDAO(Map<String, String> params) {
		
		if( params.containsKey(DBManager.DBNAME) )
			this.dbName = params.get(DBManager.DBNAME);
		
		try {
			connect();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected Connection connect() throws ClassNotFoundException, SQLException {

		if (this.conn == null) {
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		}
		
		return this.conn;

	}
	
	

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		disconnect();
	}

	protected void disconnect() throws SQLException {
		conn.close();
	}



	private String class2SqliteDataType(Class cls) {

		String retorno = null;

		if (cls.equals(int.class) || cls.equals(Integer.class)
				|| cls.equals(Short.class) || cls.equals(short.class)
				|| cls.equals(Byte.class) || cls.equals(byte.class)
			|| cls.equals(Long.class) || cls.equals(long.class))
			retorno = "INTEGER";
		else if (cls.equals(float.class) || cls.equals(Float.class)
				|| cls.equals(double.class) || cls.equals(Double.class))
			retorno = "REAL";
		else
			retorno = "TEXT";

		return retorno;

	}

	@Override
	public void createTable(String table, List<FieldDescriptor> fields)
			throws SimpleOrmException {

		try {

			try {
				conn.createStatement().execute("DROP TABLE " + table);
			} catch (Exception w) {
//				this is a precaution cause if the table don't exists, a SqlException
//				will be raised.
//				If I don't drop the table before the create table statement,
//				a SqlException will be raised too.
			}
			StringBuffer buffer = new StringBuffer();
			String sql = CREATE_TABLE.replace("@table", table);
			for (FieldDescriptor fd : fields) {
				buffer.append(fd.getColumnName()
						+ " "
						+ class2SqliteDataType(fd.getType())
						+ " "
						+ (fd.isPrimaryKey() ? " PRIMARY KEY " : "")
						+ (fd.getGenerator() == Column.AUTO ? " AUTOINCREMENT, "
								: ","));

			}
			sql = sql.replace("@fields", buffer.toString().subSequence(0,
					buffer.length() - 2));
			System.out.println(sql);
			this.conn.createStatement().execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SimpleOrmException(e);
		}

	}


	
}
