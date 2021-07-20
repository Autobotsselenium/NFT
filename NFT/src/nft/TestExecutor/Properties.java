

//package com.MAGR;
package nft.TestExecutor;

/**
 * @author YR5026406
 * This class is used for defining the configuration properties used in this project.
 */
public class Properties {

	
	/*
	 * configuration Properties for Selenium and TestNG.
	 */
	
	public static String defaultUrl="https://qa-vssm.crm4.dynamics.com";
	public static String url_Level="L3";
	public static int implicitWait=5;
	public static int pageLoadTimeout=200;
	public static int scriptTimeout=200;
	public static String ieDriverPath="Resources/Drivers/IEDriverServer.exe";
	//public static String EdgeDriverPath="Resources/Drivers/MicrosoftWebDriver2.exe";
	public static String chromeDriverPath="Resources/Drivers/chromedriver.exe";
	public static String geckoDriverPath="Resources/Drivers/geckodriver.exe";
	public static String edgeDriverPath="Resources/Drivers/MicrosoftWebDriver.exe";
	public static String firefoxBinaryPath="C:\\Program Files\\Mozilla Firefox\\Firefox.exe";
	public static String[] excelSheetPath= {"C:\\Users\\jgnanara\\Downloads\\TestUNFT\\TestUNFT\\Testdata\\NFT_Run_1.xlsx"};
	public static String[] sheetName= {""};
	//C:\\Users\\vv5051241\\Desktop\\Final INET_FXF test cases.xlsx
	//public static String XbridexcelSheetPath="C:\\Users\\vv5051241\\Desktop\\INET_XBRID_NFT MAPPING.xlsx";
	//C:\\Users\\vv5051241\\Desktop\\VishnuINET\\INET Test data sheet.xlsx
	public static String logPath="./UNFTlog.txt";
	
	// Browser Names are: Chrome/IE/Firefox/EDGE/Remote/ChromeHeadless
	public static String executionBrowserName="Chrome";
	public static String resultsPath="./Result/";

	public static String ipAddress="172.26.59.88";
	public static String portNumber="4444";
	public static String additionalCapabilities="";
	public static String downloadFilepath="";

	
	/*
	 * Configuration for test data Excel sheet
	 */
	
	public static String testCaseID="QC_ID";
	public static String testCaseName="NAME";
	public static String testCaseDescription="DESC";
	public static String runFlag="Run";
	public static String testScenario="TestScenario";
	public static int testCaseIdColumnIndex=0;
	//public static int runFlagColumnIndex;
	public static int testCaseNameColumnIndex=1;
	public static int testCaseStatus=4;
	public static int reportPath=7;
	public static int excel_ipAddress=5;
	public static int excel_ThreadPoolCount_ColumnIndex=6;
	
	//public static String Level="L2";
	public static String Level3="L3";
	public static String Level6="L6";
	
	public static String US_App_Url="https://wwwdev.idev.fedex.com";
	////wwwdrt.idev.fedex.com/en-us/home.html
	////wwwdev.idev.fedex.com/en-us/home.html
	public static String App_Pwd="Test1234";
	
	public static int threadPoolCount=1;
//	public static int testCaseDescColumnIndex=;
	
	
}
