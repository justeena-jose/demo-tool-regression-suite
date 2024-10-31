package com.regression.test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import demo.tool.project.regression.suite.Constants;
import demo.tool.project.regression.suite.Screenshots;
import demo.tool.project.regression.suite.TestData;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SearchTest {

		WebDriver driver;
		ExtentReports extent = new ExtentReports();
		ExtentTest logger;

		@BeforeSuite
		public void Report_Setup() {

			ExtentHtmlReporter Launch = new ExtentHtmlReporter("ExtentReports/Demo Tool.html");
			Launch.config().setDocumentTitle("Demo Tool Regression Report");
			Launch.config().setReportName("Demo Tool");
			Launch.config().setTheme(Theme.STANDARD);
			extent.attachReporter(Launch);

		}

		@BeforeClass
		public void initClass() {
			// Open the browser
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();	
			

	}
		@Test(priority=1)
		public void NavigateToHomePage() {
			try {
				logger = extent.createTest("Test case 1: Check whether the user able to navigate to the application and  the app shows all the products or not");
				driver.get(Constants.URL);
				Thread.sleep(5000);
				String ApplicationTitle=driver.getTitle();
				System.out.print("..................."+ApplicationTitle);
				String expTitle = "Practice Software Testing - Toolshop - v5.0";		
				Assert.assertTrue(driver.getTitle().equalsIgnoreCase(expTitle));
				logger.log(Status.PASS, "Application launched properly");
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		@Test(priority=2)
			public void ProductDetailPage()
			{
				try {
					
				
				logger=extent.createTest("Test Case 2: Check whether when the user selects/clicks on the product it will open the detailed page or not");
				Thread.sleep(5000);
				new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.PRODUCT))).click();
				Thread.sleep(5000);
				WebElement header = driver.findElement(By.xpath(Constants.PRODUCTTITLE));
				String pageTitle = header.getText();
		         System.out.print(pageTitle);
				if(pageTitle.contains("Pliers"))
				{
					Assert.assertTrue(true);		
					logger.log(Status.PASS, "Detailed page opened successfully");
				}
				else
				{
					Assert.assertTrue(false);
					logger.log(Status.FAIL, "Detailed page not opened");
				}
				
				
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		@Test(priority=3)
		public void AddToCart() throws InterruptedException{
			logger=extent.createTest("Test Case 3: Check whether  the user able to add the selected item in to cart or not");
			Thread.sleep(5000);
			
			new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.ADDTOCART))).click();
			Thread.sleep(5000);
			new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.CART))).click();
			Thread.sleep(5000);
			boolean isEnabled = driver.findElement(By.xpath(Constants.CARTELEMENT)).isEnabled();
			boolean isDisplayed = driver.findElement(By.xpath(Constants.CARTELEMENT)).isDisplayed()	;
			if (isEnabled)
			{
				logger.log(Status.PASS, "Item added to the cart successfully");
			}
			else
			{
				logger.log(Status.FAIL, "Item not added to the cart");
			}
		}
		@Test(priority=4)
		public void CheckOUT() throws InterruptedException{
			logger=extent.createTest("Test Case 4: Check whether  the user able to check out from cart or not");
			Thread.sleep(5000);
			new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.PROCEEDTOCHECKOUT))).click();
			
			boolean isEnabled = driver.findElement(By.xpath(Constants.LOGINELEMENT)).isEnabled();
			boolean isDisplayed = driver.findElement(By.xpath(Constants.LOGINELEMENT)).isDisplayed()	;
			if (isEnabled)
			{
				logger.log(Status.PASS, "Button proceed to check out is working");
			}
			else
			{
				logger.log(Status.FAIL, "Button proceed to check out is not working");
			}
		}
		@Test(priority=5)
			public void LoginandPay() throws InterruptedException
			{
				logger=extent.createTest("Test Case 5: Verify whether the user able to login with the registerd email id and password");
				
				Thread.sleep(5000);
				new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.EMAIL))).sendKeys(TestData.EMAILID);
				new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.PASSWORD))).sendKeys(TestData.PASSWORD);
				new WebDriverWait(driver,Duration.ofSeconds(50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Constants.LOGIN))).click();
				String expTitle = "Practice Software Testing - Toolshop - v5.0";		
				Assert.assertTrue(driver.getTitle().equalsIgnoreCase(expTitle));
				logger.log(Status.PASS, "Application logined for checkout");
				
				
			}
				
		
		@AfterMethod
		public void getResult(ITestResult result)throws Exception
		{
			if(result.getStatus() == ITestResult.FAILURE) 
			{
				//To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
				//We do pass the path captured by this mehtod in to the extent reports using "logger.addScreenCapture" method. 
				String screenshotPath = Screenshots.getScreenshot(driver, result.getName());
				logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" FAILED ", ExtentColor.RED));
				logger.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());		

			}
			else if(result.getStatus() == ITestResult.SUCCESS) {
				logger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" PASSED ", ExtentColor.GREEN));
			}
			else if(result.getStatus() == ITestResult.SKIP){

				logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" SKIPPED ", ExtentColor.ORANGE));
				logger.skip(result.getThrowable());
			}
		}
		@AfterTest
		public void updateReport() 
	    {
			//to write or update test information to reporter
			extent.flush();	
	    }
		@AfterClass
		public void finish()
		{             
		    driver.manage().deleteAllCookies();
		    driver.close();
		}
	}


