package demo.tool.project.regression.suite;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class ExtentReport {
	static ExtentReports extent;
	static ExtentTest logger;
	static WebDriver driver;
	
    //Create Extent report for the regression suite
	public static void createExtentReport() throws IOException
    {
		ExtentHtmlReporter reporter = new ExtentHtmlReporter(System.getProperty("TestPortalManager")+"/ExtentReports");
	    extent = new ExtentReports();
		extent.attachReporter(reporter);		
		reporter.config().setDocumentTitle("Portal Manager Test Report");
		reporter.config().setReportName("Portal Manager Test Report");
    }
    //Create each test and mark its status in the report
    public static void addTestStatus(ITestResult result, WebDriver driver) throws Exception
    {
		ExtentTest logger = extent.createTest(result.getName());
		if (result.getStatus() == ITestResult.FAILURE) {

			String temp = Screenshots.getScreenshot(driver,result.getName());

			logger.log(Status.FAIL, MarkupHelper.createLabel(
					result.getMethod().getMethodName() + " Test case FAILED due to below issues:", ExtentColor.RED));
			logger.fail(result.getThrowable().getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(temp).build());

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS,
					MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
		}

		else  {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
			logger.skip(result.getThrowable());
		}
    }
    //Generate final report with all test cases
    public static void Finalreport() throws IOException
    {
		extent.flush();
		
	}
}


