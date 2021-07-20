package extent;


import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import com.aventstack.extentreports.reporter.configuration.Theme;



public class ExtentReportsClassVersion3{
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest logger;
	ExtentTest logger1;
	
	
	@BeforeTest
	public void startReport(){
		
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/test-output/STMExtentReport.html");
		extent = new ExtentReports ();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", "SoftwareTestingMaterial");
		extent.setSystemInfo("Environment", "Automation Testing");
		extent.setSystemInfo("User Name", "Rajkumar SM");
		
		htmlReporter.config().setDocumentTitle("Title of the Report Comes here");
		htmlReporter.config().setReportName("Name of the Report Comes here");

		htmlReporter.config().setTheme(Theme.STANDARD);
	}
		
	public static void main(String[] args) {
		try {
			TestListenerAdapter tla = new TestListenerAdapter();
			final TestNG testng = new TestNG();

			testng.setTestClasses(new Class[] { ExtentReportsClassVersion3.class });
			// ArrayList<String> testmethods = new ArrayList<String>();
			
				testng.setDataProviderThreadCount(1);
			// testmethods.add("TestInitPrallel");
			testng.setPreserveOrder(true);
			testng.addListener(tla);

			Runnable r = new Runnable() {
				@Override
				public void run() {
					testng.run();
				}
			};
			Thread runner = new Thread(r);
			
				runner.start();
			runner.join();

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void passTest(){
		logger = extent.createTest("passTest");
		logger1=logger.createNode("testscenario 1","");
		Assert.assertTrue(true);
		logger1.log(Status.PASS, MarkupHelper.createLabel("Test Case Passed is passTest", ExtentColor.GREEN));
		logger1=logger.createNode("testscenario 2","");
		Assert.assertTrue(true);
		logger1.log(Status.PASS, MarkupHelper.createLabel("Test Case Passed is passTest1", ExtentColor.GREEN));
	
	}
	@Test
	public void failTest(){
		logger = extent.createTest("passTest");
		logger1=logger.createNode("testscenario 1","");
		Assert.assertTrue(true);
		logger1.log(Status.FAIL, MarkupHelper.createLabel("Test Case Passed is passTest", ExtentColor.RED));
		logger1=logger.createNode("testscenario 2","");
		Assert.assertTrue(true);
		logger1.log(Status.FAIL, MarkupHelper.createLabel("Test Case Passed is passTest1", ExtentColor.RED));
	
	}
	@Test
	public void passfailTest(){
		logger = extent.createTest("passTest");
		logger1=logger.createNode("testscenario 1","");
		Assert.assertTrue(true);
		logger1.log(Status.PASS, MarkupHelper.createLabel("Test Case Passed is passTest", ExtentColor.GREEN));
		logger1=logger.createNode("testscenario 2","");
		Assert.assertTrue(true);
		logger1.log(Status.FAIL, MarkupHelper.createLabel("Test Case Passed is passTest1", ExtentColor.RED));
	
	}
	@AfterMethod
	public void getResult(ITestResult result){
		if(result.getStatus() == ITestResult.FAILURE){
			//logger.log(Status.FAIL, "Test Case Failed is "+result.getName());
			//MarkupHelper is used to display the output in different colors
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
		}else if(result.getStatus() == ITestResult.SKIP){
			//logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));	
		}
	}
	@AfterTest
	public void endReport(){
		extent.flush();
    }
}