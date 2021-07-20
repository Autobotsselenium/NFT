package nft.TestExecutor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
	private WebDriver driver = null;
	public static boolean UNFT = false;

	public WebDriver getInstance(String BrowserName) throws Exception {
		if (BrowserName.equalsIgnoreCase("IE") || BrowserName.equalsIgnoreCase("InternetExplorer")
				|| BrowserName.equalsIgnoreCase("Internet Explorer")) {
			System.setProperty("webdriver.ie.driver", Properties.ieDriverPath);
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			capabilities.setCapability(InternetExplorerDriver.SILENT, true);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			InternetExplorerOptions options = new InternetExplorerOptions(capabilities);
			driver = new InternetExplorerDriver(options);
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			setTimeouts();
		} else if (BrowserName.equalsIgnoreCase("Chrome") || BrowserName.equalsIgnoreCase("GoogleChrome")
				|| BrowserName.equalsIgnoreCase("Google Chrome")) {
			System.setProperty("webdriver.chrome.driver", Properties.chromeDriverPath);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("chrome.switches",
					Arrays.asList("--start-maximized", "--disable-extensions", "--incognito"));
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--test-type");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(capabilities);
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			setTimeouts();
		}else if (BrowserName.equalsIgnoreCase("ChromeHeadless")) {
			System.setProperty("webdriver.chrome.driver", Properties.chromeDriverPath);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("chrome.switches",
			Arrays.asList("--start-maximized", "--disable-extensions", "--incognito"));
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--test-type");
			options.addArguments("headless");
			options.addArguments("window-size=1200x600");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(capabilities);
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			setTimeouts();
		}
		 
		else if (BrowserName.equalsIgnoreCase("FireFox")) {
			System.setProperty("webdriver.gecko.driver", Properties.geckoDriverPath);
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			if (Properties.firefoxBinaryPath.trim().length() == 0)
				Properties.firefoxBinaryPath = "C:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe";
			File binaryPathFile = new File(Properties.firefoxBinaryPath);
			if (binaryPathFile.exists()) {
				FirefoxBinary ffBinary = new FirefoxBinary(binaryPathFile);
				ProfilesIni profileIni = new ProfilesIni();
				FirefoxProfile profile = profileIni.getProfile("default");
				profile.setAcceptUntrustedCertificates(true);
				profile.setAssumeUntrustedCertificateIssuer(false);
				FirefoxOptions options = new FirefoxOptions(capabilities);
				options.setBinary(ffBinary);
				driver = new FirefoxDriver(options);
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();
				setTimeouts();
			} else {
				System.out.println("Firefox is not installed in this machine or the binary path is wrong ");
				throw new Exception("Firefox is not installed in this machine or the binary path is wrong ");
			}
		} else if (BrowserName.equalsIgnoreCase("CustomFirefox")) {

			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			if (Properties.firefoxBinaryPath.trim().length() == 0)
				Properties.firefoxBinaryPath = "C:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe";
			File binaryPathFile = new File(Properties.firefoxBinaryPath);
			if (binaryPathFile.exists()) {
				FirefoxBinary ffBinary = new FirefoxBinary(binaryPathFile);
				ProfilesIni profileIni = new ProfilesIni();
				FirefoxProfile profile = profileIni.getProfile("default");
				profile.setAcceptUntrustedCertificates(true);
				profile.setAssumeUntrustedCertificateIssuer(false);
				FirefoxOptions options = new FirefoxOptions(capabilities);
				options.setBinary(ffBinary);
				System.setProperty("webdriver.gecko.driver", Properties.geckoDriverPath);
				 
				options.addPreference("browser.download.folderList", 2);
				options.addPreference("browser.download.dir", Properties.downloadFilepath); // Here please provide the
																							// results folder path which was
																							// created as part of execution.
				options.addPreference("browser.download.useDownloadDir", true);
				options.addPreference("browser.helperApps.neverAsk.openFile", "text/csv,application/csv,application/text");
				options.addPreference("browser.helperApps.neverAsk.saveToDisk",
						"application/pdf,text/csv,application/csv,application/text,application/vnd.ms-excel");
				options.addPreference("browser.download.manager.useWindow", false);

				options.addPreference("pdfjs.disabled", true); // disable the built-in PDF
				driver = new FirefoxDriver(options);
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();
				setTimeouts();
			} else {
				System.out.println("Firefox is not installed in this machine or the binary path is wrong ");
				throw new Exception("Firefox is not installed in this machine or the binary path is wrong ");
			}

			

		} else if (BrowserName.equalsIgnoreCase("MozillaFirefox") || BrowserName.equalsIgnoreCase("Mozilla Firefox")) {

			driver = new FirefoxDriver();

		} else if (BrowserName.equalsIgnoreCase("Edge")) {
			System.setProperty("webdriver.edge.driver", Properties.edgeDriverPath);
			driver = new EdgeDriver();
		} else if (BrowserName.equalsIgnoreCase("Remote")) {
			URL url = null;
			try {
				url = new URL("http://" + Properties.ipAddress + ":" + Properties.portNumber + "/wd/hub");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			DesiredCapabilities capabilities = new DesiredCapabilities();
			if (BrowserName.equalsIgnoreCase("FireFox") || BrowserName.equalsIgnoreCase("MozillaFirefox")
					|| BrowserName.equalsIgnoreCase("Mozilla Firefox")) {
				capabilities = DesiredCapabilities.firefox();
				capabilities = setAdditionalCapabilities(capabilities);

			} else if (BrowserName.equalsIgnoreCase("IE") || BrowserName.equalsIgnoreCase("InternetExplorer")
					|| BrowserName.equalsIgnoreCase("Internet Explorer")) {
				capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				capabilities = setAdditionalCapabilities(capabilities);
			} else if (BrowserName.equalsIgnoreCase("Chrome") || BrowserName.equalsIgnoreCase("GoogleChrome")
					|| BrowserName.equalsIgnoreCase("Google Chrome")) {
				capabilities = DesiredCapabilities.chrome();
				capabilities = setAdditionalCapabilities(capabilities);
			}
			capabilities.setPlatform(Platform.WINDOWS);
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			try {

				driver = new RemoteWebDriver(url, capabilities);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					capabilities = DesiredCapabilities.chrome();
					driver = new RemoteWebDriver(url, capabilities);
				} catch (Exception ex) {
					ex.printStackTrace();
					try {
						capabilities = DesiredCapabilities.firefox();
						if (url != null) {
							driver = new RemoteWebDriver(url, capabilities);
						} else {
							driver = new FirefoxDriver();
						}
					} catch (Exception ex1) {
						ex1.printStackTrace();
					}
				}
			}
			setTimeouts();
			return driver;
		} else
			throw new Exception("Kindly mention the valid Browser Name");
		return driver;
	}

	private DesiredCapabilities setAdditionalCapabilities(DesiredCapabilities capabilities) {
		String args[] = Properties.additionalCapabilities.split(";");
		for (String arg : args) {
			if (arg.length() > 0) {
				String[] keyValue = arg.split(":");
				capabilities.setCapability(keyValue[0], keyValue[1]);
			}
		}
		return capabilities;
	}

	private void setTimeouts() {
		driver.manage().timeouts().implicitlyWait(Properties.implicitWait, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(Properties.scriptTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(Properties.pageLoadTimeout, TimeUnit.SECONDS);
	}
}