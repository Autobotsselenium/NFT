package nft.TestExecutor;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DB {
	static String URL = "jdbc:oracle:thin:@sdb00351.ute.fedex.com:1526:SDB00351";
	static String USER = "REV_SHIP";
	static String PASS = "10478215eD17ou5T741hN074f";
	static String run;
	static LinkedHashMap<String, LinkedHashMap<String, String>> storeColumns = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	static LinkedList<String> QCID = new LinkedList<String>();
	static LinkedList<String> ColumnName = new LinkedList<String>();

	
/*public static void main(String args[]) throws SQLException{
	retriveDB();
	System.out.println(storeColumns);
	DBConnect db=new DBConnect();
	System.out.println(getDBTtestdata1("133","CountryCode"));
	System.out.println(getDBTtestdata1("133","NAME"));
	
	

}*/
	
	public static void retriveDB() throws SQLException {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception E) {
			E.printStackTrace();
		}
		
		Connection connection = DriverManager.getConnection(URL, USER, PASS);
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String sql_get_QCID = "Select QC_ID from INET_FUNCTIONAL_DATA where QC_ID=133";
		
		System.out.println("sql_mps_check output" + sql_get_QCID);
		
		ResultSet resultSetQCID = stmt.executeQuery(sql_get_QCID);
		while (resultSetQCID.next()) {
			QCID.add(resultSetQCID.getString(1));
			

		}

		String sql_mps_check = "Select * from INET_FUNCTIONAL_DATA where QC_ID=133";
		
		ResultSet resultSet = stmt.executeQuery(sql_mps_check);
		
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsize = rsmd.getColumnCount();

		for (int i = 1; i <= columnsize; i++) {
			LinkedHashMap<String, String> columnValues = new LinkedHashMap<String, String>();
			resultSet.first();
			int j = 0;
			do {
//				System.out.println(resultSet.getString(i));
				columnValues.put(QCID.get(j), resultSet.getString(i));
				j++;

			} while (resultSet.next());
			ColumnName.add(rsmd.getColumnName(i));
			storeColumns.put(rsmd.getColumnName(i), columnValues);
//			System.out.println("columnValues"+columnValues);
		}
		connection.close();
//		System.out.println("***************************************************");

	}
	
	 static String getDBTtestdata1(String QCID, String columnName) {
		 
		if (columnName.equalsIgnoreCase("CountryCode")) {columnName="WDPA_COUNTRYCODE";}
		else if (columnName.equalsIgnoreCase("NAME")) {columnName="NAME";}
		else if (columnName.equalsIgnoreCase("NAME")) {columnName="NAME";}
		else if (columnName.equalsIgnoreCase("PackageAndShipment_ServiceType")) {columnName="SVCTYPCD";}
		else if (columnName.equalsIgnoreCase("PackageAndShipment_PackageType")) {columnName="PACKAGING";}
		else if (columnName.equalsIgnoreCase("PackageAndShipment_Weight")) {columnName="SHPWGT";}
		else if (columnName.equalsIgnoreCase("PackageAndShipment_NumberofPackages")) {columnName="SHPPCEQTY";}
		else if (columnName.equalsIgnoreCase("fromZipCode")) {columnName="SNDRPSTLCD";}
		else if (columnName.equalsIgnoreCase("fromCompany")) {columnName="SNDRCONM";}
		else if (columnName.equalsIgnoreCase("fromAddress2")) {columnName="SNDRADDRLN2";}
		else if (columnName.equalsIgnoreCase("fromAddress1")) {columnName="SNDRADDRLN1";}
		else if (columnName.equalsIgnoreCase("fromCity")) {columnName="SNDRCITYNM";}
		else if (columnName.equalsIgnoreCase("fromPhoneNo")) {columnName="SNDRPHONENBR";}
		else if (columnName.equalsIgnoreCase("fromState")) {columnName="SNDRSTCD";}
		else if (columnName.equalsIgnoreCase("fromcountry")) {columnName="SNDRCNTRYCD";}
		else if (columnName.equalsIgnoreCase("fromContactName")) {columnName="SNDRNM";}
		else if (columnName.equalsIgnoreCase("ToZipCode")) {columnName="RECPPSTLCD";}
		else if (columnName.equalsIgnoreCase("ToCompany")) {columnName="RECPCONM";}
		else if (columnName.equalsIgnoreCase("ToAddress2")) {columnName="RECPADDRLN2";}
		else if (columnName.equalsIgnoreCase("ToAddress1")) {columnName="RECPADDRLN1";}
		else if (columnName.equalsIgnoreCase("ToCity")) {columnName="RECPCITYNM";}
		else if (columnName.equalsIgnoreCase("ToPhoneNo")) {columnName="RECPPHONENBR";}
		else if (columnName.equalsIgnoreCase("ToState")) {columnName="RECPSTCD";}
		else if (columnName.equalsIgnoreCase("Tocountry")) {columnName="RECPCNTRYCD";}
		else if (columnName.equalsIgnoreCase("ToContactName")) {columnName="RECPNM";}
		else if (columnName.equalsIgnoreCase("QC_ID")) {columnName="QC_ID";}
		else if (columnName.equalsIgnoreCase("NAME")) {columnName="NAME";}
		else if (columnName.equalsIgnoreCase("Run")) {columnName="Run";}
		else if (columnName.equalsIgnoreCase("TestScenario")) {columnName="TestScenario";}
		else if (columnName.equalsIgnoreCase("Userid")) {columnName="LOGINID";}
		else if (columnName.equalsIgnoreCase("Password")) {columnName="Pass_word";}
		else if (columnName.equalsIgnoreCase("PageNavigation_ButtonName")) {columnName="Navigation";}
		else if (columnName.equalsIgnoreCase("ConfirmButton")) {columnName="ConfirmButton";}
		else if (columnName.equalsIgnoreCase("LabelButtons")) {columnName="LabelButtons";}

				

		Iterator<?> it = storeColumns.get(columnName).keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
		//System.out.println(i);
			if (it.next().equals(QCID))
				
			{
				Iterator<?> it1 = storeColumns.get(columnName).values().iterator();

				int j = 0;
				while (it1.hasNext()) {
					//System.out.println(i);
					if (i == j) {
						
						return (String) it1.next();

					}
					it1.next();
					j++;
				}
			}

			i++;			
		}
		return null;
	}

	private static void elseif(boolean equalsIgnoreCase) {
		// TODO Auto-generated method stub
		
	}

}