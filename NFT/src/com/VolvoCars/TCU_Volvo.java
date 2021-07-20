package com.VolvoCars;

import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import nft.TestExecutor.*;

public class TCU_Volvo
{
	private WebDriver driver;
	public static int iStepNumber;
	private LinkedHashMap<String, String> data;

	
	public static By username = By.xpath("//input[@name='username']");
	public static By password = By.xpath("//input[@name='password']");
	public static By login = By.xpath("//input[@name='login']");
	public static By TCU = By.xpath("//span[text()=\"TCU\"]");
	public static By CreatenewTCU = By.xpath("//span[text()=\"New\"]");
	public static By IMEI = By.xpath("//input[@aria-label=\"IMEI\"]");
	public static By TCU_type = By.xpath("//select[@aria-label=\"Type\"]");
	public static By CountyVar = By.xpath("//input[@aria-label=\"Country Variant\"]");
	public static By Manfacture = By.xpath("//select[@aria-label=\"Manufacturer\"]");
	public static By SaveTCU = By.xpath("(//button[@aria-label=\"Save\"]//following-sibling::span)[1]");
	public static By SIM = By.xpath("//li[text()=\"SIM\"]");
	public static By AddexistSIM = By.xpath("//span[text()='Add Existing SIM']");
	public static By TCUSIM = By.xpath("//li[text()=\"SIM\"]");
	public static By AddSIM = By.xpath("//span[text()='Add']");
	public static By assigned = By.xpath("//label[text()='Assigned']");
	String ExpectedSIMlinkStat = "Assigned";

	public static String pageName="Login page";
	private Session session;
	By TCULookup;

	public TCU_Volvo(LinkedHashMap<String, String> data, WebDriver driver, int count,Session ses) {
		super();
		this.driver = driver;
		this.data = data;
		this.session = ses;
	}

	public void TCU(Sim_Volvo f) throws Exception 
	{
		
		session.clickElement(TCU);
		Thread.sleep(5000);
		session.clickElement(CreatenewTCU);
		Thread.sleep(5000);
		session.sendKeys(IMEI, Login_Volvo121212121.randomdigit(15), "15 digits random");
		String IMEIval = session.getAttribute(IMEI, "value");
		this.TCULookup = By.xpath("//span[text()='"+IMEIval+"']");
		Thread.sleep(5000);
		session.selectValueByVisibleText(TCU_type, "TCU_TYPE");
		Thread.sleep(5000);
		session.sendKeys(CountyVar,"Country_Varaint");
		Thread.sleep(5000);
		while(true) {
			try{
				driver.findElement(Manfacture);break;
			}catch(Exception e) {
				Actions action=new Actions(session.getDriver());
				action.sendKeys(Keys.PAGE_DOWN).build().perform();
			}
		}
		//		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		//		Thread.sleep(5000); 
		session.selectValueByVisibleText(Manfacture, "Manufacturer");
		session.clickElement(SaveTCU);
		Thread.sleep(5000);
		session.clickElement(TCUSIM);
		session.clickElement(AddexistSIM);
		session.clickElement(f.EIDSIM);
		session.clickElement(AddSIM);
		Thread.sleep(6000);
		String SimlinkStats = session.getText(assigned);
		if (ExpectedSIMlinkStat.equals(SimlinkStats)) {
			System.out.println("Sim Sucessfully Linked to TCU");
			session.status(Status.PASS, "Sim Sucessfully Linked to TCU");
		}
		else {
			//				throw new Exception("MNO Link failed");
			session.status(Status.FATAL, "SIM Link failed");
		}
		
	}

	private String getURL() {
		// TODO Auto-generated method stub
		return null;
	}
}
