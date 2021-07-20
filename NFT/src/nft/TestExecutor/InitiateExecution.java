package nft.TestExecutor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nft.Report.ExecutionDetails;
import nft.Report.HTMLReportTemplate;
import nft.Report.TestResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class InitiateExecution {
	private WebDriver driver;
	private String currentTestScenarioStep;
	private LinkedHashMap<String, String> data;
	private int count;
	private int rowIndex;
	private String testCaseId;
	private LinkedHashMap<String, TestResult> testResults;
	private String resultFolderPath;
	private TestResult testResult;
	private long startTime = 0;
	private long endTime = 0;
	private ExecutionDetails executionDetails;
	public static ArrayList<String> TotalTC = new ArrayList<String>();
	public static ArrayList<String> statusPass = new ArrayList<String>();
	public static ArrayList<String> statusFail = new ArrayList<String>();

	private HashMap<String, String> storeVariables = new HashMap();
	protected HashMap<String, Integer> storeResults = new HashMap();
	private HashMap<String, WebDriver> storeDrivers = new HashMap();
	NFT_Engine nft;

	public InitiateExecution(int rowIndex, String testCaseId, ExecutionDetails executionDetails,
			LinkedHashMap<String, TestResult> testResults, String resultFolderPath, NFT_Engine nft) {
		this.rowIndex = rowIndex;
		this.testCaseId = testCaseId;
		this.testResults = testResults;
		this.resultFolderPath = resultFolderPath;
		this.executionDetails = executionDetails;
		this.nft = nft;

	}

	public void execute() throws Exception {

		NFT_Engine nft = new NFT_Engine();
		HTMLReportTemplate htmlReportTemplate = new HTMLReportTemplate();
		String screenShotPath;
		File file = new File(NFT_Executer.excelSheetPath);
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet;
		if (NFT_Executer.excelSheetName.equalsIgnoreCase("default")) {
			sheet = workbook.getSheetAt(0);
		} else {
			sheet = workbook.getSheet(NFT_Executer.excelSheetName);
		}

		data = getRowData(sheet, rowIndex);
		System.out.println("*********************************");
		System.out.println(data.get(Properties.testCaseName));
		setTestResult(new TestResult());
		setTestResult(testResults.get(testCaseId));
		getTestResult().setStatus("Running");
		
		System.out.println("sdsssssssssssdddd");
		System.out.println(getTestResult().gettcdesciption());
		testResults.put(testCaseId, getTestResult());
		// Thread.sleep(500);
		htmlReportTemplate.generateReport(executionDetails, testResults, resultFolderPath, nft);
	

		String testIdResultFolder = resultFolderPath + data.get(Properties.testCaseID) + "\\";
		new File(testIdResultFolder).mkdir();
		/*
		 * PrintStream out = new PrintStream( new
		 * FileOutputStream(testIdResultFolder+"consoleLog.txt", true), true);
		 * System.setOut(out);
		 */
		String testScenario = data.get(Properties.testScenario);

		// driver.get(Properties.DefaultURL);
		startTime = new Date().getTime();
		Session session = null;
		try {
			driver = new DriverFactory().getInstance(Properties.executionBrowserName);
			String[] testScenarioSteps = testScenario.split(";");
			System.out.println(testScenarioSteps.length);
			nft.startExtendTest(data.get("QC_ID"), "");
			LinkedHashMap<String, String> XbStatus = testResult.getXbridTC_IDStatus();
			nft.extent.flush();
			ApplicationMainClass amc = new ApplicationMainClass();
			for (int j = 0; j < testScenarioSteps.length; j++) {

				currentTestScenarioStep = testScenarioSteps[j];
				if (session != null) {
					testResult = session.getTestResult();
				}
				if (!currentTestScenarioStep.contains("Validation_")
						&& !currentTestScenarioStep.contains("validation_")) {

					for (int rc = j; rc < testScenarioSteps.length; rc++) {
						String currentstep = testScenarioSteps[rc];

						if (currentstep.contains("Validation_") || currentstep.contains("validation_")) {
							String[] tempXID = new String[1];
							String validationID = currentstep.substring(currentstep.lastIndexOf("_") + 1);
							if (validationID.contains(",")) {
								tempXID = new String[validationID.split(",").length];
								tempXID = validationID.split(",");
							} else {
								tempXID[0] = validationID;
							}

							for (int i = 0; i < tempXID.length; i++) {

								if (XbStatus.containsKey(tempXID[i])) {
									XbStatus.put(tempXID[i], "Running");

								}

							}
							testResult.setXbridTC_IDStatus(XbStatus);

							htmlReportTemplate.generateReport(executionDetails, testResults, resultFolderPath, nft);
							break;
						}

					}

				}

				count = iteratorcount(j, testScenario);
				try {

					if (!getTestResult().getStatus().equalsIgnoreCase("Fatal") && currentTestScenarioStep != null) {
						System.out.println(data.get("QC_ID"));

						try {
							nft.createExtendNode(currentTestScenarioStep, "");
							if (currentTestScenarioStep.contains(":")) {
								count = Integer.valueOf(currentTestScenarioStep.split(":")[1]);
								currentTestScenarioStep = currentTestScenarioStep.substring(0,
										currentTestScenarioStep.indexOf(":"));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("count - " + count);
						if (storeDrivers.isEmpty() || storeDrivers == null) {
							storeDrivers.put("parent", driver);
						}

						session = new Session(data, currentTestScenarioStep, storeDrivers, count, testResult,
								storeVariables, testIdResultFolder, storeResults, executionDetails, nft);

						if (currentTestScenarioStep.contains("Validation_")
								|| currentTestScenarioStep.contains("validation_")) {

							session.validate(currentTestScenarioStep);
							testResult = session.getTestResult();
						} else {
							

							boolean stepAvailavle = amc.MAGRCode(currentTestScenarioStep, data, driver, j, session);

							if (stepAvailavle) {
								testResult = session.getTestResult();
								storeVariables.putAll(session.storeVariables);
								storeResults.putAll(session.storeResults);
								storeDrivers.putAll(session.storeDrivers);
								/*
								 * String tempR = session.getStatusPF(); if (tempR != null &&
								 * tempR.equalsIgnoreCase("Fail")) { getTestResult().setStatus("Fail"); }
								 */

								screenShotPath = testIdResultFolder + "screenShot_"
										+ new SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date()) + ".png";
								File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
								FileUtils.copyFile(scrFile, new File(screenShotPath));
								getTestResult().setReportLink("file://" + screenShotPath);
								nft.logger.log(Status.INFO, "" + nft.logger.addScreenCaptureFromPath(screenShotPath));
								if (j == testScenarioSteps.length - 1) {
									/*
									 * screenShotPath = testIdResultFolder + "screenShot_" + new
									 * SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date()) + ".png"; File scrFile
									 * = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
									 * FileUtils.copyFile(scrFile, new File(screenShotPath));
									 */
									getTestResult().setReportLink("file://" + screenShotPath);
									if (getTestResult().getStatus().equalsIgnoreCase("Running")) {
										getTestResult().setStatus("Pass");
									}
									// NFT_Engine.getTest().log(Status.INFO, ""+
									// NFT_Engine.logger.addScreenCaptureFromPath(screenShotPath));

								}
								nft.extent.flush();
							} else {
								if (currentTestScenarioStep.contains("Validation_")
										|| currentTestScenarioStep.contains("validation_")) {

									session.validate(currentTestScenarioStep);
									testResult = session.getTestResult();
								}
								System.out.println(currentTestScenarioStep + " class Not available!");
								session.status("fatal", currentTestScenarioStep + " class Not available!");

								// throw new Exception();

							}
						}
					} else if (currentTestScenarioStep != null) {
						session.validate();
					}
				} catch (Exception e) {

					e.printStackTrace();
					// testResult = session.getTestResult();
					// session.status("fail",e.getLocalizedMessage());
					session.status("fatal", e.getLocalizedMessage());
					// getTestResult().setStatus("fatal");

					/*
					 * screenShotPath = testIdResultFolder + "screenShot_" + new
					 * SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date()) + ".png"; File scrFile
					 * = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					 * FileUtils.copyFile(scrFile, new File(screenShotPath));
					 * getTestResult().setReportLink("file://" + screenShotPath);
					 * getTestResult().setFailureReason(e.getLocalizedMessage());
					 * nft.logger.log(Status.FATAL, e.getLocalizedMessage() +
					 * nft.logger.addScreenCaptureFromPath(screenShotPath)); nft.extent.flush();
					 */
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

			LinkedHashMap<String, String> finalStatusCheck = testResult.getXbridTC_IDStatus();
			boolean overallStatus = true;
			for (Entry<String, String> e1 : finalStatusCheck.entrySet()) {
				String Xid = e1.getKey();
				String status = e1.getValue();

				if (status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Running")) {
					status = "Fail";
					overallStatus = false;
				} else if (status.equalsIgnoreCase("Fail") || status.equalsIgnoreCase("Fatal")) {
					overallStatus = false;
				}
				finalStatusCheck.put(Xid, status);
			}
			testResult.setXbridTC_IDStatus(finalStatusCheck);
			if (finalStatusCheck != null && !finalStatusCheck.isEmpty()) {
				if (overallStatus) {
					testResult.setStatus("Pass");
				} else {
					testResult.setStatus("Fail");
				}
			}
			endTime = new Date().getTime();
			TotalTC.add(data.get("QC_ID"));

			if (getTestResult().getStatus().equalsIgnoreCase("Pass")) {
				statusPass.add(data.get("QC_ID"));
			} else {
				statusFail.add(data.get("QC_ID"));
			}
			long diffMs = endTime - startTime;
			long diffSec = diffMs / 1000;
			long min = diffSec / 60;
			long sec = diffSec % 60;
			String diffTime = min + " min and " + sec + " sec.";
			getTestResult().setDuration(diffTime);// Duration

			workbook.close();
			fis.close();

			testResults.put(testCaseId, getTestResult());
			htmlReportTemplate.generateReport(executionDetails, testResults, resultFolderPath, nft);
			System.out.flush();
			nft.extent.flush();
			if (session != null) {
				session.quitDrivers();
			}
			driver.quit();
		}
		workbook.close();
		fis.close();
		System.out.flush();
		nft.extent.flush();
	}

	private int iteratorcount(int j, String Scenario) {
		String[] steps = Scenario.split(";");
		int count = 0;
		if (j != 0) {
			for (int k = j - 1; k >= 0; k--) {
				if (steps[j].equals(steps[k])) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * This method for getting row data based on row index and returns it as a
	 * LinkedHashMap.
	 */
	public LinkedHashMap<String, String> getRowData(XSSFSheet sheet, int rowIndex) throws Exception {
		LinkedHashMap<String, String> rowData = new LinkedHashMap<>();
		for (int i = 0; i <= sheet.getRow(0).getLastCellNum(); i++) {
			String headrerCellvalue;
			String cellValue;
			headrerCellvalue = getCellValueAsString(sheet.getRow(0).getCell(i));
			cellValue = getCellValueAsString(sheet.getRow(rowIndex).getCell(i));
			rowData.put(headrerCellvalue, cellValue);
		}
		return rowData;
	}

	/**
	 * This method for the type of data in the cell, extracts the data and returns
	 * it as a string.
	 */
	public String getCellValueAsString(Cell cell) {
		String strCellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				strCellValue = cell.toString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					strCellValue = dateFormat.format(cell.getDateCellValue());
				} else {
					Double value = cell.getNumericCellValue();
					Long longValue = value.longValue();
					strCellValue = new String(longValue.toString());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				strCellValue = new String(new Boolean(cell.getBooleanCellValue()).toString());
				break;
			case Cell.CELL_TYPE_BLANK:
				strCellValue = "";
				break;
			}
		}
		return strCellValue;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public ArrayList<String> getXbrid_ID() {

		return new ArrayList();
	}

}