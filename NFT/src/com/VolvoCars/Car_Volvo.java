package com.VolvoCars;

import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import nft.TestExecutor.*;

public class Car_Volvo
{
	private WebDriver driver;
	public static int iStepNumber;
	private LinkedHashMap<String, String> data;

	
	public static By username = By.xpath("//input[@name='username']");
	public static By password = By.xpath("//input[@name='password']");
	public static By login = By.xpath("//input[@name='login']");
	public static By loging = By.xpath("//input[@type='email']");
	public static By next = By.xpath("//span[@class='RveJvd snByac']");
	public static By sign = By.xpath("(//a[@class='h-c-header__nav-li-link '])[4]");
	public static By passwordg = By.xpath("//input[@type='password']");
	public static By next1 = By.xpath("//span[@class='CwaK9']");
	public static By Icon = By.xpath("(//a[@tabindex='0'])[4]");
	public static By logout = By.xpath("(//a[@target='_top'])[5]");
	String gmusername = "jgnanara@volvocars.com";
	String paswrd = "Lockdown@98765";
	public static By Username_vovlo=By.xpath("//input[@type='email']");
	public static By Next=By.xpath("//input[@type='submit']");
	public static By Vssm=By.xpath("//div[text()='VSSM']");
	String Expectedtitle = "VSSM - Dynamics 365";
	public static By Car = By.xpath("//span[text()='Car']");
	public static By NewCar = By.xpath("//span[text()='New']");
	public static By VIN = By.xpath("//input[@aria-label=\"VIN\"]");
	public static By ModelYear = By.xpath("//input[@aria-label='Model Year']");
	public static By StrWeek = By.xpath("//input[@aria-label='Structure Week']");
	public static By VehType = By.xpath("//input[@aria-label='Vehicle Type Name']");
	public static By Vehcode = By.xpath("//input[@aria-label='Vehicle Type Code']");
	public static By Brand = By.xpath("//input[@aria-label='Brand, Lookup']");
	public static By Market = By.xpath("//input[@aria-label='Market, Lookup']");
	public static By Savecar = By.xpath("(//span[text()='Save'])[1]");
	public static By NewCarAct = By.xpath("//span[text()='New Car Activation']");
	public static By TCUcar = By.xpath("(//li[@aria-label='TCU'])[2]");
	public static By AddTCUtocar = By.xpath("//span[text()='Add Existing TCU']");
	public static By TCUlookupfeild = By.xpath("//input[@aria-label='Multiple Selection Lookup']");
	/*public static By AddTCUtocar = By.xpath("//span[text()='Add Existing TCU']");*/
	public static By AddTCUfinal = By.xpath("//span[text()='Add']");
	public static By Searchcar = By.xpath("//input[@aria-label=\"Search this view\"]");
	
	public static By assignedtocar = By.xpath("//label[text()='Assigned']");
	public static By Yescaract = By.xpath("//button[@aria-label=\"Yes\"]");
	public static By Yesorderconfirmation = By.xpath("//span[text()=\"Yes\"]");
	public static By Polestarbrand = By.xpath("//div[text()='POLESTAR']");
	String ExpectedMnoStat = "Activated";
	String ExpectedSIMlinkStat = "Assigned";
	String ExpectedTCUtoCARlinkStat = "Assigned";
	String Expectedbrandvalue = "POLESTAR";
	String ExpectedMarkvalue = "United States";
	
	

	public static String pageName="Login page";
	private Session session;

	public Car_Volvo(LinkedHashMap<String, String> data, WebDriver driver, int count,Session ses) {
		super();
		this.driver = driver;
		this.data = data;
		this.session = ses;
	}

	public void Carcreattion(TCU_Volvo t) throws Exception 
	{
		
		session.clickElement(Car);
		Thread.sleep(4000);
		session.sendKeys(Searchcar, "VIN");
		Thread.sleep(8000);
		session.enterkeyboard();
		Thread.sleep(8000);
		
		String vin = session.getAttribute(Searchcar, "value");
		By vinse = By.xpath("//a[@title='"+vin+"']");
		System.out.println(vin);
		session.clickElement(vinse);;

		session.clickElement(NewCarAct);
		Thread.sleep(5000);
		session.clickElement(Yescaract);
		Thread.sleep(5000);
		session.handleAlert(driver);
		session.enterkeyboard();
		
		

		
		}
		
		
		
	

	private String getURL() {
		// TODO Auto-generated method stub
		return null;
	}
}
