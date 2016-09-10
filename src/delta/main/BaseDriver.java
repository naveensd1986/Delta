package delta.main;

import org.openqa.selenium.WebDriver;
//import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import generics.Excel;


public class BaseDriver implements AutomationConstants
{
	public WebDriver driver;
	public static ExtentReports eReport;
	public ExtentTest testReport;
	
	
	@DataProvider
	//public static String[][] getScenarios() 
	public String[][] getScenarios()
	{
		/*String[][] data=new String[2][1];
		data[0][0]="Scenario1";
		data[1][0]="Scenario2";*/
		
		int scenarioCount=Excel.getRowCount(controllerPath, suiteSheet);
		System.out.println("Number of Scenarios present are: "+scenarioCount);
		String[][] data=new String[scenarioCount][2];
		for(int i=1;i<=scenarioCount; i++)
		{
			String scenarioName=Excel.getCellValue(controllerPath, suiteSheet, i, 0);
			String executionStatus=Excel.getCellValue(controllerPath, suiteSheet, i, 1);
			
			data[i-1][0]=scenarioName;
			data[i-1][1]=executionStatus;
		}		
		return data;
	}
	

	@BeforeTest
	public void initFrameWork()
	{
		eReport=new ExtentReports(reportFilePath);
	}
	
	@AfterTest
	public void endFrameWork()
	{
		eReport.flush();
	}

}
