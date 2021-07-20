package com.VolvoCars;

import java.util.LinkedHashMap;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;

import nft.TestExecutor.*;

public class Login_Volvo121212121
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
	public static By passwordg = By.xpath("//input[@id=\"passwordInput\"]");
	public static By next1 = By.xpath("//span[@class='CwaK9']");
	public static By Submit = By.xpath("//span[@id=\"submitButton\"]");
	public static By Icon = By.xpath("(//a[@tabindex='0'])[4]");
	public static By logout = By.xpath("(//a[@target='_top'])[5]");
	String gmusername = "jgnanara@volvocars.com";
	String paswrd = "Lockdown@98765";
	public static By Username_vovlo=By.xpath("//input[@type='email']");
	public static By Next=By.xpath("//input[@type='submit']");
	public static By Vssm=By.xpath("//div[text()='VSSM']");
	String Expectedtitle = "VSSM - Dynamics 365";
	public static void main(String[] args) {
		System.out.println(Login_Volvo121212121.randomdigit(13));
	}
    public static String randomdigit(int noOfDigits) {
    	String rand="";
    	int i=0;
    	while(i<noOfDigits) {
    		/*if(i==0)
    		{
    			int tmp=new Random().nextInt(10);
    			if(tmp!=0) {
    				i++;
    				rand+=String.valueOf(tmp);
    			}
    		}
    		else {*/
    			rand+=new Random().nextInt(10);i++;
//    		}
    	}
    /*	Long min = Long.parseUnsignedLong("1000000000000000000");  
    	Long max = Long.parseUnsignedLong("4999999999999999999");  
    	//Generate random double value from 200 to 400   
    	System.out.println("Random value of type double between "+min+" to "+max+ ":");  
    	double a = Math.random()*(max-min+1)+min;   
    	System.out.println(a);  
    	//Generate random int value from 200 to 400   
    	System.out.println("Random value of type int between "+min+" to "+max+ ":");  
    	Long b = Long.parseUnsignedLong(String.valueOf(Double.valueOf((Math.random()*(max-min+1)+min)).intValue()));  
    	System.out.println(b);  
    	return b;
		
    	*/
    	return rand;
    	
    	
    }

	public static String pageName="Login page";
	private Session session;

	public Login_Volvo121212121(LinkedHashMap<String, String> data, WebDriver driver, int count,Session ses) {
		super();
		this.driver = driver;
		this.data = data;
		this.session = ses;
	}

	public void InvokeandLogin() throws Exception 
	{
		String URL1="";
		if(session.getTestData("CountryCode").equalsIgnoreCase("ROW")) {
		switch(session.getTestData("Level")) {
		case "Dev":
			URL1="https://dev-vssm.crm4.dynamics.com/";
			break;
		case "Test":
			URL1="https://test-vssm.crm4.dynamics.com/";
			break;
		case "QA":
			URL1="https://qa-vssm.crm4.dynamics.com/";
			break;
		case "Prod":
			URL1="https://vssm.crm4.dynamics.com/";
			break;
		}}else {
			switch(session.getTestData("Level")) {
			case "Dev":
				URL1="https://dev-vssm.crm4.dynamics.com/";
				break;
			case "Test":
				URL1="https://test-vssm.crm4.dynamics.com/";
				break;
			case "QA":
				URL1="https://qa-vssm.crm4.dynamics.com/";
				break;
			case "Prod":
				URL1="https://vssm.crm4.dynamics.com/";
				break;
			}
		}
	
		session.getURL(URL1);
		session.fluentWait(Username_vovlo, 15);
		session.sendKeys(Username_vovlo, "Userid");
		session.clickElement(Next);	
		Thread.sleep(5000);
		session.sendKeys(passwordg, "Password");
	
		session.clickElement(Submit);
		Thread.sleep(5000);
		session.getDriver().switchTo().frame("AppLandingPage");
		
		session.clickElement(Vssm);
		
		Thread.sleep(10000);
		String Acutaltitle = session.getDriver().getTitle();
		System.out.println(Acutaltitle);
		if (Expectedtitle.equals(Acutaltitle)) {
			System.out.println("User landed in homepage");
			session.status(Status.PASS, "User landed in homepage");
		}
			else {
//				throw new Exception("wrong page ot no title found");
				session.status(Status.FATAL, "wrong page ot no title found");
			}
			
		
//		Thread.sleep(300000);
		}
		
		
		
	

	private String getURL() {
		// TODO Auto-generated method stub
		return null;
	}
}
