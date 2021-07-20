package nft.Report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TestResult {
	private String testCaseID;
	private String testScenarioName;
	private String testScenarioSteps;
	
	private String status;

	private String duration;
	private String reportLink;
	private String failureReason;
	private String tcdescription;
	
	private ArrayList<String> xbridTC_ID;
	
	private LinkedHashMap<String,String> xbridTC_IDStatus=new LinkedHashMap<String,String>();
	
	
	
	public LinkedHashMap<String, String> getXbridTC_IDStatus() {
		return xbridTC_IDStatus;
	}
	public void setXbridTC_IDStatus(LinkedHashMap<String, String> xbridTC_IDStatus) {
		this.xbridTC_IDStatus = xbridTC_IDStatus;
	}
	public String getTestScenarioSteps() {
		return testScenarioSteps;
	}
	public void setTestScenarioSteps(String testScenarioSteps) {
		this.testScenarioSteps = testScenarioSteps;
	}
	public ArrayList<String> getXbridTC_ID() {
		return xbridTC_ID;
	}
	public void setXbridTC_ID(ArrayList<String> xbridTC_ID) {
		this.xbridTC_ID = xbridTC_ID;
	}
	public String getTestCaseID() {
		return testCaseID;
	}
	public void setTestCaseID(String testCaseID) {
		this.testCaseID = testCaseID;
	}
	public String getTestScenarioName() {
		return testScenarioName;
	}
	public void setTestScenarioName(String testScenarioName) {
		this.testScenarioName = testScenarioName;
	}
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getReportLink() {
		return reportLink;
	}
	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}
	
		public String getFailureReason() {
			return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	public String gettcdesciption() {
		
		return tcdescription;
}
public void settcdesciption(String tcdescription) {
	this.tcdescription = tcdescription;
}
	
}
