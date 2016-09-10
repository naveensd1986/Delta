package delta.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.relevantcodes.extentreports.LogStatus;

import generics.Excel;
import generics.Property;
import generics.Utility;

public class DeltaDriver extends BaseDriver
{
	public String browser;
	public String hubURL;
	
	@BeforeMethod
	public void launchApp(XmlTest xmlTest) throws MalformedURLException
	{
		String configPptPath="./config/config.properties";
			
		//String browser=xmlTest.getParameter("browser");
		browser=xmlTest.getParameter("browser");
		hubURL=xmlTest.getParameter("hubURL");
		DesiredCapabilities dc=new DesiredCapabilities();
		dc.setBrowserName(browser);
		dc.setPlatform(Platform.ANY);
		driver=new RemoteWebDriver(new URL(hubURL), dc);
		
		String appURL=Property.getPropertyValue(configPptPath, "URL");
		String timeout=Property.getPropertyValue(configPptPath, "Timeout");
		
		/*if(browser.equals("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			driver=new ChromeDriver();
		}
		else
		{
			driver=new FirefoxDriver();
		}*/
		driver.get(appURL);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(timeout), TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
		
	//@Test(dataProvider="getScenarios", dataProviderClass=BaseDriver.class) //Should be used for static getScenarios() only but for not non-static method
	@Test(dataProvider="getScenarios")
	public void testScenarios(String scenarioSheet, String ExecutionStatus) throws InterruptedException
	{
		testReport=eReport.startTest(browser+"_"+scenarioSheet);
		
		if(ExecutionStatus.equalsIgnoreCase("Yes"))
		{
			int rc=Excel.getRowCount(scenariosPath, scenarioSheet);
			
			for(int i=1; i<=rc; i++)
			{
			String description=Excel.getCellValue(scenariosPath, scenarioSheet, i, 0);
			String action=Excel.getCellValue(scenariosPath, scenarioSheet, i, 1);
			String input1=Excel.getCellValue(scenariosPath, scenarioSheet, i, 2);
			String input2=Excel.getCellValue(scenariosPath, scenarioSheet, i, 3);
			
			String msg="description: "+description+"action: "+action+"input1: "+input1+"input2: "+input2;
			testReport.log(LogStatus.INFO,msg);
			
			System.out.println(description +" "+action+" "+input1+" "+input2);
			Keyword.executeKeyWord(driver, action, input1, input2);
			//Assert.fail();
			}
		}
		else
		{
			testReport.log(LogStatus.SKIP, "Execution Status is 'No'");
			throw new SkipException("Skipping this Scenario...");
		}
	
		
	}

	@AfterMethod
	public void quitApp(ITestResult test)
	{
		if(test.getStatus()==ITestResult.FAILURE)
		{
		String pImage=Utility.getPageScreenShot(driver, imageFolderPath);
		String p=testReport.addScreenCapture("."+pImage);
		testReport.log(LogStatus.FAIL, "Page screen shot: "+p);
		}
		eReport.endTest(testReport);
		driver.close();
	}
	
	
}
