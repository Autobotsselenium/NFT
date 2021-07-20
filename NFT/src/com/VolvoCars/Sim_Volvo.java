package com.VolvoCars;

import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;


import nft.TestExecutor.*;

public class Sim_Volvo
{
	private WebDriver driver;
	public static int iStepNumber;
	private LinkedHashMap<String, String> data;

	public static By Submit = By.xpath("//span[@id=\"submitButton\"]");
	public static By VSSMrep = By.xpath("(//span[text()=\"VSSM\"])[3]");

	public static By logout = By.xpath("(//a[@target='_top'])[5]");

	public static By username = By.xpath("//input[@name='username']");
	public static By password = By.xpath("//input[@name='password']");
	public static By login = By.xpath("//input[@name='login']");
	public static By loging = By.xpath("//input[@type='email']");
	public static By next = By.xpath("//span[@class='RveJvd snByac']");
	public static By sign = By.xpath("(//a[@class='h-c-header__nav-li-link '])[4]");
	public static By passwordg = By.xpath("//input[@type='password']");
	public static By next1 = By.xpath("//span[@class='CwaK9']");
	public static By Icon = By.xpath("(//a[@tabindex='0'])[4]");
	public static By SIMM = By.xpath("//span[text()=\"SIM\"]");
	public static By SaveSIM = By.xpath("(//span[text()=\"Save\"])[1]");
	public static By CreatenewSIM = By.xpath("//span[text()=\"New\"]");
	public static By SIm_type = By.xpath("//select[@aria-label=\"Sim Type\"]");
	public static By ICCID = By.xpath("//input[@aria-label=\"ICCID\"]");
	public static By Username_vovlo=By.xpath("//input[@type='email']");
	public static By Next=By.xpath("//input[@type='submit']");

	public static By TCU = By.xpath("//span[text()=\"TCU\"]");
	public static By CreatenewTCU = By.xpath("//span[text()=\"New\"]");
	public static By IMEI = By.xpath("//input[@aria-label=\"IMEI\"]");
	public static By TCU_type = By.xpath("//select[@aria-label=\"Type\"]");
	public static By CountyVar = By.xpath("//input[@aria-label=\"Country Variant\"]");
	public static By Manfacture = By.xpath("//select[@aria-label=\"Manufacturer\"]");
	public static By SaveTCU = By.xpath("(//button[@aria-label=\"Save\"]//following-sibling::span)[1]");
	public static By MNOprof = By.xpath("//li[contains(text(),'MNO Profiles')]");
	public static By NewMNOprof = By.xpath("(//button[@aria-label=\"New MNO Profile\"]//following-sibling::span)[1]");
	public static By IMSI = By.xpath("//input[@aria-label=\"IMSI\"]");
	public static By SaveMnoprof = By.xpath("(//button[@aria-label=\"Save\"]//following-sibling::span)[1]");
	public static By MNOstatus = By.xpath("//div[@title=\"Activated\"]//child::label");
	public static By Car = By.xpath("//span[text()='Car']");
	
	String ExpectedMnoStat = "Activated";
	String ExpectedTCUtoCARlinkStat = "Assigned";
	String Expectedbrandvalue = "POLESTAR";
	String ExpectedMarkvalue = "United States";
	public static String pageName="Login page";
	public static By Vssm=By.xpath("//div[text()='VSSM']");
	private Session session;
	public  By TCULookup;
	public  By EIDSIM;

	public Sim_Volvo(LinkedHashMap<String, String> data, WebDriver driver, int count,Session ses) {
		super();
		this.driver = driver;
		this.data = data;
		this.session = ses;
	}

	public void simdata() throws Exception 

	{

		String EIDv = session.getTestData("ICCID");
		System.out.println("asaaaaaaaaaaa");
		System.out.println(EIDv);
		session.clickElement(SIMM);
		Thread.sleep(5000);
		session.clickElement(CreatenewSIM);
		
		Thread.sleep(5000);
		session.getScreenshotreports();
		session.selectValueByVisibleText(SIm_type, "SIM_type");
		session.sendKeys(ICCID, "ICCID");
		session.clearElement(ICCID);
		while(!session.getAttribute(ICCID, "value").isEmpty()) {
			session.sendKeys(ICCID, Keys.BACK_SPACE);
			session.sendKeys(ICCID, Keys.DELETE);
		}
		session.sendKeys(ICCID, Login_Volvo121212121.randomdigit(19), "19 digits random");
		String attribute = session.getAttribute(ICCID, "value");
		System.out.println(attribute);
		session.clickElement(SaveSIM);
		Thread.sleep(3000);
		session.escape();
		Thread.sleep(5000);
		session.clickElement(SIMM);
		By EID = By.xpath("//a[text()='"+attribute+"']");
		this.EIDSIM = By.xpath("//span[text()='"+attribute+"']");
		session.clickElement(EID);
		Thread.sleep(5000);
		session.clickElement(MNOprof);
		session.clickElement(NewMNOprof);
		session.sendKeys(IMSI,session.getTestData("IMSI")+Login_Volvo121212121.randomdigit(13),"13 digit random");
		session.clickElement(SaveMnoprof);
		Thread.sleep(5000);
		session.clickElement(SIMM);
		session.clickElement(EID);
		Thread.sleep(5000);
		session.clickElement(MNOprof);
		String ActualMnoStats = session.getText(MNOstatus);
		if (ExpectedMnoStat.equals(ActualMnoStats)) {
			System.out.println("Mno Sucessfully Linked to SIM");
			session.status(Status.PASS, "Mno Sucessfully Linked to SIM");
		}
		else {
			//				throw new Exception("MNO Link failed");
			session.status(Status.FATAL, "MNO Link failed");
		}
		
		












	}

}
