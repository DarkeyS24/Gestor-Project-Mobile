package br.com.drky.gestor_app.util;

import java.sql.SQLException;

import totalcross.db.sqlite.SQLiteUtil;
import totalcross.sql.Connection;
import totalcross.sql.Statement;
import totalcross.sys.Settings;

public class DatabaseManager {

	public static SQLiteUtil sqliteUtil;

	static {

		try {

			sqliteUtil = new SQLiteUtil(Settings.appPath, "gestor.db");

			Statement st = sqliteUtil.con().createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS tbcliente (codigo INTEGER PRIMARY KEY AUTOINCREMENT, cpfCnpj TEXT, email TEXT NULL, excluido INTEGER CHECK (excluido BETWEEN 0 AND 1), nome TEXT, sincronizado INTEGER CHECK (sincronizado BETWEEN 0 AND 1), telefone TEXT, tipo TEXT);");
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			return sqliteUtil.con();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}