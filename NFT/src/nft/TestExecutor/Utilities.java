package nft.TestExecutor;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Utilities {
	public static void JavaScriptClick(WebDriver driver,By by)
	{
		WebElement webElement=null;
		webElement=driver.findElement(by);
		JavascriptExecutor executor=(JavascriptExecutor)driver;
		try{
			executor.executeScript("arguments[0].click();", webElement);
		}catch (Exception e) {
			try{
				executor.executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[0].dispatchEvent(evt);", webElement);
			}catch (Exception ex) {
			}
		}
	}

	public static void JavaScriptClick(WebDriver driver,WebElement element)
	{
		JavascriptExecutor executor=(JavascriptExecutor)driver;
		try{
			executor.executeScript("arguments[0].click();", element);
		}catch (Exception e) {
			try{
				executor.executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[0].dispatchEvent(evt);", element);
			}catch (Exception ex) {
			}
		}
	}

	public static void highLight(WebDriver driver,By by) throws Exception
	{
		WebElement webElement=null;
		webElement =driver.findElement(by);
		JavascriptExecutor js=(JavascriptExecutor)driver; 
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", webElement);
		Thread.sleep(5000);
		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", webElement); 
	}

	public static void highLight(WebDriver driver,WebElement element) throws Exception
	{
		JavascriptExecutor js=(JavascriptExecutor)driver; 
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
		Thread.sleep(5000);
		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element); 
	}
	
	public static void captureScreenShot(WebDriver driver,String screenShotDestinationPath) throws Exception {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(screenShotDestinationPath));
	}
}
