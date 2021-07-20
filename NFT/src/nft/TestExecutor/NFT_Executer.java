package nft.TestExecutor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.graphbuilder.struc.LinkedList;

import nft.Report.ExecutionDetails;
import nft.Report.HTMLReportTemplate;
import nft.Report.TestResult;

public class NFT_Executer {

	private LinkedHashMap<String, TestResult> testResults;
	private TestResult testResult;
	private ExecutionDetails executionDetails;
	private LinkedHashSet<Object[]> dataProviderData;
	public String resultFolderPath;
	public static String excelSheetPath;
	public static String excelSheetName;
	public int runColumnIndex;
	public int testScenarioStepsColumnIndex;

	ArrayList<String> testCases = new ArrayList();
	HashMap<String, ArrayList<String>> NFT_Xbrid_Mapping = new HashMap();
	public static HashMap<String, String> XbridValidationPoints = new HashMap();
	ArrayList<String> xBridTestID = new ArrayList();

	public NFT_Executer() throws Exception {

		for (int i = 0; i < Properties.excelSheetPath.length; i++) {
			dataProviderData = null;
			executionDetails = new ExecutionDetails();
			testResult = new TestResult();
			testResults = new LinkedHashMap<>();
			resultFolderPath = null;
			excelSheetPath = null;
			excelSheetName = null;
			XbridValidationPoints = new HashMap();
			testCases= new ArrayList();

			dataProviderData = getData(i);
			getXbridData(i);

			if (dataProviderData != null && !dataProviderData.isEmpty()) {
				NFT_Engine nft = new NFT_Engine();
				executionDetails = getExecutionDetails();
				resultFolderPath = genearteResultFolder();
				String resultHtmlFilePath = generateReport(resultFolderPath, nft);

				Desktop desktop = Desktop.getDesktop();
				File file = new File(resultHtmlFilePath);
				if (file.exists())
					desktop.open(file);

				nft.run(dataProviderData, executionDetails, testResults, resultFolderPath, nft);
			} else {
				System.err.println("No data found in Excel -->" + Properties.excelSheetPath[i]);
			}
		}
	}

	private String generateReport(String resultFolderPath, NFT_Engine nft) throws Exception {
		HTMLReportTemplate htmlReportTemplate = new HTMLReportTemplate();
		return htmlReportTemplate.generateReport(executionDetails, testResults, resultFolderPath, nft);
	}

	private String genearteResultFolder() {
		String resultFolderName;
		String resultFolderPath;
		try {
			resultFolderName = "Result_" + new SimpleDateFormat("ddMMMyyyyHHmmss").format(new Date());
			if (Properties.resultsPath.endsWith("\\") || Properties.resultsPath.endsWith("/"))
				resultFolderPath = Properties.resultsPath + resultFolderName + "\\";
			else
				resultFolderPath = Properties.resultsPath + "\\" + resultFolderName + "\\";

			if (resultFolderPath.startsWith(".")) {
				resultFolderPath = resultFolderPath.replaceFirst(".", "");
				// System.out.println("-->"+System.getProperty("user.dir"));
				resultFolderPath = System.getProperty("user.dir") + resultFolderPath;

			}
			new File(resultFolderPath).mkdir();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultFolderPath;
	}

	private void getXbridData(int excelCount) throws Exception {

		for (int i = 0; i < testCases.size(); i++) {
			testResult = new TestResult();
			testResult = testResults.get(testCases.get(i));
			String[] temp = testResult.getTestScenarioSteps().split(";");
			for (String s : temp) {
				if (s.contains("Validation_") || s.contains("validation_")) {
					ArrayList<String> XIdList = new ArrayList<String>();
					s = s.substring(s.lastIndexOf("_") + 1);
					if (s.contains(",")) {
						String[] temp1 = s.split(",");

						for (String ms : temp1) {
							if (NFT_Xbrid_Mapping.get(testResult.getTestCaseID()) != null) {
								XIdList = NFT_Xbrid_Mapping.get(testResult.getTestCaseID());
							}
							XIdList.add(ms.trim());
							xBridTestID.add(ms.trim());
							NFT_Xbrid_Mapping.put(testResult.getTestCaseID(), XIdList);
						}

					} else {

						if (NFT_Xbrid_Mapping.get(testResult.getTestCaseID()) != null) {
							XIdList = NFT_Xbrid_Mapping.get(testResult.getTestCaseID());
						}
						XIdList.add(s.trim());
						xBridTestID.add(s.trim());
						NFT_Xbrid_Mapping.put(testResult.getTestCaseID(), XIdList);
					}

				}
			}
			LinkedHashMap<String, String> xbridTC_IDStatus = new LinkedHashMap();

			if (NFT_Xbrid_Mapping.get(testCases.get(i)) != null) {
				for (String getXBID : NFT_Xbrid_Mapping.get(testCases.get(i))) {
					xbridTC_IDStatus.put(getXBID, "Pending");
				}
				testResult.setXbridTC_ID(NFT_Xbrid_Mapping.get(testCases.get(i)));
				testResult.setXbridTC_IDStatus(xbridTC_IDStatus);
				testResults.put(testCases.get(i), testResult);

			}
		}

		System.out.println(NFT_Xbrid_Mapping.keySet());
		System.out.println(NFT_Xbrid_Mapping.values());

		// excelSheetPath = Properties.excelSheetPath[excelCount];
		File file = new File(excelSheetPath);
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		try {
			XSSFSheet sheet = workbook.getSheet("ValidationPoint");

			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

				try {

					String XtestID = getCellValueAsString(sheet.getRow(i).getCell(0)).trim();
					String XvalidationPoints = getCellValueAsString(sheet.getRow(i).getCell(1));
					if (XtestID != null && XvalidationPoints != null && XtestID != "" && XvalidationPoints != "") {
						
						if (xBridTestID.contains(XtestID)) {
							XbridValidationPoints.put(XtestID, XvalidationPoints);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {

			System.err.println("***Warning***\n ValidationPoint sheet un-available");

		}

		workbook.close();
		fis.close();

	}

	private LinkedHashSet<Object[]> getData(int excelCount) throws Exception {
		LinkedHashSet<Object[]> testScenarios = new LinkedHashSet<>();

		excelSheetPath = Properties.excelSheetPath[excelCount];
		File file = new File(excelSheetPath);
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet;
		try {
			excelSheetName = Properties.sheetName[excelCount].trim();
			if (excelSheetName == null || excelSheetName.isEmpty() || excelSheetName.equalsIgnoreCase("default")) {
				excelSheetName = "default";
				sheet = workbook.getSheetAt(0);
			} else {
				sheet = workbook.getSheet(excelSheetName);
			}
		} catch (Exception e) {
			excelSheetName = "default";
			sheet = workbook.getSheetAt(0);
		}

		// System.out.println(sheet.getPhysicalNumberOfRows());
		// loading of test case id's having Run flag as Yes
		/*
		 * try { runColumnIndex=Properties.runFlagColumnIndex; }catch (Exception e) { //
		 * TODO: handle exception }
		 */
		try {
			// if(Properties.runFlagColumnIndex!=null)

			// System.err.println("\nAuto locating RUN column Index");

			for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
				// System.out.println(getCellValueAsString(sheet.getRow(0).getCell(i)));
				String temp = getCellValueAsString(sheet.getRow(0).getCell(i)).trim();
				if (temp.equalsIgnoreCase("Run")) {
					System.err.println("\nRUN column Index --> " + i + " located in Sheet -->'" + sheet.getSheetName()
							+ "' in Excel -->'" + Properties.excelSheetPath[excelCount] + "'");
					runColumnIndex = i;

				}

				if (temp.equalsIgnoreCase(Properties.testScenario)) {
					System.err.println("TestScenario column Index --> " + i + " located in Sheet -->'"
							+ sheet.getSheetName() + "' in Excel -->'" + Properties.excelSheetPath[excelCount] + "'");
					testScenarioStepsColumnIndex = i;

				}
				if (runColumnIndex != 0 && testScenarioStepsColumnIndex != 0) {
					break;
				}
			}

			if (Properties.implicitWait > 10) {
				System.err.println(
						"For improved performance reduce the implicit wait to lesser than or equal to 10 seconds");
			}
		} catch (Exception e) {

			e.printStackTrace();
			/*
			 * System.err.println("Error occcured while locating RUN column in Sheet -->" +
			 * sheet.getSheetName() + " in Excel -->" +
			 * Properties.excelSheetPath[excelCount]);
			 */
		}

		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

			try {
				XSSFCell runFlagCell = sheet.getRow(i).getCell(runColumnIndex);
				if (runFlagCell != null) {
					String runFlag = runFlagCell.toString();

					if (runFlag.equalsIgnoreCase("Yes") || runFlag.equalsIgnoreCase("Y")) {
						testResult = new TestResult();
						String testID = getCellValueAsString(sheet.getRow(i).getCell(Properties.testCaseIdColumnIndex));
						String testCaseName = getCellValueAsString(
								sheet.getRow(i).getCell(Properties.testCaseNameColumnIndex));
						String testCaseDesc = getCellValueAsString(
								sheet.getRow(i).getCell(Properties.testCaseNameColumnIndex));
						String testScenarioSteps = getCellValueAsString(
								sheet.getRow(i).getCell(testScenarioStepsColumnIndex));
						int rowIndex = i;
						Object[] testScenario = new Object[2];
						testScenario[0] = rowIndex;
						testScenario[1] = testID;
						testCases.add(testID);
						testScenarios.add(testScenario);
						testResult.setTestCaseID(testID);
						testResult.setTestScenarioName(testCaseName);
//						testResult.settcdesciption(testCaseDesc);
						testResult.setTestScenarioSteps(testScenarioSteps);
						testResult.setStatus("Pending");
						testResult.setDuration("NA");
						testResult.setReportLink("#");
						testResults.put(testID, testResult);
					}
				}
			} catch (Exception e) {

			}
		}
		workbook.close();
		fis.close();
		return testScenarios;
	}

	private ExecutionDetails getExecutionDetails() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss a");
		String startTime = dateFormat.format(new Date());
		executionDetails.setStartDate(new Date());
		executionDetails.setStartTime(startTime);
		executionDetails.setOperatingSystem(System.getProperty("os.name"));
		executionDetails.setJavaVersion(System.getProperty("java.version"));
		executionDetails.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		executionDetails.setHostName(InetAddress.getLocalHost().getHostName());
		executionDetails.setUserName(System.getProperty("user.name"));
		executionDetails.setBrowserName(Properties.executionBrowserName);
		executionDetails.setStartTime(startTime);
		executionDetails.setEndtime("Execution is in progress");
		executionDetails.setTimeTaken("Execution is in progress");
		executionDetails.setThreadCount(Properties.threadPoolCount);
		// executionDetails.setThreadCount(1);
		executionDetails.setAppURL(Properties.defaultUrl);
		return executionDetails;
	}

	/**
	 * This method for the type of data in the cell, extracts the data and returns
	 * it as a string.
	 */
	public static String getCellValueAsString(Cell cell) {
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

}
