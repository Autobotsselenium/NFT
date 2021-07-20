package nft.Report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nft.TestExecutor.NFT_Engine;
import nft.TestExecutor.NFT_Executer;

public class HTMLReportTemplate {

	public String generateReport(ExecutionDetails executionDetails, LinkedHashMap<String, TestResult> testResults,
			String resultFolderPath, NFT_Engine nft) throws Exception {
		String operatingSystem = executionDetails.getOperatingSystem();
		String javaVersion = executionDetails.getJavaVersion();
		String ipAddress = executionDetails.getIpAddress();
		String hostName = executionDetails.getHostName();
		String userName = executionDetails.getUserName();
		String browserName = executionDetails.getBrowserName();
		String startTime = executionDetails.getStartTime();
		String endtime = executionDetails.getEndtime();
		String timeTaken = executionDetails.getTimeTaken();
		String reprotHtmlText = getReportTable(testResults);
		String threadPoolCount = String.valueOf(executionDetails.getThreadCount());

		int passCount = 0;
		int failCount = 0;
		int pendingCount = 0;
		int runningCount = 0;
		int totalCount = testResults.size();

		int xPassCount = 0;
		int xFailCount = 0;
		int xPendingCount = 0;
		int xRunningCount = 0;
		int xTotalCount = 0;
		for (Entry<String, TestResult> entry : testResults.entrySet()) {

			TestResult tR;
			tR = entry.getValue();

			LinkedHashMap<String, String> finalStatusCheck = tR.getXbridTC_IDStatus();

			if (finalStatusCheck != null &&!finalStatusCheck.isEmpty()) {
				for (Entry<String, String> e1 : finalStatusCheck.entrySet()) {

					String status = e1.getValue();

					switch (status) {
					case "Pass":
						xPassCount++;
						break;
					case "Fail":
						xFailCount++;
						break;
					case "Running":
						xRunningCount++;
						break;
					case "Pending":
						xPendingCount++;
						break;
					default:
						break;

					}
					xTotalCount++;
				}
			} else {
				String status = tR.getStatus();

				switch (status) {
				case "Pass":
					xPassCount++;
					break;
				case "Fail":
					xFailCount++;
					break;
				case "Running":
					xRunningCount++;
					break;
				case "Pending":
					xPendingCount++;
					break;
				default:
					break;

				}
				xTotalCount++;
			}

		}

		for (String testID : testResults.keySet()) {
			TestResult testResult = testResults.get(testID);
			if (testResult.getStatus().equalsIgnoreCase("Fatal")) {
				testResult.setStatus("Fail");
			}
			switch (testResult.getStatus()) {
			case "Pass":
				passCount++;
				break;
			case "Fail":
				failCount++;
				break;
			case "Running":
				runningCount++;
				break;
			case "Pending":
				pendingCount++;
				break;
			default:
				break;
			}
		}

		// testResults.keySet().stream().forEach(System.out::print);
		String s = "";
		String ref = "";

		if ((passCount + failCount) == totalCount) {
			ref = "<html><head>\r\n";
		} else {
			ref = "<html><head><meta http-equiv=\"refresh\" content=\"30\" >\r\n";

		}
		if (nft.ExtendReportPath != null) {
			String reportPath = "file://" + nft.ExtendReportPath;

			s = ref + "<title>Report</title><script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script type='text/javascript'>google.charts.load('current', {'packages':['corechart']});google.charts.setOnLoadCallback(drawChart);function drawChart() {  var data = google.visualization.arrayToDataTable([  ['TestResults', 'TestCases'],  ['Running', "
					+ xRunningCount + "],  ['Fail', " + xFailCount + "],  ['Pending', " + xPendingCount
					+ "],  ['Pass', " + xPassCount
					+ "]]);var options = {'width':420,'height':250};var chart = new google.visualization.PieChart(document.getElementById('piechart'));chart.draw(data, options);}</script><style>table {    font-family: arial, sans-serif;    border-collapse: collapse; table-layout:fixed; overflow: auto;   width:100%;}td, th {    border: 1px solid #dddddd;    text-align: left;    padding: 3px;}</style></head><body><div align='center'><font style='font-weight:bold;font-size:50;color:#5c05ad;'>Test</font><font style='font-weight:bold;font-size:50;color:#8a868e;'>Results</font><br/><br/></div><div style='width:99%; height:300px;  color: navy; background-color: white; border: 2px solid black; padding: 5px;display:block; align:center;'><div style='width:30%; height:97%;overflow: auto;word-wrap:break-word; color: navy; background-color: white; border: 1px solid black; padding: 5px;margin-left:11%;float:left;display:block;'><table>  <tr>    <th colspan=2 style='text-align:center;background-color:#5c05ad;color:#d6d4d8;'>Execution Details</th>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Operating System</td>    <td>"
					+ operatingSystem
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Java Version</td>    <td>"
					+ javaVersion
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>IP Address</td>    <td>"
					+ ipAddress
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Host Name</td>    <td>"
					+ hostName
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>User Name</td>    <td>"
					+ userName
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>ExtendReport Path</td>    <td><a href='"
					+ reportPath + "'>" + NFT_Engine.ExtendReportPath
					+ "</a></td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Browser Name</td>    <td>"
					+ browserName
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Start Time</td>    <td>"
					+ startTime
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>End Time</td>    <td>"
					+ endtime
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Time Taken</td>    <td>"
					+ timeTaken
					+ "</td>  </tr><tr>    <td class='PropName' style='font-weight: bold;'>Thread count</td>    <td>"
					+ threadPoolCount
					+ "</td>  </tr></table></div><div id='piechart' style='width:40%; height:100%; color: navy; background-color: white; border: 1px solid black; margin-left:5px;padding: 0px 2px 0px 0px;float:left;display:block;'\\></div><div style='width:15%; height:100%; color: navy; background-color: white; border: 1px solid black; margin-left:5px;padding: 0px 0px 0px 0px;float:left;display:block;overflow:auto;'><table>"
					/*
					 * +"<tr> <th colspan=2
					 * style='text-align:center;background-color:#5c05ad;color:#d6d4d8;'>NFT Count
					 * Details</th> </tr> <tr> <td class='PropName' style='font-weight:
					 * bold;'>Total</td> <td>" + totalCount + "</td> </tr> <tr> <td class='PropName'
					 * style='font-weight: bold;'>Pass</td> <td>" + passCount + "</td> </tr> <tr>
					 * <td class='PropName' style='font-weight: bold;'>Fail</td> <td>" + failCount +
					 * "</td> </tr> <tr> <td class='PropName' style='font-weight:bold;'>Running</td>
					 * <td>" + runningCount + "</td> </tr> <tr> <td class='PropName'
					 * style='font-weight: bold;'>Pending</td> <td>" + pendingCount + "</td></tr>"
					 */
					+ "<tr>    <th colspan=2 style='text-align:center;background-color:#5c05ad;color:#d6d4d8;'> Count Details </th>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Total</td>    <td>"
					+ xTotalCount
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Pass</td>    <td>"
					+ xPassCount
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Fail</td>    <td>"
					+ xFailCount
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight:bold;'>Running</td>    <td>"
					+ xRunningCount
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Pending</td>    <td>"
					+ xPendingCount + "</td></tr>"

					+ "</table></div></div><div style='width:99%; height:50%; color: navy; background-color: white; border: 2px solid black; padding: 5px;display:block;overflow:scroll;'>"
					+ reprotHtmlText + "</div></body></html>";
		} else {
			s = ref + "<title>Report</title><script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><script type='text/javascript'>google.charts.load('current', {'packages':['corechart']});google.charts.setOnLoadCallback(drawChart);function drawChart() {  var data = google.visualization.arrayToDataTable([  ['TestResults', 'TestCases'],  ['Running', "
					+ xRunningCount + "],  ['Fail', " + xFailCount + "],  ['Pending', " + xPendingCount
					+ "],  ['Pass', " + xPassCount
					+ "]]);var options = {'width':420,'height':250};var chart = new google.visualization.PieChart(document.getElementById('piechart'));chart.draw(data, options);}</script><style>table {    font-family: arial, sans-serif;    border-collapse: collapse; table-layout:fixed; overflow: auto;   width:100%;}td, th {    border: 1px solid #dddddd;    text-align: left;    padding: 3px;}</style></head><body><div align='center'><font style='font-weight:bold;font-size:50;color:#5c05ad;'>Test</font><font style='font-weight:bold;font-size:50;color:#8a868e;'>Results</font><br/><br/></div><div style='width:99%; height:320px;  color: navy; background-color: white; border: 2px solid black; padding: 5px;display:block; align:center;'><div style='width:30%; height:97%;overflow: auto;word-wrap:break-word; color: navy; background-color: white; border: 1px solid black; padding: 5px;margin-left:11%;float:left;display:block;'><table>  <tr>    <th colspan=2 style='text-align:center;background-color:#5c05ad;color:#d6d4d8;'>Execution Details</th>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Operating System</td>    <td>"
					+ operatingSystem
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Java Version</td>    <td>"
					+ javaVersion
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>IP Address</td>    <td>"
					+ ipAddress
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Host Name</td>    <td>"
					+ hostName
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>User Name</td>    <td>"
					+ userName
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Report Path</td><td>N/A</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Browser Name</td>    <td>"
					+ browserName
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Start Time</td>    <td>"
					+ startTime
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>End Time</td>    <td>"
					+ endtime
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Time Taken</td>    <td>"
					+ timeTaken
					+ "</td>  </tr><tr>    <td class='PropName' style='font-weight: bold;'>Thread count</td>    <td>"
					+ threadPoolCount
					+ "</td>  </tr></table></div><div id='piechart' style='width:40%; height:100%; color: navy; background-color: white; border: 1px solid black; margin-left:5px;padding: 0px 2px 0px 0px;float:left;display:block;overflow:auto;'\\></div><div style='width:15%; height:100%; color: navy; background-color: white; border: 1px solid black; margin-left:5px;padding: 0px 0px 0px 0px;float:left;display:block;overflow:auto;'><table> "
					/*
					 * +"<tr> <th colspan=2
					 * style='text-align:center;background-color:#5c05ad;color:#d6d4d8;'>NFT Count
					 * Details</th> </tr> <tr> <td class='PropName' style='font-weight:
					 * bold;'>Total</td> <td>" + totalCount + "</td> </tr> <tr> <td class='PropName'
					 * style='font-weight: bold;'>Pass</td> <td>" + passCount + "</td> </tr> <tr>
					 * <td class='PropName' style='font-weight: bold;'>Fail</td> <td>" + failCount +
					 * "</td> </tr> <tr> <td class='PropName' style='font-weight:bold;'>Running</td>
					 * <td>" + runningCount + "</td> </tr> <tr> <td class='PropName'
					 * style='font-weight: bold;'>Pending</td> <td>" + pendingCount + "</td></tr>"
					 */

					+ "<tr>    <th colspan=2 style='text-align:center;background-color:#5c05ad;color:#d6d4d8;'> Count Details </th>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Total</td>    <td>"
					+ xTotalCount
					+ "</td>  </tr>  <tr>    <td class='PropName' style='font-weight: bold;'>Pass</td>    <td>"
					+ xPassCount
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Fail</td>    <td>"
					+ xFailCount
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight:bold;'>Running</td>    <td>"
					+ xRunningCount
					+ "</td>  </tr>   <tr>    <td class='PropName' style='font-weight: bold;'>Pending</td>    <td>"
					+ xPendingCount + "</td></tr>"

					+ "</table></div></div><div style='width:99%; height:50%; color: navy; background-color: white; border: 2px solid black; padding: 5px;display:block;overflow:scroll;'>"
					+ reprotHtmlText + "</div></body></html>";
		}
		File resultFolder = new File(resultFolderPath);
		if (!resultFolder.exists())
			resultFolder.mkdirs();
		File file = new File(resultFolderPath + "HTMLReport.html");
		if (!file.exists())
			file.createNewFile();
		else {
			file.delete();
			try {
				file.createNewFile();
			} catch (Exception e) {
				Thread.sleep(200);
				file.createNewFile();
			}
		}
		FileWriter writer = new FileWriter(file);
		BufferedWriter buffer = new BufferedWriter(writer);
		buffer.write(s);
		buffer.close();
		return file.getAbsolutePath();
	}

	public String getReportTable(LinkedHashMap<String, TestResult> testResults) {

		String startText = "<table class='Report'>  <tr style='background-color:#5c05ad;color:#d6d4d8;' >    <th style='width:35px'>S.No</th>    <th style='width:80px'>Scenario ID</th> <th style='width:80px'>TestCase ID</th>   <th>Testcase Desc</th>    <th style='width:65px'>Status</th>    <th style='width:150px'>Duration</th>    <th style='width:100px'>Screenshot</th>  </tr> ";
		String endText = "</table>";
		String rowstext = "";
		String trText = "";
		String rowBackgroundColorhexCode = "";
		int i = 1;

		for (String testID : testResults.keySet()) {
			TestResult testResult = testResults.get(testID);
			if (testResult.getStatus().equalsIgnoreCase("Fatal")) {
				testResults.get(testID).setStatus("Fail");
			}
			switch (testResult.getStatus()) {
			case "Pass":
				rowBackgroundColorhexCode = "#c6ed9e";
				break;
			case "Fail":
				rowBackgroundColorhexCode = "#f58d97";
				break;
			case "Fatal":
				rowBackgroundColorhexCode = "#f58d97";
				break;
			case "Running":
				rowBackgroundColorhexCode = "#97beef";
				break;
			case "Pending":
				rowBackgroundColorhexCode = "#eabc81";
				break;
			default:
				break;
			}

			String reportLink = testResult.getReportLink();
			String failureReason = testResult.getFailureReason();
			if (reportLink == null || reportLink.trim().isEmpty() || reportLink.trim().equalsIgnoreCase("#")) {
				reportLink = "N/A";
			}

			if (failureReason == null || failureReason.trim().isEmpty()) {
				failureReason = "N/A";
			} else {
				failureReason = "Check Report";
			}

			LinkedHashMap<String, String> allXbridID = testResult.getXbridTC_IDStatus();
			if (allXbridID != null && !allXbridID.isEmpty()) {
				int rowspan = allXbridID.size();
				boolean first = true;
				for (Map.Entry<String, String> entry : allXbridID.entrySet()) {
					String XbId = entry.getKey();
					String XbStatus = entry.getValue();
					String XbridIdBgColor = getXbridbgColor(XbStatus);
					String XbridValidationPoint = NFT_Executer.XbridValidationPoints.get(XbId);
                      
					// System.out.println("Row Span --> " + rowspan);
					if (first) {
						first = false;
						String First;
						System.out.println("8888888888888888888");
						System.out.println(testResult.getTestScenarioName());
						if (reportLink.equalsIgnoreCase("N/A")) {
							First = "<tr style='background-color:" + rowBackgroundColorhexCode
									+ ";word-wrap:break-word;'><td rowspan='" + rowspan + "'>" + i
									+ "</td><td rowspan='" + rowspan + "'>" + testResult.getTestCaseID() + "</td>"
									+ "<td style='background-color:" + XbridIdBgColor + "'>" + XbId
									+ "</td><td style='background-color:" + XbridIdBgColor + "'>" + testResult.getTestScenarioName()
									+ "</td>    <td rowspan='" + rowspan + "'>" + testResult.getStatus()
									+ "</td><td rowspan='" + rowspan + "'>" + testResult.getDuration()
									+ "</td><td rowspan='" + rowspan + "'>" + reportLink + "</td></tr>";

						} else {
							First = "<tr style='background-color:" + rowBackgroundColorhexCode
									+ ";word-wrap:break-word;'><td rowspan='" + rowspan + "'>" + i
									+ "</td><td rowspan='" + rowspan + "'>" + testResult.getTestCaseID() + "</td>"
									+ "<td style='background-color:" + XbridIdBgColor + "'>" + XbId
									+ "</td><td style='background-color:" + XbridIdBgColor + "'>" + testResult.getTestScenarioName()
									+ "</td>    <td rowspan='" + rowspan + "'>" + testResult.getStatus()
									+ "</td><td rowspan='" + rowspan + "'>" + testResult.getDuration()
									+ "</td><td rowspan='" + rowspan + "'><a href='" + reportLink
									+ "'>click here</a></td></tr>";
						}

						trText = First;
					} else {

						String additional = "<tr><td style='background-color:" + XbridIdBgColor
								+ ";word-wrap:break-word;'>" + XbId + "</td><td style='background-color:"
								+ XbridIdBgColor + ";word-wrap:break-word;'>" + testResult.getTestScenarioName() + "</td></tr>";
						trText = trText + additional;
					}

				}
				i++;
				rowstext += trText;
			} else {

				if (reportLink.equalsIgnoreCase("N/A")) {
					trText = "<tr style='background-color:" + rowBackgroundColorhexCode + ";word-wrap:break-word;'><td>"
							+ i + "</td><td>" + testResult.getTestCaseID() + "</td><td>" + testResult.getXbridTC_ID()
							+ "</td><td>" + testResult.getTestScenarioName() + "</td>    <td>" + testResult.getStatus()
							+ "</td><td>" + testResult.getDuration() + "</td><td>" + reportLink + "</td></tr> ";
				} else {
					trText = "<tr style='background-color:" + rowBackgroundColorhexCode + ";word-wrap:break-word;'><td>"
							+ i + "</td><td>" + testResult.getTestCaseID() + "</td><td>" + testResult.getXbridTC_ID()
							+ "</td><td>" + testResult.getTestScenarioName() + "</td>    <td>" + testResult.getStatus()
							+ "</td><td>" + testResult.getDuration() + "</td><td><a href='" + reportLink
							+ "'>click here</a></td></tr> ";
				}

				i++;

				rowstext += trText;
			}
		}
		return startText + rowstext + endText;
	}

	public String getXbridbgColor(String status) {
		String color = "#eabc81";
		switch (status) {
		case "Pass":
			color = "#c6ed9e";
			break;
		case "Fail":
			color = "#f58d97";
			break;
		case "Running":
			color = "#97beef";
			break;
		case "Pending":
			color = "#eabc81";
			break;
		default:
			break;
		}

		return color;

	}

	public String getFailureDetails(ArrayList<String> statusFail) {
		String failurereason = null;
		for (String failure : statusFail) {
			System.out.println(failure);
			failurereason = failure;
		}

		return failurereason;

	}

}