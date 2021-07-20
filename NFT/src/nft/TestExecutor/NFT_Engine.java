package nft.TestExecutor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;

import nft.Report.ExecutionDetails;
import nft.Report.HTMLReportTemplate;
import nft.Report.TestResult;

public class NFT_Engine {
	private static final ExtentColor GREEN = null;
	private static final ExtentColor RED = null;
	public  static LinkedHashMap<String, TestResult> testResults = new LinkedHashMap<>();
	private static String resultFolderPath;
	private static  ExecutionDetails executionDetails = new ExecutionDetails();
	private static TestResult TestResult = new TestResult();
	private static LinkedHashSet<Object[]> dataProviderData;
	public  String excelPath;
	private LinkedHashMap<String, String> data;
	public  int rowindex;
	//public static ExtentTest test;
	//Added and commented by ragu
	//public static ExtentTest test1,test2;
	//public static com.aventstack.extentreports.ExtentReports extent;
	public String testCaseName = TestResult.getTestScenarioName();
	public String testDescription="Testcase Description";
	public  Map extentTestMap = new HashMap();
	private WebDriver driver;
	static NFT_Engine nft;
	
	 static ExtentReports extent;
	
	  ExtentTest parent;
	  ExtentTest logger;
	
	ExtentHtmlReporter htmlReporter;
	public  static String ExtendReportPath;
	
   // static ExtentReports extent;
	public ApplicationMainClass amc;
	public Properties prop;
    /*public NFT_Engine(ApplicationMainClass amc,Properties prop)
    {
    	this.amc = amc;
    	this.prop = prop;
//    	System.out.println("Test data:"+prop.var);
    }*/
	@BeforeSuite
	public void beforeSuite() throws Exception{
		
	
		
		ExtendReportPath=resultFolderPath+"ExtendReport.html";
		
	htmlReporter = new ExtentHtmlReporter(ExtendReportPath);
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		htmlReporter.config().setDocumentTitle("Automation report");
		htmlReporter.config().setReportName("Automation result");
		htmlReporter.config().setTheme(Theme.DARK);

		try {

			extent.setSystemInfo("Host Name", "host");
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("IP Address", InetAddress.getLocalHost().getHostAddress());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
		//extent.loadConfig(new File("./extent-config.xml"));
		
		
	}
	
	@BeforeTest
	public void beforeTest() {
		System.out.println("BTest .......");
		try {
			Runtime.getRuntime().exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");							
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	@Test(dataProvider = "DataProvider")
	public void TestInitPrallel(int rowIndex, String testCaseId)
			throws Exception {
		try {
			//InitiateExecution initiateExecution = new InitiateExecution(driver,rowIndex, testCaseId, executionDetails, testResults, resultFolderPath);
			
			InitiateExecution initiateExecution = new InitiateExecution(rowIndex, testCaseId, executionDetails, testResults, resultFolderPath,nft);
			initiateExecution.execute();
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}

	@DataProvider(name = "DataProvider", parallel = true)
	public Iterator<Object[]> TestNG_initParallel() throws Exception {
		return dataProviderData.iterator();
	}
	//commented by vishnu
	/*public static synchronized ExtentTest getTest() {
        return (ExtentTest)extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }*/
 
    public static synchronized void endTest() {
        //extent.endTest((ExtentTest)extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    	
    }
 
    public static synchronized void startTest(String testName, String desc) {
    	
   /*
    	logger = extent.createTest(testName,desc);
    	
    	
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), logger);
        return logger;*/
    }
    
    public  synchronized ExtentTest startExtendTest(String testName, String desc) {
    	
    	   // test = extent.startTest(testName, desc);
    			parent=extent.createTest(testName);
    	    	//test1 = test.createNode(testName, desc);
    	    	//test1 = test.createNode(testName, desc);	
    	    	
    	        extentTestMap.put((int) (long) (Thread.currentThread().getId()), logger);
    	        return logger;
    	    }
    
    public  synchronized ExtentTest createExtendNode(String testName, String desc) {
    	
    	
    		logger=parent.createNode(testName);
    	
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), logger);
        return logger;
    }

    
    public static synchronized void CreateNode(Status testName,String desc) {
    	
    	/*if(testName==Status.PASS)
    		logger.log(Status.INFO, MarkupHelper.createLabel(desc, GREEN));
    	else if(testName==Status.FAIL)
    		logger.log(Status.INFO, MarkupHelper.createLabel(desc, RED));*/
    	}
    

	@AfterTest
	public void afterTest() throws Exception {
		System.out.println("After Test");
		Date startDate = executionDetails.getStartDate();
		Date endDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss a");
		long diffMs = endDate.getTime() - startDate.getTime();
		long diffSec = diffMs / 1000;
		long min = diffSec / 60;
		long sec = diffSec % 60;
		String diffTime = min + " min and " + sec + " sec.";
		executionDetails.setEndtime(dateFormat.format(endDate));
		executionDetails.setTimeTaken(diffTime);
		System.out.println("Total Testcase Executed : "
				+ InitiateExecution.TotalTC.size() + "");
		System.out.println("Total Testcase Passed : "
				+ InitiateExecution.statusPass + "");
		System.out.println("Total Testcase Failed : "
				+ InitiateExecution.statusFail + "");
		HTMLReportTemplate htmlReportTemplate = new HTMLReportTemplate();
		htmlReportTemplate.generateReport(executionDetails, testResults,
				resultFolderPath,nft);

		File srcDir = new File("" + resultFolderPath + "");
		File dstDir = new File("./FinalReport");
		for (File srcFile : srcDir.listFiles()) {
			if (!srcFile.isDirectory()) {
				FileUtils.copyFileToDirectory(srcFile, dstDir);
			}
		}

	}
	
	
	@AfterSuite
	public void afterSuite(){
		System.out.println("After Suite");
		/*for (Entry<String, TestResult> entry : testResults.entrySet()) {
			String key = entry.getKey();
			TestResult tR = new TestResult();
			tR = entry.getValue();
			LinkedHashMap<String, String> finalStatusCheck = tR.getXbridTC_IDStatus();

			for (Entry<String, String> e1 : finalStatusCheck.entrySet()) {
				String Xid = e1.getKey();
				String status = e1.getValue();

				if (status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Running")) {
					status = "Fail";
				}
				finalStatusCheck.put(Xid, status);
			}
			tR.setXbridTC_IDStatus(finalStatusCheck);
			testResults.put(key, tR);
		}*/
		extent.flush();
	
		//extent.close();
		//driver.quit();
		//extent.flush();
		
	}
	
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException{
		//test.log(LogStatus.INFO, test.addScreenCapture(CaptureScreenShot.captureScreen(driver, CaptureScreenShot.generateFileName(result))));
		//test.log(LogStatus.PASS, "Reported ended Successfully");
		//extent.endTest(test);
	}
	
	public void run(LinkedHashSet<Object[]> dataProviderData,
			ExecutionDetails executionDetails,
			LinkedHashMap<String, TestResult> testResults,
			String resultFolderPath, NFT_Engine nft) throws Exception {
		
		NFT_Engine.dataProviderData = dataProviderData;
		NFT_Engine.resultFolderPath = resultFolderPath;
		NFT_Engine.executionDetails = executionDetails;
		NFT_Engine.testResults = testResults;
		NFT_Engine.nft=nft;

		boolean flag = false;
		try {
			TestListenerAdapter tla = new TestListenerAdapter();
			final TestNG testng = new TestNG();

			testng.setTestClasses(new Class[] { NFT_Engine.class });
			// ArrayList<String> testmethods = new ArrayList<String>();
			if (Properties.threadPoolCount > 1)
				testng.setDataProviderThreadCount(Properties.threadPoolCount);
			else
				testng.setDataProviderThreadCount(1);
			// testmethods.add("TestInitPrallel");
			testng.setPreserveOrder(true);
			testng.addListener(tla);
			testng.run();
			/*Runnable r = new Runnable() {
				@Override
				public void run() {
					testng.run();
				}
			};*/
			/*Thread runner = new Thread(r);
			if (flag)
				runner.run();
			else
				runner.start();
			runner.join();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}