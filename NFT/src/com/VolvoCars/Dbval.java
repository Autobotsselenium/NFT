package com.VolvoCars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class Dbval {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String url="jdbc:sqlserver://qa-vssm.crm4.dynamics.com:1433;database=demo;encrypt=true;trustServerCertificate=false;loginTimeout=30";
		String username = "jgnanara@volvocars.com";
		String password = "Amex@123%";
			/*tring dbURL = "jdbc:sqlserver://qa-vssm.crm4.dynamics.com";
		
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String url = "jdbc:sqlserver://qa-vssm.crm4.dynamics.com;";
*/
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection(url, username, password);

		/* Connection con = DriverManager.getConnection(dbURL,username,password); */
		Connection con = DriverManager.getConnection( "jdbc:sqlserver://qa-vssm.crm4.dynamics.com:1433;databaseName=dbname", username, password );
		Statement st = con.createStatement();
		String selectquery = "SELECT * FROM vssm_car WHERE vssm_carid='a83ef5b4-695f-eb11-a812-000d3a224a6b'";
		ResultSet rs = st.executeQuery(selectquery);
		while (rs.next()) {
			System.out.println(rs.getString("vssm_carid"));
		}

		con.close();


	}

}
