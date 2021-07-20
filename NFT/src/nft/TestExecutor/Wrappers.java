package nft.TestExecutor;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
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
//import com.relevantcodes.extentreports.LogStatus;

//Commented by Ragu
//import Report.HTMLReportTemplate;
@Deprecated
public class Wrappers {
	public static int noWaitTime = 1;
	public static int waitTime = 30;
	private LinkedHashMap<String,String> data;
	public static boolean continueOnClickFail = false;
	public static boolean elementPrestFail    = false;
	// public static WebDriver driver;
	public static String currentBrowser = Properties.executionBrowserName;
	
	public static   By errorWindow=By.xpath("//div[@id='modalContainerDiv'][@class='show']");
	public static 	By systemError=By.xpath("//div[@id='modalContainerDiv'][@class='show']/div/div/div/div/p/a");
	public static   By alertError=By.xpath("//div[@id='modalContainerDiv'][@class='show']/div/div/div/div/span");
	public static   By ratesDownError=By.xpath("//div[@id='modalContainerDiv'][@class='show']/div/div/div/div[2]");
	static StringWriter sw = new StringWriter();
	public int dataPosition=0;
	
	public Wrappers(LinkedHashMap<String, String> data) {
		super();
		this.data = data;
	}

	/**
	 * Method to check if element is present
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public static boolean isElementPresent(WebDriver driver,By elementBy) throws Exception {
		System.err.println("Use session to get best results");
		return isElementPresent(driver,elementBy, 5);
	}

	/**
	 * Method to check if element is present after waiting for specific time
	 * 
	 * @param elementBy
	 * @return
	 */
	public static boolean isElementPresentAfterWait(WebDriver driver, final By elementBy) {
		System.err.println("Use session to get best results");
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

		} catch (Exception e) {

			System.out.println("Generic Exception occured");
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
	public static boolean noWaitElementPresent(WebDriver driver,By elementBy) throws Exception {

		return isElementPresent(driver,elementBy, 1);
	}

	/**
	 * Method to check if element is not present without wait
	 * 
	 * @param elementBy
	 * @return
	 */
	public static boolean isElementNotPresent(WebDriver driver, By elementBy) {

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

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementNotPresent;
	}

	/**
	 * Method to check element is present within parent element
	 * 
	 * @param element
	 * @param elementBy
	 * @return
	 */
	public static boolean isElementPresent(WebDriver driver, WebElement element, By elementBy) {

		boolean elementPresent = false;

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

		try {

			elementPresent = element.findElement(elementBy).isDisplayed();

		} catch (Exception ex) {

			System.out.println("Exception occured while finding element within element");
		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

		return elementPresent;
	}

	/**
	 * Method to click element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public static void clickElement(int iStepNumber,WebDriver driver, By elementBy,String sText,String sPageName) throws Exception {

		boolean exceptionOccured = false;
		String beforeClickUrl = driver.getCurrentUrl();

		try {

			if (isElementPresent(driver,elementBy, 2)) {

				

				try {

					try {

						driver.findElement(elementBy).click();

						if (beforeClickUrl.equalsIgnoreCase(driver.getCurrentUrl()) && currentBrowser == "IE") {

							jsClick(driver,elementBy);
						}
						//Commented and updated by Ragu
						//NFT_Engine.logger.log(Status.PASS, "Step # "+iStepNumber+" -> '"+sText +"' is clicked in the  '"+sPageName+"' page.");
						NFT_Engine.CreateNode(Status.PASS, "Step # "+iStepNumber+" -> '"+sText +"' is clicked in the  '"+sPageName+"' page.");
						/*
						 * if(currentBrowser == Browser.IE) {
						 * jsClick(elementBy); }
						 */

					} catch (NoSuchElementException nsee) {

						exceptionOccured = true;

					}

				} catch (Exception ex) {

					exceptionOccured = true;

				}

			} else {
				
				//below line updated by Ragu
				//NFT_Engine.logger.log(Status.FAIL, "Step # "+iStepNumber+" -> '"+sText +"' is Not clicked in the  '"+sPageName+"' page.");
				
				if (!continueOnClickFail) {

					throw new Exception("Custom Error");
					
				}
				
			}

			driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);

			
		} catch (Exception ex) {

			handleAlert(driver);

			
		}
	}

	/**
	 * Method to click element
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public static void clickNoUrlValidation(WebDriver driver, By elementBy) throws Exception {

		boolean exceptionOccured = false;

		if (isElementPresent(driver,elementBy)) {

			

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

					jsClick(driver,elementBy);

				} catch (Exception ex) {

				}
			}

		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to click element
	 * 
	 * @param element
	 * @throws Exception
	 */
	public static void clickElement(WebDriver driver, WebElement element) throws Exception {
		System.err.println("Use session to get best result's");
		boolean exceptionOccured = false;

		if (isElementPresent(driver,element, 5)) {

			

			try {
				try {

					element.click();

					/*
					 * if(currentBrowser == Browser.IE) { jsClick(elementBy); }
					 */

				} catch (NoSuchElementException nsee) {

					exceptionOccured = true;

				}

			} catch (Exception ex) {

				exceptionOccured = true;
				failureErrorHandling(driver, ex.getMessage());

			}

			if (exceptionOccured) {

				try {

					jsClick(driver,element);

				} catch (Exception ex) {
					//failureErrorHandling(driver, ex.getMessage());
				}
			}

		}

		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to click using Actions
	 * 
	 * @param elementBy
	 */
	public static void clickUsingActions(WebDriver driver, By elementBy) {

		try {

			if (isElementPresent(driver,elementBy)) {

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
	public static void doubleClickUsingAction(WebDriver driver, By elementBy) {

		try {

			if (isElementPresent(driver,elementBy)) {

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
	public static boolean isAlertPresent(WebDriver driver) {
		System.err.println("Use session to get best result's");
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
	 */
	public static void clickElement(WebDriver driver,WebElement element, By elementBy) {
		System.err.println("Use session to get best result's");
		if (isElementPresent(driver,element, elementBy)) {

			element.findElement(elementBy).click();
		}
	}

	/**
	 * Method to check if element is enabled
	 * 
	 * @param elementBy
	 */

	public static boolean isEnabled(WebDriver driver, By elementBy) {
		
		return driver.findElement(elementBy).isEnabled();
	}

	/**
	 * Method to mouse over element using javascript
	 * 
	 * @param element
	 */
	public static void mouseOver(WebDriver driver, WebElement element) {

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
	public static void mouseOver(WebDriver driver, By elementBy) throws Exception {

		WebElement weElement = driver.findElement(elementBy);

		String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";

		if (isElementPresent(driver,elementBy, 5)) {

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
	public static void jsClick(WebDriver driver, By elementBy) throws Exception {
		System.err.println("Use session to get best result's");
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
	public static void jsClick(WebDriver driver, WebElement weElement) throws Exception {
		System.err.println("Use session to get best result's");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", weElement);
	}

	/**
	 * Method to click element using javascript
	 * 
	 * @param documentId
	 */
	public static void jsClick(WebDriver driver, String documentId) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("document.getElementById('hlViewStoreFinder').click();");

	}

	/**
	 * Method to focus element
	 * 
	 * @param elementBy
	 */
	public static void focusElement(WebDriver driver, By elementBy) {

		try {
			System.err.println("Use session to get best result's");
			jsFocus(driver, driver.findElement(elementBy));

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
	private static void jsFocus(WebDriver driver, WebElement element) {
		System.err.println("Use session to get best result's");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		((RemoteWebDriver) executor).executeScript("arguments[0].click();", element);
	}

	/**
	 * Method to clear text element
	 * 
	 * @param elementBy
	 */
	public static void clearElement(WebDriver driver, By elementBy) {
		System.err.println("Use session to get best result's");
		driver.findElement(elementBy).sendKeys(Keys.HOME);
		driver.findElement(elementBy).sendKeys("");
		driver.findElement(elementBy).clear();
	}

	/**
	 * Method to navigate back
	 */
	public static void navigateBack(WebDriver driver) {

		driver.navigate().back();
	}

	/**
	 * Method to navigate to url
	 */
	public static void navigateTo(WebDriver driver, String url) {

		driver.navigate().to(url);
	}

	/**
	 * Method to Get to url
	 */
	public static void getURL(WebDriver driver, String url) {

		driver.get(url);
	}

	/**
	 * Method to select value for dropdown list
	 * 
	 * @param elementBy
	 * @param label
	 * @throws Exception 
	 */
	 //updated and commented by Ragu
	public static void selectValueByVisible(int iStepNumber,WebDriver driver, By elementBy, String label,String sDropDownName,String sPageName ) throws Exception {
      try
      {System.err.println("Use session to get best result's");
    	  if (isElementPresent(driver,elementBy, 1)) {
    		  
    	  
    	//  String sPassingMessage = "Step # "+iStepNumber+" -> '"+label + "' is selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.";
  		  new Select(driver.findElement(elementBy)).selectByVisibleText(label);
    	 
  		
  	  		//  NFT_Engine.logger.log(Status.PASS, sPassingMessage);
  			//NFT_Engine.CreateNode(Status.PASS, sPassingMessage);
  			NFT_Engine.CreateNode(Status.PASS, "Step # "+iStepNumber+" -> '"+label + "' selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.");
    	  }
    	  else
			{
				//NFT_Engine.logger.log(Status.FAIL, "Step # "+iStepNumber+" -> ' The expected field is not available or locator is incorrect : '"+EditBox+"' in the '"+sPageName+"' page.");
    		  //String sPassingMessage = "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.";
    		 // NFT_Engine.CreateNode(Status.FAIL, "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.");
    		 // NFT_Engine.logger.log(Status.FAIL, "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.");
			}
    	  
       }
     
      catch(Exception e)
      {
    	  
    	 // String sPassingMessage = "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.";
    	//  NFT_Engine.logger.log(Status.FAIL, e.getMessage());
    	// NFT_Engine.CreateNode(Status.FAIL, e.getMessage());
    	  //Commented by Ragu
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
	 //updated and commented by Ragu
	public static void selectByValue(int iStepNumber,WebDriver driver, By elementBy, String label,String sDropDownName,String sPageName ) throws Exception {
      try
      {System.err.println("Use session to get best result's");
    	  if (isElementPresent(driver,elementBy, 1)) {
    		  
    	
    	 new Select(driver.findElement(elementBy)).selectByValue(label); 
  		
  	  		//  NFT_Engine.logger.log(Status.PASS, sPassingMessage);
  			//NFT_Engine.CreateNode(Status.PASS, sPassingMessage);
  			NFT_Engine.CreateNode(Status.PASS, "Step # "+iStepNumber+" -> '"+label + "' selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.");
    	  }
    	  else
			{
				//NFT_Engine.logger.log(Status.FAIL, "Step # "+iStepNumber+" -> ' The expected field is not available or locator is incorrect : '"+EditBox+"' in the '"+sPageName+"' page.");
    		  //String sPassingMessage = "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.";
    		 // NFT_Engine.CreateNode(Status.FAIL, "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.");
    		//  NFT_Engine.logger.log(Status.FAIL, "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.");
			}
    	  
       }
     
      catch(Exception e)
      {
    	  
    	 // String sPassingMessage = "Step # "+iStepNumber+" -> '"+label + "' Not selected from the dropdwon list for : '" +sDropDownName+"' in the '"+sPageName+"' page.";
    	//  NFT_Engine.logger.log(Status.FAIL, e.getMessage());
    	// NFT_Engine.CreateNode(Status.FAIL, e.getMessage());
    	  //Commented by Ragu
    	// failureErrorHandling(driver, e.getMessage());
      } 
     
	}


	//
	/**
	 * Method to send keys to text element
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public static void sendKeys(int iStepNumber,WebDriver driver, By elementBy, String typeValue,String EditBox,String sPageName) throws Exception {

		try {
			System.err.println("Use session to get best result's");
			if (isElementPresent(driver,elementBy, 1)) {

				driver.findElement(elementBy).clear();
				driver.findElement(elementBy).sendKeys(typeValue);
				NFT_Engine.CreateNode(Status.PASS, "Step # "+iStepNumber+" -> '"+typeValue +"' is entered for the field : '"+EditBox+"' in the '"+sPageName+"' page.");
				//	NFT_Engine.logger.log(Status.PASS, "Step # "+iStepNumber+" -> '"+typeValue +"' is entered for the field : '"+EditBox+"' in the '"+sPageName+"' page.");
			}
			// below lines updated by Ragu
			else
			{
				
				//NFT_Engine.logger.log(Status.FAIL, "Step # "+iStepNumber+" -> ' The expected field is not available or locator is incorrect : '"+EditBox+"' in the '"+sPageName+"' page.");
			}
			//
			

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Method to send characters to text element
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public static void sendChars(WebDriver driver, By elementBy, String typeChar) throws Exception {

		if (isElementPresent(driver,elementBy)) {

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
	public static String getText(WebDriver driver, By elementBy) throws Exception {

		if (noWaitElementPresent(driver,elementBy)) {

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
	public static Double getDouble(WebDriver driver,By elementBy) throws Exception {

		Double retValue = 0.0;

		try {

			retValue = Double.valueOf(getText(driver,elementBy));

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
	public static Double getDouble(String str) throws Exception {

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
	public static int getInteger(WebDriver driver,By elementBy) throws Exception {

		int retValue = 0;

		try {

			retValue = Integer.valueOf(getText(driver,elementBy));

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
	public static String getText(WebDriver driver,WebElement element, By elementBy) {

		if (isElementPresent(driver,element, elementBy)) {

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
	public static String getText(WebDriver driver,WebElement element) throws Exception {

		if (isElementPresent(driver,element, 5)) {

			return element.getText().trim();

		} else {

			return "";

		}
	}

	/**
	 * Method to handle alert box
	 */
	public static void handleAlert(WebDriver driver) {

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
	public static void waitForElementPresent(WebDriver driver,final By elementBy) throws Exception {

		isElementPresent(driver,elementBy, 15);
		
		
	}

	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @throws Exception
	 */
	public static void waitForElementPresent(WebDriver driver,final WebElement elementBy) throws Exception {

		isElementPresent(driver,elementBy, 5);
	}
	/**
	 * Method to wait for specific time till element is present
	 * 
	 * @param elementBy
	 * @param waitTimeSecs
	 * @throws InterruptedException
	 */
	public static void waitForElementPresent(WebDriver driver,final By elementBy, int waitTimeSecs) throws Exception {

		isElementPresent(driver,elementBy, waitTimeSecs);
	}

	/**
	 * Method to check if element is Present within some seconds
	 * 
	 * @param elementBy
	 * @param waitForSeconds
	 * @return
	 * @throws InterruptedException
	 */
	public static boolean isElementPresent(WebDriver driver, By elementBy, int waitForSeconds) throws Exception {
		System.err.println("Use session to get best result's");
		boolean elementPresent = true;
		int count = 0;

		

		while (elementPresent) {
			try {
				if (driver.findElement(elementBy).isDisplayed()) {
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
	 * Method to check if element is Present with in an element for some seconds
	 * 
	 * @param element
	 * @param elementBy
	 * @param waitForSeconds
	 * @return
	 * @throws Exception
	 */
	public static boolean isElementPresent(WebDriver driver, WebElement element, By elementBy, int waitForSeconds)
			throws Exception {
		System.err.println("Use session to get best result's");
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
	public static boolean isElementPresent(WebDriver driver, WebElement element, int waitForSeconds) throws Exception {
		System.err.println("Use session to get best result's");
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
	public static void waitForPageLoaded(WebDriver driver) {

		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
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

	/**
	 * Component to move actual mouse pointer to top left corner of page.
	 * 
	 * @throws AWTException
	 */
	public static void moveActualMouse() throws AWTException {

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
	public static void moveActualMouse(int x, int y) throws AWTException {

		Robot robot = new Robot();
		robot.mouseMove(x, y);
		robot = null;
	}

	/**
	 * Component to click on close button of overlay and wait to until the
	 * overlay closes itself.
	 * 
	 * @param elementToCheck
	 * @param closeButton
	 * @throws Exception
	 */
	public static void clickAndWaitUntilDisappear(WebDriver driver,By elementToCheck, By closeButton) throws Exception {

		if (isElementPresent(driver,elementToCheck, 0)) {

		//	clickElement(driver,closeButton);

			while (true) {

				if (isElementPresent(driver,elementToCheck, 1)) {

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
	public static void waitUntilDisappear(WebDriver driver,By elementToCheck) throws Exception {

		int i = 0;

		while (true) {

			if (isElementPresent(driver,elementToCheck, 1)) {

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
	public static void pasteText(WebDriver driver,By pasteBy) throws Exception {

		if (isElementPresent(driver,pasteBy)) {

			clearElement(driver,pasteBy);
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
	public static String getAttribute(WebDriver driver,By elementBy, String attribute) throws Exception {

		String attributeValue = "";

		if (isElementPresent(driver,elementBy)) {

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
	public static String getAttribute(WebDriver driver,WebElement element, String attribute) throws Exception {

		String attributeValue = "";

		if (isElementPresent(driver,element, 2)) {

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
	public static String getAttribute(WebDriver driver,WebElement element, By elementBy, String attribute) throws Exception {

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
	public static String getCssValue(WebDriver driver,By elementBy, String cssAttribute) throws Exception {

		String cssValue = "";

		if (isElementPresent(driver,elementBy)) {

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
	public static String getCssValue(WebDriver driver,WebElement element, By elementBy, String cssAttribute) throws Exception {

		String cssValue = "";

		if (isElementPresent(driver,element, elementBy)) {

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
	public static Point getLocation(WebDriver driver,By elementBy) throws Exception {

		Point locations = new Point(0, 0);

		if (isElementPresent(driver,elementBy, 5)) {

			locations = driver.findElement(elementBy).getLocation();
		}

		return locations;
	}

	/**
	 * Method to refresh web page
	 * 
	 * @throws Exception
	 */
	public static void refresh(WebDriver driver) throws Exception {

		driver.navigate().refresh();
	}

	/**
	 * Method to get no of webElements count
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public static int getElementsCount(WebDriver driver,By elementBy) throws Exception {

		int noOfElements = 0;

		if (noWaitElementPresent(driver,elementBy)) {

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
	public static List<WebElement> getElements(WebDriver driver,By elementBy) throws Exception {

		List<WebElement> lstWebElements = new ArrayList<WebElement>();

		if (isElementPresent(driver,elementBy, 2)) {

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
	public static List<WebElement> getElements(WebDriver driver,WebElement webElement, By elementBy) throws Exception {

		List<WebElement> lstWebElements = new ArrayList<WebElement>();

		if (isElementPresent(driver,webElement, elementBy, 2)) {

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
	public static int getElementsCount(WebDriver driver,WebElement webElement, By elementBy) throws Exception {

		int noOfElements = 0;

		if (isElementPresent(driver,webElement, elementBy, 0)) {

			noOfElements = webElement.findElements(elementBy).size();
		}

		return noOfElements;
	}

	/**
	 * Method to open new tab
	 * 
	 * @param url
	 */
	public static void openTab(WebDriver driver,String url) {

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
	public static void closeTab(WebDriver driver) throws InterruptedException {

		 String originalHandle = driver.getWindowHandle();

		    //Do something to open new tabs

		    for(String handle : driver.getWindowHandles()) {
		        if (!handle.equals(originalHandle)) {
		            driver.switchTo().window(handle);
		            driver.close();
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
	public static void trigger(WebDriver driver,String script, WebElement element) {

		((JavascriptExecutor) driver).executeScript(script, element);
	}

	/**
	 * Executes a script
	 * 
	 * @note Really should only be used when the web driver is sucking at
	 *       exposing functionality natively
	 * @param script
	 *            The script to execute
	 */
	public static Object trigger(WebDriver driver,String script) {

		return ((JavascriptExecutor) driver).executeScript(script);
	}

	/**
	 * Method to get width in pixels of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public static int getWidth(WebDriver driver,By elementBy) throws Exception {

		int width = 0;

		if (noWaitElementPresent(driver,elementBy)) {

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
	public static int getHeight(WebDriver driver,By elementBy) throws Exception {

		int height = 0;

		if (noWaitElementPresent(driver,elementBy)) {

			height = driver.findElement(elementBy).getSize().getHeight();
		}

		return height;
	}

	/**
	 * Method to clear browser history
	 * 
	 * @throws Exception
	 */
	public static void clearHistory(WebDriver driver) throws Exception {
		System.err.println("Use session to get best result's");
		driver.manage().deleteAllCookies();
		refresh(driver);
	}

	/**
	 * Method to set display style of element to block
	 * 
	 * @param elementBy
	 */
	public static void blockElement(WebDriver driver,By elementBy) {

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
	public static boolean isSelected(WebDriver driver,By elementBy) throws Exception {

		return driver.findElement(elementBy).isSelected();
	}

	/**
	 * This method checks the ascending order of list.
	 * 
	 */
	public static boolean isAscending(List<Integer> sortOrder) {

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
	public static boolean isDescending(List<Integer> sortOrder) {

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
	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	/**
	 * Function that returns a string within quotes. Intended to be used in
	 * reports.
	 * 
	 */
	public static String inQuotes(String str) {

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
	 */
	public static void moveToElement(WebDriver driver,By elementBy) {
		System.err.println("Use session to get best result's");
		Actions act = new Actions(driver);
		WebElement mainMenu = driver.findElement(elementBy);
		act.moveToElement(mainMenu).build().perform();
	}

	/**
	 * Method to send keys to text element without clearing
	 * 
	 * @param elementBy
	 * @param typeValue
	 * @throws InterruptedException
	 */
	public static void sendKeysWithoutClearing(WebDriver driver,By elementBy, String typeValue) throws Exception {
		System.err.println("Use session to get best result's");
		if (isElementPresent(driver,elementBy)) {

			driver.findElement(elementBy).sendKeys(typeValue);
		}
	}

	/**
	 * Method to getLocation for a particular element
	 * 
	 * @param elementBy
	 * @return
	 * @throws Exception
	 */
	public static Point getLocation(WebDriver driver,WebElement element, By elementBy) throws Exception {
		System.err.println("Use session to get best result's");
		Point location = new Point(0, 0);

		if (isElementPresent(driver,element, elementBy, 5)) {

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
	public static String getText(WebDriver driver,By parentElement, By elementBy) throws Exception {
		System.err.println("Use session to get best result's");
		String text = "";

		if (isElementPresent(driver,parentElement, 2)) {

			WebElement element = driver.findElement(parentElement);

			if (isElementPresent(driver,element, elementBy)) {

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
	public static void clickElement(WebDriver driver,By parentElementBy, By elementBy) throws Exception {
		System.err.println("Use session to get best result's");
		if (isElementPresent(driver,parentElementBy, 2)) {

			WebElement parentElement = driver.findElement(parentElementBy);

			if (isElementPresent(driver,parentElement, elementBy)) {

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
	public static String removeDecimalvalue(String str) throws Exception {
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
	public static void sendKeys(WebDriver driver,By elementBy, Keys typeValue) throws Exception {
		System.err.println("Use session to get best result's");
		try {

			if (isElementPresent(driver,elementBy, 1)) {

				driver.findElement(elementBy).sendKeys(typeValue);
			}

		} catch (Exception ex) {

			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Method to get Selected Option text of an element
	 * 
	 * @param elementBy
	 * @return
	 * @throws InterruptedException
	 */
	public static String getSelectedOptionText(WebDriver driver,By elementBy) throws Exception {
		System.err.println("Use session to get best result's");
		if (noWaitElementPresent(driver,elementBy)) {
			String Text = new Select(driver.findElement(elementBy)).getFirstSelectedOption().getText().trim();
			return Text;
		} else {

			return "";
		}
	}
	public static boolean isElementVisible(WebDriver driver, By elementBy) throws Exception {
		System.err.println("Use session to get best result's");
		boolean elementPresent = false;
		
			try {
				if (driver.findElement(elementBy).isDisplayed()) {
					elementPresent=true;
				
				}
			} catch (Exception ex) {
				
					elementPresent = false;
					
				}

		
		return elementPresent;
	}
	
	public static void failureErrorHandling(WebDriver driver,String erString) throws Exception
	{
		String systemErrorM="//div[@id='modalContainerDiv'][@class='show']/div/div/div/div/p/a";
		String systemErrorMessage="/following::p";
		  
		if(isElementVisible(driver,errorWindow)) 
  	    {
  		  if(isElementVisible(driver, systemError))
  		  {
  			  String errorMessage = null;
  			  String text1 = null;
  			  int i=1;
  			  while(i<=3)
  			  {
  				errorMessage=driver.findElement(By.xpath(systemErrorM+systemErrorMessage)).getText();
  				
  				 text1=driver.findElement(By.xpath(systemErrorM+systemErrorMessage+"/span/a"+"["+i+"]")).getText();
  				 i++;
  			  }
  			 
  	  		//  NFT_Engine.logger.log(Status.FAIL, "<span style='color:red;font-weight:bold'> Application error occured</span>:- "+getLabel(driver.findElement(systemError).getText()+errorMessage+text1));
  		  }
  		  else if(isElementVisible(driver, alertError))
  		  {
  		//	NFT_Engine.logger.log(Status.FAIL, "<span style='color:red;font-weight:bold'> Application error occured</span> :-"+getLabel(driver.findElement(alertError).getText()));
  		  }
  		  else if(isElementVisible(driver, ratesDownError))
  		  {
  		//	NFT_Engine.logger.log(Status.FAIL, " Application Error Occured :- "+driver.findElement(ratesDownError).getText()); 
  		  }
  		
  	    }
		else
		{
		//	NFT_Engine.logger.log(Status.FAIL, "<span style='color:red;font-weight:bold'>  Object property Changed :-"+erString+"</span>");
		}
	}
	private static String getLabel(String text) {
	    return "<span class='label outline info'>" + text + "</span>";
	}

	 protected static String getStackTrace(String stacktrace) {
		    stacktrace = sw.toString();
	        return stacktrace.replace(System.getProperty("line.separator"), "<br/>\n");
	    }
	 public String getTestData(String columnName) {
		 System.err.println("Use session to get best result's");
		 System.out.println(data);
			if(data.get(columnName) != null && data.get(columnName).length()>0)
			{
				System.out.println("Not Null");
				String[] var = data.get(columnName).split(";");
				if (var.length > dataPosition)
					return var[dataPosition];
				else
					return "";
			}
			return "";
		}

	
}
