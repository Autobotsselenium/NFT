package com.VolvoCars;

import java.util.LinkedHashMap;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import nft.TestExecutor.*;

public class Login_Volvo
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
	public static By passworchina = By.xpath("//input[@name=\"passwd\"]");
	public static By next1 = By.xpath("//span[@class='CwaK9']");
	public static By Submit = By.xpath("//span[@id=\"submitButton\"]");
	public static By Submitchina = By.xpath("//input[@type=\"submit\"]");
	public static By VSSMrep = By.xpath("(//span[text()=\"VSSM\"])[3]");
	public static By Icon = By.xpath("(//a[@tabindex='0'])[4]");
	public static By logout = By.xpath("(//a[@target='_top'])[5]");
	String gmusername = "jgnanara@volvocars.com";
	String paswrd = "Lockdown@98765";
	public static By Username_vovlo=By.xpath("//input[@type='email']");
	public static By Next=By.xpath("//input[@type='submit']");
	public static By Dash = By.xpath("//span[text()=\"Dashboards\"]");
	public static By Pdfselect = By.xpath("//select[@class=\"md-select\"]");
	public static By Moresetting = By.xpath("//div[text()=\"More settings\"]");
	public static By brand = By.xpath("(//span[text()=\"Brand\"])[6]");

	public static By Vssm=By.xpath("//div[text()='VSSM']");
	String Expectedtitle = "VSSM - Dynamics 365";
	public static void main(String[] args) {
		System.out.println(Login_Volvo.randomdigit(13));
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

		return rand;


	}

	public static String pageName="Login page";
	private Session session;

	public Login_Volvo(LinkedHashMap<String, String> data, WebDriver driver, int count,Session ses) {
		super();
		this.driver = driver;
		this.data = data;
		this.session = ses;
	}

	public void Dashdownload() throws Exception 
	{
		/*
*/
		
		String URL1="";
		if(session.getTestData("CountryCode").equals("ROW")) {
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
			URL1="https://vssm.crm.dynamics.cn/";
			break;
		}}else {
			switch(session.getTestData("Level")) {
			case "QA":
				URL1="https://test-vssm.crm.dynamics.cn/";
				break;
			case "Prod":
				URL1="https://vssm.crm.dynamics.cn/";
				break;
			}
		}
	

		session.getURL(URL1);
		session.fluentWait(Username_vovlo, 30);
		session.sendKeys(Username_vovlo, "Userid");
		session.clickElement(Next);	
		
		session.sendKeys(passworchina, "Password");

		session.clickElement(Submitchina);
		Thread.sleep(5000);
		session.enterkeyboard();
		session.getDriver().switchTo().frame("AppLandingPage");

		session.clickElement(Vssm);

		Thread.sleep(10000);
		/*String Acutaltitle = session.getDriver().getTitle();
		System.out.println(Acutaltitle);
		if (Expectedtitle.equals(Acutaltitle)) {
			System.out.println("User landed in homepage");
			session.status(Status.PASS, "User landed in homepage");
		}
		else {
			//				throw new Exception("wrong page ot no title found");
			session.status(Status.FATAL, "wrong page ot no title found");
		}*/







		//		Thread.sleep(300000);
	}





	private String getURL() {
		// TODO Auto-generated method stub
		return null;
	}
}
