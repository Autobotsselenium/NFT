package com.VolvoCars;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import nft.TestExecutor.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Restfin
{
	private WebDriver driver;
	public static int iStepNumber;
	private LinkedHashMap<String, String> data;
	public static String pageName="Login page";
	private Session session;
	By TCULookup;

	public Restfin(LinkedHashMap<String, String> data, WebDriver driver, int count,Session ses) {
		super();
		this.driver = driver;
		this.data = data;
		this.session = ses;
	}
	int Expectedcode = 204;
	

	public void Post_sims(Sim_Volvo f) throws Exception 
	{
		String url = session.getTestData("url");
	           
		
		OkHttpClient client = new OkHttpClient().newBuilder()
				  .build();
				MediaType mediaType = MediaType.parse("application/json;api-version=1.0");
				RequestBody body = RequestBody.create(mediaType, session.rand("./test-output/data1.json"));
				Request request = new Request.Builder()
				  .url("https://api-gw-qa.vssm.volvocars.biz/simservice/api/sims")
				  .method("POST", body)
				  .addHeader("X-Request-ID", "20190410001")
				  .addHeader("Date", "18 APR 2019 12:28:07")
				  .addHeader("Vssm-api-key", "8a59a0b7e042491ba09aff88b32f2b8f")
				  .addHeader("Content-Type", "application/json;api-version=1.0")
				  .build();
				Response response = client.newCall(request).execute();
				 ResponseBody body1 = response.body();

				 System.out.println("Response Body is: " + body1.toString());
				
				 String jsonData = response.body().string();
					int Actualcode = response.code();
					System.out.println(Actualcode);
					
					if (Expectedcode == Actualcode) {
						System.out.println("Post request is sucessful");
						session.status(Status.PASS, "Post request is sucessful");
					}
					else {
						System.out.println(jsonData); JSONObject Jobject = new JSONObject(jsonData);
						 System.out.println(); System.out.println(Jobject);
					}
					
					 
					
	}
}
