package nft.TestExecutor;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.Detainted;
import javax.annotation.Signed;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;


import nft.Report.ExecutionDetails;
import nft.Report.TestResult;

/**
 * @author VV5051241
 * 
 * 
 */
public class Session {
	// public int noWaitTime = 1;
	public int waitTime = Properties.implicitWait;
	private LinkedHashMap<String, String> data;
	public boolean continueOnClickFail = false;
	public boolean elementPrestFail = false;
	// public WebDriver driver;
	public String currentBrowser = Properties.executionBrowserName;

	public By errorWindow = By.xpath("//div[@id='modalContainerDiv'][@class='show']");
	public By systemError = By.xpath("//div[@id='modalContainerDiv'][@class='show']/div/div/div/div/p/a");
	public By alertError = By.xpath("//div[@id='modalContainerDiv'][@class='show']/div/div/div/div/span");
	public By ratesDownError = By.xpath("//div[@id='modalContainerDiv'][@class='show']/div/div/div/div[2]");
	StringWriter sw = new StringWriter();
	private int dataPosition = 0;
	private String currentTestScenarioStep;
	protected HashMap<String, WebDriver> storeDrivers = new LinkedHashMap();
	protected HashMap<String, String> storeVariables = new HashMap();
	protected HashMap<String, Integer> storeResults = new HashMap();
	private WebDriver driver;
	private int iStepNumber = 1;
	private TestResult testResult;
	private String testIdResultFolder;
	private int pass = 0;
	private int fail = 0;
	private String statusPF;
	private ExecutionDetails executionDetails;
	NFT_Engine nft;

	public Session(LinkedHashMap<String, String> data, String currentTestScenarioStep,
			HashMap<String, WebDriver> storeDrivers, Integer dataPosition, TestResult testResult,
			HashMap<String, String> storeVariables, String testIdResultFolder, HashMap<String, Integer> storeResults,
			ExecutionDetails executionDetails, NFT_Engine nft) {
		super();
		this.nft = nft;
		this.data = data;
		this.currentTestScenarioStep = currentTestScenarioStep;
		this.driver = storeDrivers.get("parent");
		try {
			if (currentTestScenarioStep.contains(":")) {
				this.dataPosition = Integer.valueOf(currentTestScenarioStep.split(":")[1]);
			} else {
				this.dataPosition = dataPosition;
			}
		} catch (Exception e) {
			this.dataPosition = dataPosition;
		}
		this.testResult = testResult;

		this.storeVariables.putAll(storeVariables);
		this.storeResults.putAll(storeResults);
		this.testIdResultFolder = testIdResultFolder;
		this.storeDrivers = storeDrivers;
		this.executionDetails = executionDetails;
		if (storeResults.get("Pass") != null) {
			this.pass = storeResults.get("Pass");
		}
		if (storeResults.get("Fail") != null) {
			this.pass = storeResults.get("Fail");
		}

	}

	public String getStatusPF() {
		return statusPF;
	}

	public void setStatusPF(String statusPF) {
		this.statusPF = statusPF;
	}

	protected void validate(String validationID) {
		if (validationID != null && !validationID.isEmpty()) {
			String[] tempXID = new String[1];
			if (validationID.contains("Validation_") || validationID.contains("validation_")) {
				validationID = validationID.substring(validationID.lastIndexOf("_") + 1);
			}
			if (validationID.contains(",")) {
				tempXID = new String[validationID.split(",").length];
				tempXID = validationID.split(",");
			} else {
				tempXID[0] = validationID;
			}
			String st = getTestResult().getStatus();
			LinkedHashMap<String, String> getSetXbridStatus = testResult.getXbridTC_IDStatus();
			if (st.equalsIgnoreCase("Fail")) {

				for (int i = 0; i < tempXID.length; i++) {

					if (getSetXbridStatus.containsKey(tempXID[i].trim())) {
						getSetXbridStatus.put(tempXID[i], "Fail");
						fail++;
						

					} else {

						System.err
								.println("Warning....  Invalid Xbrid ID \n Not able to Map TestCase...  " + tempXID[i]);
					}

				}
				testResult.setXbridTC_IDStatus(getSetXbridStatus);
				statusPF = "Fail";

			} else if (st.equalsIgnoreCase("Fatal")) {

				for (int i = 0; i < tempXID.length; i++) {

					if (getSetXbridStatus.containsKey(tempXID[i])) {
						getSetXbridStatus.put(tempXID[i], "Fail");
						fail++;
						

					} else {

						System.err
								.println("Warning....  Invalid Xbrid ID \n Not able to Map TestCase...  " + tempXID[i]);
					}

				}
				testResult.setXbridTC_IDStatus(getSetXbridStatus);
				statusPF = "Fatal";
				getTestResult().setStatus("Fatal");
			} else {
				for (int i = 0; i < tempXID.length; i++) {

					if (getSetXbridStatus.containsKey(tempXID[i])) {
						getSetXbridStatus.put(tempXID[i], "Pass");
						pass++;
						

					} else {

						System.err
								.println("Warning....  Invalid Xbrid ID \n Not able to Map TestCase...  " + tempXID[i]);
					}

				}

				testResult.setXbridTC_IDStatus(getSetXbridStatus);
				statusPF = "Pass";

			}
			for (Entry<String, String> entry : getSetXbridStatus.entrySet()) {
				
				String value = entry.getValue();
				
				if(value.equalsIgnoreCase("Pending"))
				{
					testResult.setStatus("Running");
					break;
				}
				}
			executionDetails.setPass(pass);
			executionDetails.setFail(fail);
			storeResults.put("Pass", pass);
			storeResults.put("Fail", pass);

		} else {
			System.err.println("Unable to Validate \nXbrid ID null or empty");
		}
	}

	protected void validate() {

		ArrayList<String> Xid = new ArrayList();
		String st = getTestResult().getStatus();
		LinkedHashMap<String, String> getSetXbridStatus = testResult.getXbridTC_IDStatus();
		if (st.equalsIgnoreCase("Fail")) {

			for (Entry<String, String> entry : getSetXbridStatus.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (value.equalsIgnoreCase("Running")) {
					Xid.add(key);
					fail++;
					getSetXbridStatus.put(key, "Fail");
				}
			}
			testResult.setXbridTC_IDStatus(getSetXbridStatus);
			statusPF = "Fail";

		}
		boolean run = false;
		for (int i = 0; i < Xid.size(); i++) {

			if (getSetXbridStatus.get(Xid.get(i)).equalsIgnoreCase("Pending")) {
				getTestResult().setStatus("Running");
				run = true;
				break;
			}

		}
		if (!run) {
			getTestResult().setStatus("Fail");
		}
		executionDetails.setPass(pass);
		executionDetails.setFail(fail);
		storeResults.put("Pass", pass);
		storeResults.put("Fail", pass);
	}

	public boolean getScreenshot() {
		boolean result = false;
		try {
			String screenShotPath = testIdResultFolder + "screenShot_"
					+ new SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date()) + ".png";
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenShotPath));
			nft.logger.log(Status.INFO, "Took Screenshot " + nft.logger.addScreenCaptureFromPath(screenShotPath));

			nft.extent.flush();
			result = true;
		} catch (Exception e) {

		}
		return result;

	}
	public boolean getScreenshotreports() {
		boolean result = false;
		try {
			String screenShotPath = testIdResultFolder + "screenShot_"
					+ new SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date()) + ".png";
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenShotPath));
			nft.logger.log(Status.INFO, "Took Screenshot " + nft.logger.addScreenCaptureFromPath(screenShotPath));

			nft.extent.flush();
			result = true;
		} catch (Exception e) {

		}
		return result;

	}

	private String getScreenshotPath() {
		String screenShotPath = null;
		try {
			screenShotPath = testIdResultFolder + "screenShot_"
					+ new SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date()) + ".png";
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenShotPath));

		} catch (Exception e) {

		}
		return screenShotPath;

	}

	public boolean setVariable(String key, String value) throws Exception {

		boolean setStatus = false;
		try {

			storeVariables.put(key, value);
			status("info", "Global Variable set --> key -" + key + " Value-" + value);
			setStatus = true;
		} catch (Exception e) {
			e.printStackTrace();
			status("warning", "Global Variable Not set --> key -" + key + " Value-" + value);
			setStatus = false;
		}

		return setStatus;

	}

	public String getVariable(String key) throws Exception {

		if (storeVariables.containsKey(key)) {
			String value = storeVariables.get(key);
			status("info", "Global Variable retrived --> key -" + key + " Value-" + value);
			return value;
		} else {
			status("info", "Global Variable not available --> key -" + key);
			return "";
		}
	}

	public boolean setNewDriver(String driverName) throws Exception {

		boolean newDriverStatus = false;
		try {
			WebDriver driver = new DriverFactory().getInstance(Properties.executionBrowserName);
			this.driver = driver;
			storeDrivers.put(driverName, driver);

			newDriverStatus = true;
		} catch (Exception e) {
			e.printStackTrace();
			// status("fatal",driverName+" new driver initialization failed");
			newDriverStatus = false;
		}

		if (newDriverStatus) {
			status("pass", " New driver '" + driverName + "' initilized");
		} else {
			status("fatal", "New driver not initilized");
		}
		return newDriverStatus;

	}

	public WebDriver getDriver() {

		return driver;

	}

	public boolean quitDriver(String driverName) throws Exception {

		if (storeDrivers.get(driverName) != null) {
			try {
				storeDrivers.get(driverName).quit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			status("pass", "Driver Quit successfully");
			return true;
		} else {
			status("fatal", "Driver Quit failed");
			return false;
		}

	}

	public boolean quitDrivers() throws Exception {
		boolean status = true;
		try {
			int i = 1;
			for (WebDriver d : storeDrivers.values()) {
				if (i != 1) {
					d.quit();
				}
				i++;
			}
		} catch (Exception e) {

			status = false;
		}
		return status;
	}

	public WebDriver getDriver(String driverName) {

		return storeDrivers.get(driverName);

	}

	public boolean setDriver(String driverName) throws Exception {

		if (storeDrivers.get(driverName) != null) {
			this.driver = storeDrivers.get(driverName);
			status("pass", "Driver switched successfully");
			return true;
		} else {
			status("fatal", "Driver switch failed");
			return false;
		}

	}

	public boolean setParentDriver() throws Exception {

		if (storeDrivers.get("parent") != null) {
			this.driver = storeDrivers.get("parent");
			status("pass", "Switched to parent driver successfully");
			return true;
		} else {
			status("fatal", "Unable to switch to parent driver / Parent driver not available");
			return false;
		}

	}

	protected TestResult getTestResult() {
		return testResult;

	}

	public WebElement findElement(By elementBy) throws Exception {
		WebElement element = null;
		try {
			String elementText = "N/A";
			try {
				elementText = driver.findElement(elementBy).getText();
			} catch (Exception e) {

			}
			element = driver.findElement(elementBy);
			status("Pass", "'" + elementText + "' element found");
		} catch (Exception e) {
			status("Fail", e.getStackTrace() + " Element not found");
		}

		return element;
	}

	public List<WebElement> findElements(By elementBy) throws Exception {

		return driver.findElements(elementBy);
	}

	public boolean isDisplayed(By elementBy) throws Exception {
		try {
			if (driver.findElement(elementBy).isDisplayed()) {
				return true;

			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isTextPresent(WebElement element, String string) throws Exception {
		if (element.getText() != null || element.getText().equals(string)) {
			return true;
		}

		return false;
	}

	public boolean isDisplayed(WebElement element) throws Exception {
		try {
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception j) {
			return false;
		}

	}

	public boolean isDisplayed(WebElement element, String text) throws Exception {
		try {
			if (element.isDisplayed() || element.getText().equals(text)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception j) {
			return false;
		}

	}

	/**
	 * Method to check if element is present
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(By elementBy) throws Exception {
		boolean status = true;
		try {
			status = driver.findElement(elementBy).isDisplayed();
		} catch (Exception e) {
			status = false;
		}

		return status;
	}

	/**
	 * Method to check if element is present after waiting for specific time
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresentAfterWait(final By elementBy) throws Exception {

		boolean elementPresent = false;

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(120, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy));

			if (driver.findElement(elementBy).isDisplayed()) {

				elementPresent = true;
			}

		} catch (NoSuchElementException nsee) {

			System.out.println("No Such Element present exception occured");
			throw new Exception(nsee);

		} catch (Exception e) {

			System.out.println("Generic Exception occured");
			throw new Exception(e);
		}

		return elementPresent;
	}

	public boolean fluentWait(final By elementBy, int seconds) {

		boolean elementPresent = true;

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy));

			/*
			 * vishnu if (driver.findElement(elementBy).isDisplayed()) {
			 * 
			 * elementPresent = true; }
			 */

		} catch (NoSuchElementException nsee) {
			elementPresent = false;
			System.out.println("No Such Element present exception occured");
			nft.logger.log(Status.INFO, "Step # " + iStepNumber++ + " -> '" + " element not present");

		} catch (Exception e) {
			elementPresent = false;
			e.printStackTrace();

		}

		return elementPresent;
	}

	
	public boolean fluentWait_AllElements( By elementBy,ArrayList<String> elementList,String elementName  ,int seconds) {

		boolean elementPresent = true;

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy));
			wait.until(ExpectedConditions.visibilityOf(driver.findElements(elementBy).get(elementList.indexOf(elementName))));

			/*
			 * vishnu if (driver.findElement(elementBy).isDisplayed()) {
			 * 
			 * elementPresent = true; }
			 */

		} catch (NoSuchElementException nsee) {
			elementPresent = false;
			System.out.println("No Such Element present exception occured");
			nft.logger.log(Status.INFO, "Step # " + iStepNumber++ + " -> '" + " element not present");

		} catch (Exception e) {
			elementPresent = false;
			e.printStackTrace();

		}

		return elementPresent;
	}
	/**
	 * Method to check if element is present without wait
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean noWaitElementPresent(By elementBy) throws Exception {

		return isElementPresent(elementBy, 1);
	}

	/**
	 * Method to check if element is not present without wait
	 * 
	 * @param elementBy
	 * @return
	 */
	public boolean isElementNotPresent(By elementBy) {

		boolean elementNotPresent = true;

		try {

			if (driver.findElement(elementBy).isDisplayed()) {

				return false;
			}

		} catch (NoSuchElementException nsee) {

			System.out.println("Element not found, NoSuchElementException caught");

		} catch (Exception se) {
			se.printStackTrace();
			System.out.println("Element not found, generic exception caught");
		}

		// driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementNotPresent;
	}

	/**
	 * Method to check element is present within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @return
	 */
	public boolean isElementPresent(WebElement element, By elementBy) {

		boolean elementPresent = false;

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

		try {

			elementPresent = element.findElement(elementBy).isDisplayed();

		} catch (Exception ex) {

			System.out.println("Exception occured while finding element within element");
		}

		// driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementPresent;
	}

	public boolean isElementPresent(WebElement element) {

		boolean elementPresent = false;

		// driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

		try {

			elementPresent = element.isDisplayed();

		} catch (Exception ex) {

			System.out.println("Exception occured while finding element within element");
		}

		// driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementPresent;
	}

	/**
	 * Method to click element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void clickElement(By elementBy, String stepDescription, String PageDescription) throws Exception {

		boolean exceptionOccured = false;
		String beforeClickUrl = driver.getCurrentUrl();

		try {
			moveToElement(elementBy);
			if (isElementPresent(elementBy, 2)) {

				try {

					driver.findElement(elementBy).click();

					// Commented and updated by ...
					// nft.logger.log(Status.PASS, "Step # "+iStepNumber+++" ->
					// '"+stepDescription +"' is clicked in the '"+PageDescription+"' page.");
					nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + stepDescription
							+ "' is clicked in the  '" + PageDescription + "' page.");
					/*
					 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
					 */

				} catch (Exception ex) {
					try {
						jsClick(elementBy);
					} catch (Exception e) {
						status(Status.FAIL, e.getLocalizedMessage());
					}

				}

			} else {

				status(Status.FAIL,
						stepDescription + "' is Not Avaliable/clicked in the  '" + PageDescription + "' page.");

			}

			// driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		} catch (Exception e) {

			// handleAlert(driver);
			status(Status.FATAL, e.getLocalizedMessage());

		}
	}

	/**
	 * Method to click element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void clickNoUrlValidation(By elementBy) throws Exception {

		boolean exceptionOccured = false;

		if (isElementPresent(elementBy)) {

			try {

				try {

					driver.findElement(elementBy).click();

					/*
					 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
					 */

				} catch (NoSuchElementException nsee) {

					exceptionOccured = true;

				}

			} catch (Exception ex) {

				exceptionOccured = true;

			}

			if (exceptionOccured) {

				try {

					jsClick(elementBy);

				} catch (Exception ex) {

				}
			}

		}

		// driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to set status
	 * 
	 * @param status
	 *            {@value Pass,Fail,Info,fatal,debug,error,skip,warning}
	 * @param description
	 * 
	 * @throws Exception
	 */
	public void status(String status, String description) throws Exception {
		description = "Step # " + iStepNumber++ + " -> '" + description + " in the '" + driver.getTitle() + "' page.";
		String screenShotPath;
		switch (status.toLowerCase()) {
		case "pass":
			nft.logger.log(Status.PASS, description);
			System.err.println("PASS --> " + description);
			break;
		case "fail":
			getTestResult().setStatus("Fail");
			screenShotPath = getScreenshotPath();
			nft.logger.log(Status.FAIL, description + "\n\t" + nft.logger.addScreenCaptureFromPath(screenShotPath));
			getTestResult().setReportLink("file://" + screenShotPath);
			getTestResult().setFailureReason(description);
			System.err.println("FAIL --> " + description);
			break;
		// throw new Exception("FAIL --> " + description);
		case "fatal":
			getTestResult().setStatus("Fatal");
			screenShotPath = getScreenshotPath();
			nft.logger.log(Status.FATAL, description + "\n\t" + nft.logger.addScreenCaptureFromPath(screenShotPath));
			getTestResult().setReportLink("file://" + screenShotPath);
			getTestResult().setFailureReason(description);
			System.err.println("FATAL --> " + description);
			validate();
			throw new Exception("FATAL --> " + description);

		case "info":
			nft.logger.log(Status.INFO, description);
			System.err.println("INFO --> " + description);
			break;
		case "debug":
			nft.logger.log(Status.DEBUG, description);
			System.err.println("DEBUG --> " + description);
			break;
		case "error":
			nft.logger.log(Status.ERROR, description);
			testResult.setStatus("Fail");
			System.err.println("ERROR --> " + description);
			break;
		case "skip":
			nft.logger.log(Status.SKIP, description);
			System.err.println("SKIP --> " + description);
			break;
		case "warning":
			nft.logger.log(Status.WARNING, description);
			System.err.println("WARNING --> " + description);
			break;
		default:
			System.err.println("Invalid Status");
			throw new IllegalArgumentException();

		}
		nft.extent.flush();
	}

	public void status(Status status, String description) throws Exception {
		description = "Step # " + iStepNumber++ + " -> '" + description + " in the '" + driver.getTitle() + "' page.";
		// test1 = test.createNode(testName, desc);
		String screenShotPath;
		switch (status) {
		case PASS:
			nft.logger.log(Status.PASS, description);
			System.err.println("PASS --> " + description);
			break;
		case FAIL:
			getTestResult().setStatus("Fail");
			screenShotPath = getScreenshotPath();
			nft.logger.log(Status.FAIL, description + "\n\t" + nft.logger.addScreenCaptureFromPath(screenShotPath));
			getTestResult().setReportLink("file://" + screenShotPath);
			getTestResult().setFailureReason(description);
			System.err.println("FAIL --> " + description);
			break;
		// throw new Exception("FAIL --> " + description);
		case FATAL:
			getTestResult().setStatus("Fatal");
			screenShotPath = getScreenshotPath();
			nft.logger.log(Status.FATAL, description + "\n\t" + nft.logger.addScreenCaptureFromPath(screenShotPath));
			getTestResult().setReportLink("file://" + screenShotPath);
			getTestResult().setFailureReason(description);
			System.err.println("FATAL --> " + description);
			throw new Exception("FATAL --> " + description);

		case INFO:
			nft.logger.log(Status.INFO, description);
			System.err.println("INFO --> " + description);
			break;
		case DEBUG:
			nft.logger.log(Status.DEBUG, description);
			System.err.println("DEBUG --> " + description);
			break;
		case ERROR:
			nft.logger.log(Status.ERROR, description);
			testResult.setStatus("Fail");
			System.err.println("ERROR --> " + description);
			break;
		case SKIP:
			nft.logger.log(Status.SKIP, description);
			System.err.println("SKIP --> " + description);
			break;
		case WARNING:
			nft.logger.log(Status.WARNING, description);
			System.err.println("WARNING --> " + description);
			break;
		default:
			System.err.println("Invalid Status");
			throw new IllegalArgumentException();

		}
		nft.extent.flush();
	}

	/**
	 * Method to click element
	 * 
	 * @param element
	 * @throws Exception
	 */
	public void clickElement(By elementBy) throws Exception {

		boolean exceptionOccured = false;

		if (isElementPresent(elementBy, 5)) {

			try {
				String text = "N/A";
				try {
					moveToElement(elementBy);
					try {
						text = driver.findElement(elementBy).getText();
					} catch (Exception e) {
						text = "N/A";
					}
					driver.findElement(elementBy).click();

					nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + text + "' is clicked in the  '"
							+ driver.getTitle() + "' page.");
					/*
					 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
					 */

				} catch (NoSuchElementException nsee) {

					exceptionOccured = true;

				}

			} catch (Exception ex) {

				exceptionOccured = true;
				// failureErrorHandling(ex.getMessage());

			}

			if (exceptionOccured) {

				try {

					jsClick(elementBy);

				} catch (Exception ex) {
					failureErrorHandling(ex.getMessage());
				}
			}

		}else {
			status("fail", elementBy+" - Xpath not available");
		}

		// driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to click using Actions
	 * 
	 * @param elementBy
	 */
	public void clickUsingActions(By elementBy) {

		try {

			if (isElementPresent(elementBy)) {

				Actions actions = new Actions(driver);
				actions.click(driver.findElement(elementBy));
			}

		} catch (Exception ex) {

			System.out.println("Exceptin occured whil clicking using Actions");
		}
	}

	/**
	 * Method to double click using actions
	 * 
	 * @param elementBy
	 */
	public void doubleClickUsingAction(By elementBy) {

		try {

			if (isElementPresent(elementBy)) {

				Actions actions = new Actions(driver);
				actions.doubleClick(driver.findElement(elementBy));
			}

		} catch (Exception ex) {

			System.out.println("Exceptin occured whil clicking using Actions");
		}
	}

	/**
	 * Method to check if alert box is present
	 * 
	 * @return
	 */
	public boolean isAlertPresent(WebDriver driver) {

		try {

			driver.switchTo().alert();

			return true;

		} catch (NoAlertPresentException Ex) {

			// For FF throwing exception
			return false;
		}
	}

	/**
	 * Method to click element within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @throws Exception
	 */
	public void clickElement(WebElement element, By elementBy) throws Exception {
		String text = "N/A";

		if (isElementPresent(element, elementBy)) {
			try {
				element.findElement(elementBy).click();

				try {
					text = element.findElement(elementBy).getText();
				} catch (Exception e) {
					text = "N/A";
				}

				nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + text + "' is clicked in the  '"
						+ driver.getTitle() + "' page.");
			} catch (Exception e) {
				// TODO: handle exception
				status(Status.FAIL, e.getLocalizedMessage());
			}
		} else {
			status(Status.FAIL, "Element not available to click");
		}

	}

	public void clickElement(WebElement element) throws Exception {
		String text = "N/A";
		if (isElementPresent(element)) {
			try {
				element.click();
				try {
					text = element.getText();
				} catch (Exception e) {
					text = "N/A";
				}

				status(Status.PASS, "Step # " + iStepNumber++ + " -> '" + text + "' is clicked in the  '"
						+ driver.getTitle() + "' page.");
			} catch (Exception e) {
				jsClick(element);
			}
		} else {
			status(Status.FAIL, "Element is not available");
		}
	}

	/**
	 * Method to check if element is enabled
	 * 
	 * @param elementBy
	 * @throws Exception
	 */

	public boolean isEnabled(By elementBy) throws Exception {
		try {

			return driver.findElement(elementBy).isEnabled();
		} catch (Exception e) {
			throw new Exception(e);

		}

	}

	/**
	 * Method to mouse over element using javascript
	 * 
	 * @param element
	 */
	public void mouseOver(WebElement element) {

		String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript(code, element);
	}

	/**
	 * Method to mouse over element using javascript
	 * 
	 * @param elementBy
	 * @throws InterruptedException
	 */
	public void mouseOver(By elementBy) throws Exception {

		WebElement weElement = driver.findElement(elementBy);

		String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";

		if (isElementPresent(elementBy, 5)) {

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			((RemoteWebDriver) executor).executeScript(code, weElement);
		}
	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param elementBy
	 * @throws InterruptedException
	 */
	public void jsClick(By elementBy) throws Exception {

		WebElement weElement = driver.findElement(elementBy);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", weElement);

	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param weElement
	 * @throws InterruptedException
	 */
	public void jsClick(WebElement weElement) throws Exception {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			((RemoteWebDriver) executor).executeScript("arguments[0].click();", weElement);
			nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + "Element clicked " + " in page "
					+ driver.getTitle() + "' page.");
		} catch (Exception e) {
			status(Status.FAIL, e.getLocalizedMessage());
		}
	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param documentId
	 */
	public void jsClick(String documentId) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("document.getElementById('hlViewStoreFinder').click();");

	}

	/**
	 * Method to focus element
	 * 
	 * @param elementBy
	 */
	public void focusElement(By elementBy) {

		try {

			jsFocus(driver.findElement(elementBy));

		} catch (Exception ex) {

			System.out.println("focusElement" + ex.toString()
					+ " Exception occured while clicking element using driver focus method");

		}
	}

	/**
	 * Method to focus element using javascript
	 * 
	 * @param element
	 */
	private void jsFocus(WebElement element) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", element);
	}

	/**
	 * Method to clear text element
	 * 
	 * @param elementBy
	 */
	public void clearElement(By elementBy) {

		driver.findElement(elementBy).sendKeys(Keys.HOME);
		driver.findElement(elementBy).sendKeys("");
		driver.findElement(elementBy).clear();
	}

	/**
	 * Method to navigate back
	 */
	public void navigateBack() {

		driver.navigate().back();
	}

	/**
	 * Method to navigate to url
	 */
	public void navigateTo(String url) {

		driver.navigate().to(url);
	}

	/**
	 * Method to Get to url
	 */
	public void getURL(String url) {

		driver.get(url);
	}

	/**
	 * Method to select value for dropdown list
	 * 
	 * @param elementBy
	 * @param label
	 * @throws Exception
	 */
	// updated by vishnu
	public void selectValueByVisible(By elementBy, String label, String sDropDownName) throws Exception {
		try {
			String PageDescription = driver.getTitle();
			if (isElementPresent(elementBy, 1)) {

				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' is
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				new Select(driver.findElement(elementBy)).selectByVisibleText(label);

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + label + "' selected from the dropdwon list for : '"
								+ sDropDownName + "' in the '" + PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");

				status(Status.FAIL, label + "' Not selected from the dropdwon list for : '" + sDropDownName);
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}

	public String getSelectedOption(By elementBy) throws Exception {
		String option = null;
		try {

			if (isElementPresent(elementBy, 1)) {

				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' is
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				option = new Select(driver.findElement(elementBy)).getFirstSelectedOption().getAttribute("value");

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);

			} else {
				status("fail", "Unable to get the selected option");
			}

		}

		catch (Exception e) {

			status("fail", "Unable to get the selected option");
		}
		return option;

	}

	@Deprecated
	public void selectValueByVisible(By elementBy, String labelColumnName) throws Exception {
		try {

			String PageDescription = driver.getTitle();
			if (isElementPresent(elementBy, 1)) {

				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' is
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				new Select(driver.findElement(elementBy)).selectByVisibleText(getTestData(labelColumnName));

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + getTestData(labelColumnName)
								+ "' selected from the dropdwon list for : '" + labelColumnName + "' in the '"
								+ PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, " -> '" + getTestData(labelColumnName)
						+ "' Not selected from the dropdwon list for : '" + labelColumnName);

			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}

	public void selectValueByVisibleText(By elementBy, String labelColumnName) throws Exception {
		try {

			String PageDescription = driver.getTitle();
			if (isElementPresent(elementBy, 1)) {

				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' is
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				new Select(driver.findElement(elementBy)).selectByVisibleText(getTestData(labelColumnName));

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + getTestData(labelColumnName)
								+ "' selected from the dropdwon list for : '" + labelColumnName + "' in the '"
								+ PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, getTestData(labelColumnName) + "' Not selected from the dropdwon list for : '"
						+ labelColumnName);
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}

	public void selectValueByVisibleText(By elementBy, String label, String description) throws Exception {
		try {

			String PageDescription = driver.getTitle();
			if (isElementPresent(elementBy, 1)) {

				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' is
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				new Select(driver.findElement(elementBy)).selectByVisibleText(label);

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + label + "' selected from the dropdwon list for : '"
								+ description + "' in the '" + PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, label + "' Not selected from the dropdwon list for : '" + description);
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}
	
	public void selectValueByVisibleText(WebElement element, String label, String description) throws Exception {
		try {

			String PageDescription = driver.getTitle();
			if (isElementPresent(element, 1)) {

				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' is
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				new Select(element).selectByVisibleText(label);

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + label + "' selected from the dropdwon list for : '"
								+ description + "' in the '" + PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, label + "' Not selected from the dropdwon list for : '" + description);
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}

	//

	/**
	 * Method to select value for dropdown list
	 * 
	 * @param elementBy
	 * @param label
	 * @throws Exception
	 */
	// updated and commented by ...
	@Deprecated
	public void selectByValue(By elementBy, String label, String sDropDownName, String PageDescription)
			throws Exception {
		try {
			if (isElementPresent(elementBy, 1)) {

				new Select(driver.findElement(elementBy)).selectByValue(label);

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + label + "' selected from the dropdwon list for : '"
								+ sDropDownName + "' in the '" + PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, label + "' Not selected from the dropdwon list for : '" + sDropDownName);
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}

	public void selectByValue(By elementBy, String label, String sDropDownName) throws Exception {
		try {
			String PageDescription = driver.getTitle();
			if (isElementPresent(elementBy, 1)) {

				new Select(driver.findElement(elementBy)).selectByValue(label);

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + label + "' selected from the dropdwon list for : '"
								+ sDropDownName + "' in the '" + PageDescription + "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, label + "' Not selected from the dropdwon list for : '" + sDropDownName);
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}


	public void selectByValue(By elementBy, String labelColumnName) throws Exception {
		try {
			String PageDescription = driver.getTitle();
			if (isElementPresent(elementBy, 1)) {

				new Select(driver.findElement(elementBy)).selectByValue(getTestData(labelColumnName));

				// nft.logger.log(Status.PASS, sPassingMessage);
				// nft.logger.log(Status.PASS, sPassingMessage);
				nft.logger.log(Status.PASS,
						"Step # " + iStepNumber++ + " -> '" + labelColumnName
								+ "' selected from the dropdwon list for : '" + "N\\A" + "' in the '" + PageDescription
								+ "' page.");
			} else {
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> ' The
				// expected field is not available or locator is incorrect : '"+EditBox+"' in
				// the '"+PageDescription+"' page.");
				// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
				// selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.";
				// nft.logger.log(Status.FAIL, "Step # "+iStepNumber+++" -> '"+label +
				// "'
				// Not selected from the dropdwon list for : '" +sDropDownName+"' in the
				// '"+PageDescription+"' page.");
				status(Status.FAIL, labelColumnName + " Not selected from the dropdwon list");
			}

		}

		catch (Exception e) {

			// String sPassingMessage = "Step # "+iStepNumber+++" -> '"+label + "' Not
			// selected from the dropdwon list for : '" +sDropDownName+"' in the
			// '"+PageDescription+"' page.";
			status(Status.FAIL, e.getLocalizedMessage());
			// nft.logger.log(Status.FAIL, e.getMessage());
			// Commented by ...
			// failureErrorHandling(driver, e.getMessage());
		}

	}

	//
	/**
	 * Method to send keys to text element
	 * 
	 * @param elementBy
	 * @param typeValueColumnName
	 * @throws InterruptedException
	 */
	public void sendKeys(By elementBy, String typeValueColumnName) throws Exception {

		try {
			String PageDescription = driver.getTitle();
			String EditBox = typeValueColumnName;
			if (isElementPresent(elementBy, 1)) {
				driver.findElement(elementBy).clear();
				while(!getAttribute(elementBy, "value").isEmpty()) {
					sendKeys(elementBy, Keys.BACK_SPACE);
					sendKeys(elementBy, Keys.DELETE);
				}

				driver.findElement(elementBy).sendKeys(getTestData(typeValueColumnName));
				nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + typeValueColumnName
						+ "' is entered for the field : '" + EditBox + "' in the '" + PageDescription + "' page.");
				// nft.logger.log(Status.PASS, "Step # "+iStepNumber+++" ->
				// '"+typeValue +"' is entered for the field : '"+EditBox+"' in the
				// '"+PageDescription+"' page.");
			}
			// below lines updated by ...
			else {

				status(Status.FAIL, "The expected field is not available or locator is incorrect : '" + EditBox);
			}
			//

		} catch (Exception e) {

			status(Status.FAIL, e.getLocalizedMessage());
		}
	}

	
	
	
	public void sendKeyswithoutbackspace(By elementBy, String typeValueColumnName) throws Exception {

		try {
			String PageDescription = driver.getTitle();
			String EditBox = typeValueColumnName;
			if (isElementPresent(elementBy, 1)) {
				driver.findElement(elementBy).sendKeys(getTestData(typeValueColumnName));
				nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + typeValueColumnName
						+ "' is entered for the field : '" + EditBox + "' in the '" + PageDescription + "' page.");
				// nft.logger.log(Status.PASS, "Step # "+iStepNumber+++" ->
				// '"+typeValue +"' is entered for the field : '"+EditBox+"' in the
				// '"+PageDescription+"' page.");
			}
			// below lines updated by ...
			else {

				status(Status.FAIL, "The expected field is not available or locator is incorrect : '" + EditBox);
			}
			//

		} catch (Exception e) {

			status(Status.FAIL, e.getLocalizedMessage());
		}
	}

	public void sendKeys(By elementBy, String typeValue, String EditBoxDescription) throws Exception {

		try {

			if (isElementPresent(elementBy, 1)) {

				driver.findElement(elementBy).clear();
				while(!getAttribute(elementBy, "value").isEmpty()) {
					sendKeys(elementBy, Keys.BACK_SPACE);
					sendKeys(elementBy, Keys.DELETE);
				}
				driver.findElement(elementBy).sendKeys(typeValue);
				nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + typeValue
						+ "' is entered for the field : '" + EditBoxDescription);
				// nft.logger.log(Status.PASS, "Step # "+iStepNumber+++" ->
				// '"+typeValue +"' is entered for the field : '"+EditBox+"' in the
				// '"+PageDescription+"' page.");
			}
			// below lines updated by ...
			else {

				status(Status.FAIL,
						" The expected field is not available or locator is incorrect : '" + EditBoxDescription);
			}
			//

		} catch (Exception e) {

			status(Status.FAIL, e.getLocalizedMessage());
		}
	}

	/**
	 * Method to send characters to text element
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public void sendChars(By elementBy, String typeChar) throws Exception {

		if (isElementPresent(elementBy)) {

			driver.findElement(elementBy).sendKeys(typeChar);
		}
	}

	/**
	 * Method to get text of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public String getText(By elementBy) throws Exception {

		if (noWaitElementPresent(elementBy)) {

			return driver.findElement(elementBy).getText().trim();

		} else {

			return "";
		}
	}

	/**
	 * Method to get Double value of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public Double getDouble(By elementBy) throws Exception {

		Double retValue = 0.0;

		try {

			retValue = Double.valueOf(getText(elementBy));

		} catch (Exception ex) {

		}

		return retValue;
	}

	/**
	 * Method to get Double value of an element
	 * 
	 * @param str
	 * @return
	 * @throws InterruptedException
	 */
	public Double getDouble(String str) throws Exception {

		Double retValue = 0.0;

		try {

			retValue = Double.valueOf(str);

		} catch (Exception ex) {

		}

		return retValue;
	}

	/**
	 * Method to get Double value of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public int getInteger(By elementBy) throws Exception {

		int retValue = 0;

		try {

			retValue = Integer.valueOf(getText(elementBy));

		} catch (Exception ex) {

		}

		return retValue;
	}

	/**
	 * Method to get text of an element within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @return
	 */
	public String getText(WebElement element, By elementBy) {

		if (isElementPresent(element, elementBy)) {

			return element.findElement(elementBy).getText().trim();

		} else {

			return "";
		}
	}

	/**
	 * Method to get text of an element within parent element
	 * 
	 * @param element
	 * @return
	 * @throws InterruptedException
	 */
	public String getText(WebElement element) throws Exception {

		if (isElementPresent(element, 5)) {

			return element.getText().trim();

		} else {

			return "";

		}
	}

	/**
	 * Method to handle alert box
	 */
	public void handleAlert(WebDriver driver) {

		try {

			Alert alert = driver.switchTo().alert();
			alert.accept();

		} catch (Exception ex) {

			System.out.println("Execption occured during alert handle");

		}
	}

	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void waitForElementPresent(final By elementBy) throws Exception {

		isElementPresent(elementBy, 15);

	}

	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void waitForElementPresent(final WebElement elementBy) throws Exception {

		isElementPresent(elementBy, 5);
	}

	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @param waitTimeSecs
	 * @throws InterruptedException
	 */
	public boolean waitForElementPresent(final By elementBy, int waitTimeSecs) throws Exception {

		return isElementPresent(elementBy, waitTimeSecs);
	}

	/**
	 * Method to check if element is Present within some seconds
	 * 
	 * @param elementBy
	 * @param waitForSeconds
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isElementPresent(By elementBy, int waitForSeconds) throws Exception {

		boolean elementPresent = false;
		int count = 0;

		fluentWait(elementBy, waitForSeconds);
		try {

			if (driver.findElement(elementBy).isDisplayed()) {
				elementPresent = true;
			}

		} catch (Exception e) {
			elementPresent = false;
		}
		return elementPresent;
	}

	/**
	 * Method to check if element is Present with in an element for some seconds
	 * 
	 * @param element
	 * @param elementBy
	 * @param waitForSeconds
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(WebElement element, By elementBy, int waitForSeconds) throws Exception {

		boolean elementPresent = true;
		int count = 0;

		while (elementPresent) {

			try {

				if (element.findElement(elementBy).isDisplayed()) {

					break;

				} else {

					if (count == waitForSeconds) {

						elementPresent = false;
						break;

					}

					Thread.sleep(1000);
					count++;

				}

			} catch (Exception ex) {

				if (count == waitForSeconds) {

					elementPresent = false;
					break;

				}

				Thread.sleep(1000);
				count++;
			}
		}

		return elementPresent;
	}

	/**
	 * Method to check web element is present
	 * 
	 * @param element
	 * @param waitForSeconds
	 * @return
	 * @throws Exception
	 */
	public boolean isElementPresent(WebElement element, int waitForSeconds) throws Exception {

		boolean elementPresent = true;
		int count = 0;

		while (elementPresent) {

			try {

				if (element.isDisplayed()) {

					break;

				} else {

					if (count == waitForSeconds) {

						elementPresent = false;
						break;
					}

					Thread.sleep(1000);
					count++;
				}

			} catch (Exception ex) {

				if (count == waitForSeconds) {

					elementPresent = false;
					break;
				}

				Thread.sleep(1000);
				count++;
			}
		}

		return elementPresent;
	}

	/**
	 * Method to wait for page to get loaded
	 */
	public void waitForPageLoaded() {

		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver1) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};

		WebDriverWait wait = new WebDriverWait(driver, 30);

		try {

			wait.until(expectation);

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	public void waitForPageTitle(String title, int seconds) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.titleIs(title));
		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	/**
	 * Component to move actual mouse pointer to top left corner of page.
	 * 
	 * @throws AWTException
	 */
	public void moveActualMouse() throws AWTException {

		Robot robot = new Robot();
		robot.mouseMove(0, 0);
		robot = null;
	}

	/**
	 * Component to move actual mouse pointer to top left corner of page
	 * 
	 * @param x
	 * @param y
	 * @throws AWTException
	 */
	public void moveActualMouse(int x, int y) throws AWTException {

		Robot robot = new Robot();
		robot.mouseMove(x, y);
		robot = null;
	}

	/**
	 * Component to click on close button of overlay and wait to until the overlay
	 * closes itself.
	 * 
	 * @param elementToCheck
	 * @param closeButton
	 * @throws Exception
	 */
	public void clickAndWaitUntilDisappear(By elementToCheck, By closeButton) throws Exception {

		if (isElementPresent(elementToCheck, 0)) {

			// clickElement(driver,closeButton);

			while (true) {

				if (isElementPresent(elementToCheck, 1)) {

				} else {

					break;
				}
			}
		}
	}

	/**
	 * Component to wait until the overlay closes itself
	 * 
	 * @param elementToCheck
	 * @throws Exception
	 */
	public void waitUntilDisappear(By elementToCheck) throws Exception {

		int i = 0;

		while (true) {

			if (isElementPresent(elementToCheck, 1)) {

			} else {

				break;
			}

			if (i == 10) {

				break;
			}

			i++;
		}
	}

	/**
	 * Component to paste text to a control
	 * 
	 * @param pasteBy
	 * @throws Exception
	 */
	public void pasteText(By pasteBy) throws Exception {

		if (isElementPresent(pasteBy)) {

			clearElement(pasteBy);
			driver.findElement(pasteBy).sendKeys(Keys.CONTROL + "v");
		}
	}

	/**
	 * Component to get attribute value for a particular control
	 * 
	 * @param elementBy
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(By elementBy, String attribute) throws Exception {

		String attributeValue = "";

		if (isElementPresent(elementBy)) {

			attributeValue = driver.findElement(elementBy).getAttribute(attribute);
		}

		return attributeValue;
	}

	/**
	 * Component to get attribute value for a particular control
	 * 
	 * @param element
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(WebElement element, String attribute) throws Exception {

		String attributeValue = "";

		if (isElementPresent(element, 2)) {

			attributeValue = element.getAttribute(attribute);
		}

		return attributeValue;
	}

	/**
	 * Component to get attribute value for a particular control
	 * 
	 * @param element
	 * @param elementBy
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public String getAttribute(WebElement element, By elementBy, String attribute) throws Exception {

		String attributeValue = "";

		try {
			attributeValue = element.findElement(elementBy).getAttribute(attribute);

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
		}

		return attributeValue;
	}

	/**
	 * Component to get CSS value for a particular control
	 * 
	 * @param elementBy
	 * @param cssAttribute
	 * @return
	 * @throws Exception
	 */
	public String getCssValue(By elementBy, String cssAttribute) throws Exception {

		String cssValue = "";

		if (isElementPresent(elementBy)) {

			cssValue = driver.findElement(elementBy).getCssValue(cssAttribute);
		}

		return cssValue;
	}

	/**
	 * Component to get CSS value for a particular control
	 * 
	 * @param element
	 * @param elementBy
	 * @param cssAttribute
	 * @return
	 * @throws Exception
	 */
	public String getCssValue(WebElement element, By elementBy, String cssAttribute) throws Exception {

		String cssValue = "";

		if (isElementPresent(element, elementBy)) {

			cssValue = element.findElement(elementBy).getCssValue(cssAttribute);
		}

		return cssValue;
	}

	/**
	 * Method to getLocation for a particular element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public Point getLocation(By elementBy) throws Exception {

		Point locations = new Point(0, 0);

		if (isElementPresent(elementBy, 5)) {

			locations = driver.findElement(elementBy).getLocation();
		}

		return locations;
	}

	/**
	 * Method to refresh web page
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception {

		driver.navigate().refresh();
	}

	/**
	 * Method to get no of webElements count
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getElementsCount(By elementBy) throws Exception {

		int noOfElements = 0;

		if (noWaitElementPresent(elementBy)) {

			noOfElements = driver.findElements(elementBy).size();
		}

		return noOfElements;
	}

	/**
	 * Method to get webElements
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> getElements(By elementBy) throws Exception {

		List<WebElement> lstWebElements = new ArrayList<WebElement>();

		if (isElementPresent(elementBy, 2)) {

			lstWebElements = driver.findElements(elementBy);
		}

		return lstWebElements;
	}

	/**
	 * Method to get webElements
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> getElements(WebElement webElement, By elementBy) throws Exception {

		List<WebElement> lstWebElements = new ArrayList<WebElement>();

		if (isElementPresent(webElement, elementBy, 2)) {

			lstWebElements = webElement.findElements(elementBy);
		}

		return lstWebElements;
	}

	/**
	 * Method to get element count
	 * 
	 * @param webElement
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getElementsCount(WebElement webElement, By elementBy) throws Exception {

		int noOfElements = 0;

		if (isElementPresent(webElement, elementBy, 0)) {

			noOfElements = webElement.findElements(elementBy).size();
		}

		return noOfElements;
	}

	/**
	 * Method to open new tab
	 * 
	 * @param url
	 */
	public void openTab(String url) {

		String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
		script = String.format(script, url);

		Object element = ((JavascriptExecutor) driver).executeScript(script);

		if (element instanceof WebElement) {

			WebElement anchor = (WebElement) element;
			anchor.click();
			((JavascriptExecutor) driver).executeScript("var a=arguments[0];a.parentNode.removeChild(a);", anchor);

			Set<String> handles = driver.getWindowHandles();
			String current = driver.getWindowHandle();
			handles.remove(current);

			String newTab = handles.iterator().next();

			driver.switchTo().window(newTab);

		} else {

		}
	}

	/**
	 * Method to close new tab
	 * 
	 * @param url
	 * @throws InterruptedException
	 */
	public void closeTab() throws InterruptedException {

		String originalHandle = driver.getWindowHandle();

		// Do something to open new tabs

		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(originalHandle)) {
				driver.switchTo().window(handle);
				driver.close();
				Thread.sleep(5000);
			}
		}

		driver.switchTo().window(originalHandle);
	}
	public void switchTab() throws InterruptedException {

		String originalHandle = driver.getWindowHandle();

		// Do something to open new tabs

		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(originalHandle)) {
				driver.switchTo().window(handle);
				
				Thread.sleep(5000);
			}
		}

		driver.switchTo().window(originalHandle);
	}

	/**
	 * Method to trigger javascript on web element
	 * 
	 * @param script
	 * @param element
	 */
	public void trigger(String script, WebElement element) {

		((JavascriptExecutor) driver).executeScript(script, element);
	}

	/**
	 * Executes a script
	 * 
	 * @note Really should only be used when the web driver is sucking at exposing
	 *       functionality natively
	 * @param script
	 *            The script to execute
	 */
	public Object trigger(String script) {

		return ((JavascriptExecutor) driver).executeScript(script);
	}

	/**
	 * Method to get width in pixels of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getWidth(By elementBy) throws Exception {

		int width = 0;

		if (noWaitElementPresent(elementBy)) {

			width = driver.findElement(elementBy).getSize().getWidth();
		}

		return width;
	}

	/**
	 * Method to get height in pixels of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public int getHeight(By elementBy) throws Exception {

		int height = 0;

		if (noWaitElementPresent(elementBy)) {

			height = driver.findElement(elementBy).getSize().getHeight();
		}

		return height;
	}

	/**
	 * Method to clear browser history
	 * 
	 * @throws Exception
	 */
	public void clearHistory(WebDriver driver) throws Exception {

		driver.manage().deleteAllCookies();
		refresh();
	}

	/**
	 * Method to set display style of element to block
	 * 
	 * @param elementBy
	 */
	public void blockElement(By elementBy) {

		WebElement weElement = driver.findElement(elementBy);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].style.display = 'block';", weElement);
	}

	/**
	 * Method to check if element is selected
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public boolean isSelected(By elementBy) throws Exception {

		return driver.findElement(elementBy).isSelected();
	}

	/**
	 * This method checks the ascending order of list.
	 * 
	 */
	public boolean isAscending(List<Integer> sortOrder) {

		boolean ascending = true;

		for (int i = 1; i < sortOrder.size() && (ascending); i++) {
			ascending = ascending && sortOrder.get(i) >= sortOrder.get(i - 1);
		}

		return ascending;
	}

	/**
	 * This method checks the descending order of list.
	 * 
	 */
	public boolean isDescending(List<Integer> sortOrder) {

		boolean descending = true;

		for (int i = 1; i < sortOrder.size() && (descending); i++) {
			descending = descending && sortOrder.get(i) <= sortOrder.get(i - 1);
		}

		return descending;
	}

	/**
	 * Converts the accented characters to deaccented Ex: Décor to Decor
	 * 
	 */
	public String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	/**
	 * Function that returns a string within quotes. Intended to be used in reports.
	 * 
	 */
	public String inQuotes(String str) {

		if (str.contains("$")) {
			str = str.replaceAll("\\$", "");
		}
		if (str != null)
			return "\"" + str + "\"";
		else
			return "";
	}

	/**
	 * Method to move to element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public void moveToElement(By elementBy) throws Exception {
		try {
			Actions act = new Actions(driver);
			WebElement mainMenu = driver.findElement(elementBy);
			act.moveToElement(mainMenu).build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	
	public void escape() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.sendKeys(Keys.ESCAPE).build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	
	public void pagedown() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.sendKeys(Keys.PAGE_DOWN).build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	
	public void print() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.sendKeys(Keys.CONTROL +"p").build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	public void arrowup() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.sendKeys(Keys.ARROW_UP).build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	public void arrowdown() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.sendKeys(Keys.ARROW_DOWN).build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	public void rightclick() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.contextClick().build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}
	public void enterkeyboard() throws Exception {
		try {
			Actions act = new Actions(driver);
			
			act.sendKeys(Keys.ENTER).build().perform();
		} catch (Exception e) {
			status("fail", e.getLocalizedMessage());
		}
	}

	public void moveToElement(WebElement element) {

		Actions act = new Actions(driver);
		WebElement mainMenu = element;
		act.moveToElement(mainMenu).build().perform();
	}

	/**
	 * Method to send keys to text element without clearing
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public void sendKeysWithoutClearing(By elementBy, String excelColumnName) throws Exception {
		try {
			if (isElementPresent(elementBy)) {

				driver.findElement(elementBy).sendKeys(getTestData(excelColumnName));
				nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + getTestData(excelColumnName)
						+ " is entered in " + excelColumnName);
			} else {

				throw new Exception();
			}
		} catch (Exception e) {
			// TODO: handle exception
			status(Status.FAIL, e.getLocalizedMessage());
			throw new Exception(e);

		}
	}

	public void sendKeysWithoutClearing(By elementBy, String typeValue, String description) throws Exception {
		try {
			if (isElementPresent(elementBy)) {

				driver.findElement(elementBy).sendKeys(typeValue);
				nft.logger.log(Status.PASS, "Step # " + iStepNumber++ + " -> '" + description);
			} else {

				throw new Exception();
			}
		} catch (Exception e) {
			// TODO: handle exception

			status(Status.FAIL, e.getLocalizedMessage());
			throw new Exception(e);

		}
	}

	/**
	 * Method to getLocation for a particular element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public Point getLocation(WebElement element, By elementBy) throws Exception {

		Point location = new Point(0, 0);

		if (isElementPresent(element, elementBy, 5)) {

			location = element.findElement(elementBy).getLocation();
		}

		return location;
	}

	/**
	 * Method to get text of an element within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public String getText(By parentElement, By elementBy) throws Exception {

		String text = "";

		if (isElementPresent(parentElement, 2)) {

			WebElement element = driver.findElement(parentElement);

			if (isElementPresent(element, elementBy)) {

				return element.findElement(elementBy).getText().trim();
			}
		}

		return text;
	}

	/**
	 * Method to click element within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @throws Exception
	 */
	public void clickElement(By parentElementBy, By elementBy) throws Exception {

		if (isElementPresent(parentElementBy, 2)) {

			WebElement parentElement = driver.findElement(parentElementBy);

			if (isElementPresent(parentElement, elementBy)) {

				parentElement.findElement(elementBy).click();
			}
		}
	}

	/**
	 * Method to remove the decimal '.' and trailing zero
	 * 
	 * @param String
	 * @param
	 * @throws Exception
	 * @return the trimmed string to the calling function
	 */
	public String removeDecimalvalue(String str) throws Exception {
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		int length, index;
		length = str.length();
		index = length - 1;
		for (; index >= 0; index--)
			if (chars[index] != '0' && chars[index] != '.')
				break;
		return (index == length - 1) ? str : str.substring(0, index + 1);
	}

	/**
	 * Method to send keys to text element
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public void sendKeys(By elementBy, Keys typeValue) throws Exception {

		try {

			if (isElementPresent(elementBy, 1)) {

				driver.findElement(elementBy).sendKeys(typeValue);
			}

		} catch (Exception e) {

			status(Status.FAIL, e.getLocalizedMessage());
		}
	}

	/**
	 * Method to get Selected Option text of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public String getSelectedOptionText(By elementBy) throws Exception {

		if (noWaitElementPresent(elementBy)) {
			String Text = new Select(driver.findElement(elementBy)).getFirstSelectedOption().getText().trim();
			return Text;
		} else {

			return "";
		}
	}

	public boolean isElementVisible(By elementBy) throws Exception {

		boolean elementPresent = false;

		try {
			if (driver.findElement(elementBy).isDisplayed()) {
				elementPresent = true;

			}
		} catch (Exception ex) {

			elementPresent = false;

		}

		return elementPresent;
	}

	public boolean validateAllLinks() throws Exception {
		boolean allLinksVerified = false;
		String homePage = driver.getCurrentUrl();
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		List<WebElement> links = driver.findElements(By.tagName("a"));

		Iterator<WebElement> it = links.iterator();
		int link_count = 0;
		while (it.hasNext()) {

			url = it.next().getAttribute("href");

			// status("info", "Validating Link " + (++link_count) + " -->" + url + " in
			// parent URL -->" + homePage);

			if (url == null || url.isEmpty()) {
				// System.out.println("URL is either not configured for anchor tag or it is
				// empty");
				status("skip", "Link is either not configured for anchor tag or it is empty -->" + url
						+ " in parent URL -->" + homePage);
				continue;
			}

			if (!url.startsWith(homePage)) {
				// System.out.println("URL belongs to another domain, skipping it.");
				status("skip",
						"Link belongs to another domain, skipping it. -->" + url + " in parent URL -->" + homePage);
				continue;
			}

			try {
				huc = (HttpURLConnection) (new URL(url).openConnection());

				huc.setRequestMethod("HEAD");

				huc.connect();

				respCode = huc.getResponseCode();

				if (respCode >= 400) {
					status("fail", "Invalid link  -->" + url + " in parent URL -->" + homePage);
					// System.out.println(url + " is a broken link");
				} else {
					status("pass", "Valid link  -->" + url + " in parent URL -->" + homePage);
					// System.out.println(url + " is a valid link");
				}
				allLinksVerified = true;
			} catch (MalformedURLException e) {
				status("fail", "Exception Occured  -->" + url + " in parent URL -->" + homePage + " Exception -->" + e);
				// TODO Auto-generated catch block
				allLinksVerified = false;
				e.printStackTrace();
			} catch (Exception e) {
				status("fail", "Exception Occured  -->" + url + " in parent URL -->" + homePage + " Exception -->" + e);
				// TODO Auto-generated catch block
				allLinksVerified = false;
				e.printStackTrace();
			}
		}
		/*
		 * if (allLinksVerified && link_count != 0) { status("info",
		 * "All Links Validated, Total No of Links Validated -->" + link_count +
		 * " in parent URL -->" + homePage); } else if (link_count != 0) {
		 * status("Warning",
		 * "Not able to Validate all the Links, Total No of Links Validated -->" +
		 * link_count + " in parent URL -->" + homePage); } else { status("info",
		 * "No links found" + " in the parent URL -->" + homePage); }
		 */
		return allLinksVerified;

	}

	public boolean validateLink(By elementBy) throws Exception {
		boolean allLinksVerified = true;
		String homePage = driver.getCurrentUrl();
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		String text = "N/A";
		try {
			text = driver.findElement(elementBy).getText();
		} catch (Exception e) {

		}
		List<WebElement> links = driver.findElements(elementBy);

		Iterator<WebElement> it = links.iterator();
		int link_count = 0;
		while (it.hasNext()) {

			url = it.next().getAttribute("href");

			// status("info", "Validating Link " + (++link_count) + " -->" + url + " in
			// parent URL -->" + homePage);

			if (url == null || url.isEmpty()) {
				// System.out.println("URL is either not configured for anchor tag or it is
				// empty");
				status("skip", "'" + text + "' Link is either not configured for anchor tag or it is empty -->" + url
						+ " in parent URL -->" + homePage);
				continue;
			}

			if (!url.startsWith(homePage)) {
				// System.out.println("URL belongs to another domain, skipping it.");
				status("skip", "'" + text + "' Link belongs to another domain, skipping it. -->" + url
						+ " in parent URL -->" + homePage);
				continue;
			}

			try {
				huc = (HttpURLConnection) (new URL(url).openConnection());

				huc.setRequestMethod("HEAD");

				huc.connect();

				respCode = huc.getResponseCode();

				if (respCode >= 400) {
					status("fail", "'" + text + "' Invalid link  -->" + url + " in parent URL -->" + homePage);
					// System.out.println(url + " is a broken link");
				} else {
					status("pass", "'" + text + "' Valid link  -->" + url + " in parent URL -->" + homePage);
					// System.out.println(url + " is a valid link");
				}

			} catch (MalformedURLException e) {
				status("fail", "Exception Occured  -->" + url + " in parent URL -->" + homePage + " Exception -->" + e);
				// TODO Auto-generated catch block
				allLinksVerified = false;
				e.printStackTrace();
			} catch (IOException e) {
				status("fail", "Exception Occured  -->" + url + " in parent URL -->" + homePage + " Exception -->" + e);
				// TODO Auto-generated catch block
				allLinksVerified = false;
				e.printStackTrace();
			}
		}
		/*
		 * if (allLinksVerified && link_count != 0) { if (link_count > 1) {
		 * status("info", "All Links Validated, Total No of Links Validated -->" +
		 * link_count + " in parent URL -->" + homePage); } else { status("info",
		 * "Link Validated in parent URL -->" + homePage); } } else if (link_count != 0)
		 * { status("Warning",
		 * "Not able to Validate all the Links, Total No of Links Validated -->" +
		 * link_count + " in parent URL -->" + homePage); } else { status("info",
		 * "No links found" + " in the parent URL -->" + homePage); }
		 */
		return allLinksVerified;

	}

	public boolean validateLink(WebElement element) throws Exception {
		boolean allLinksVerified = true;
		String homePage = driver.getCurrentUrl();
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		String text = "N/A";
		try {
			text = element.getText();
		} catch (Exception e) {

		}

		url = element.getAttribute("href");

		boolean verify = true;
		if (url == null || url.isEmpty()) {
			// System.out.println("URL is either not configured for anchor tag or it is
			// empty");
			status("fail", "'" + text + "' Link is either not configured for anchor tag or it is empty -->" + url
					+ " in parent URL -->" + homePage);
			verify = false;
		}

		if (verify) {
			try {
				huc = (HttpURLConnection) (new URL(url).openConnection());

				huc.setRequestMethod("HEAD");

				huc.connect();

				respCode = huc.getResponseCode();

				if (respCode >= 400) {
					status("fail", "'" + text + "' Invalid link  -->" + url + " in parent URL -->" + homePage);
					// System.out.println(url + " is a broken link");
				} else {
					status("pass", "'" + text + "' Valid link  -->" + url + " in parent URL -->" + homePage);
					// System.out.println(url + " is a valid link");
				}

			} catch (MalformedURLException e) {
				status("fail", "Exception Occured  -->" + url + " in parent URL -->" + homePage + " Exception -->" + e);
				// TODO Auto-generated catch block
				allLinksVerified = false;
				e.printStackTrace();
			} catch (IOException e) {
				status("fail", "Exception Occured  -->" + url + " in parent URL -->" + homePage + " Exception -->" + e);
				// TODO Auto-generated catch block
				allLinksVerified = false;
				e.printStackTrace();
			}
		}

		return allLinksVerified;

	}

	public void failureErrorHandling(String erString) throws Exception {
		status(Status.FAIL, erString);
		String systemErrorM = "//div[@id='modalContainerDiv'][@class='show']/div/div/div/div/p/a";
		String systemErrorMessage = "/following::p";

		if (isElementVisible(errorWindow)) {
			if (isElementVisible(systemError)) {
				String errorMessage = null;
				String text1 = null;
				int i = 1;
				while (i <= 3) {
					errorMessage = driver.findElement(By.xpath(systemErrorM + systemErrorMessage)).getText();

					text1 = driver.findElement(By.xpath(systemErrorM + systemErrorMessage + "/span/a" + "[" + i + "]"))
							.getText();
					i++;
				}

				nft.logger.log(Status.FAIL,
						"<span style='color:red;font-weight:bold'> Application error occured</span>:- "
								+ getLabel(driver.findElement(systemError).getText() + errorMessage + text1));
			} else if (isElementVisible(alertError)) {
				nft.logger.log(Status.FAIL,
						"<span style='color:red;font-weight:bold'> Application error occured</span> :-"
								+ getLabel(driver.findElement(alertError).getText()));
			} else if (isElementVisible(ratesDownError)) {
				nft.logger.log(Status.FAIL,
						" Application Error Occured :- " + driver.findElement(ratesDownError).getText());
			}

		} else {
			nft.logger.log(Status.FAIL,
					"<span style='color:red;font-weight:bold'>  Object property Changed :-" + erString + "</span>");
		}
	}

	private String getLabel(String text) {
		return "<span class='label outline info'>" + text + "</span>";
	}

	protected String getStackTrace(String stacktrace) {
		stacktrace = sw.toString();
		return stacktrace.replace(System.getProperty("line.separator"), "<br/>\n");
	}

	public String getTestData(String columnName) {
		// System.out.println(data);
		if (data.get(columnName) != null && data.get(columnName).length() > 0) {
			// System.out.println("Not Null");
			String[] var = data.get(columnName).split(";");
			if (var.length > dataPosition)
				return var[dataPosition].trim();
			else
				return "";
		}
		return "";
	}

	public String getCompleteTestData(String columnName) {
		// System.out.println(data);
		if (data.get(columnName) != null && data.get(columnName).length() > 0) {
			// System.out.println("Not Null");
			return data.get(columnName).trim();
		}
		return "";
	}

	public boolean waitForElementPresentAndVerifyText(By elementBy, String text, int waitForSeconds) throws Exception {
		boolean elementTextMatch = false;
		int count = 0;
		while (true) {
				
			if (fluentWait(elementBy, waitForSeconds)) {
				status("pass", elementBy + " element found!");
				if (driver.findElement(elementBy).getText().trim().equals(text.trim()) || count >= waitForSeconds) {
					elementTextMatch = true;
					break;
				}
				count++;
				Thread.sleep(1000);
			}
		}
		if (elementTextMatch) {
			status("pass", text + " matched even after polling for " + count + " seconds");
		} else {
			status("fail", text + " not matched even after polling for " + waitForSeconds + " seconds");
		}

		return elementTextMatch;

	}



public static String randomdigit(int noOfDigits) {
	String rand = "";
	int i = 0;
	while (i < noOfDigits) {
		rand += new Random().nextInt(10);
		i++;
	}
	return rand;
}

public static String rand(String filename) throws IOException {
	Scanner sc = new Scanner(new File(filename));
	String content = "";
	while (sc.hasNextLine()) {

		String line = sc.nextLine();
		if (line.contains("icicirandom")) {
			line = line.replace("icicirandom", randomdigit(19));
		}
		if (line.contains("imsirandom")) {
			line = line.replace("imsirandom", randomdigit(9));
		}
		content += line + "\r";
	}
	sc.close();
	/*
	 * FileWriter fw =new FileWriter(filename);; fw.write(content); fw.flush();
	 * fw.close();
	 */
	return content;
}


}



