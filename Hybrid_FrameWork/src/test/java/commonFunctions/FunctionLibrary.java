package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static WebDriver driver;  //Import global variable "webdriver", used for accesing webdriver methods
	public static Properties conpro; //Used for accessing properties class methods
	//Method for launching browser
	public static WebDriver startBrowser() throws Throwable
	{
		conpro = new Properties();
		//Load property file here
		conpro.load(new FileInputStream("./Propertyfiles/Environment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();                                        //This class is for methods like typeaction, wait for element and generate date
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is not matching",true);
		}
		return driver;
	}
	//Method for launching url
	public static WebDriver OpenUrl()
	{
		driver.get(conpro.getProperty("Url"));
		return driver;
	}
	//method for wait to any webelement in a page
	public static void Waitforelement(String LocatorType,String LocatorValue,String TestData)
	{ 
		WebDriverWait mywait = new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));

		}

	}
	//method for any textbox
	public static void typeAction(String LocatorType,String LocatorValue, String testData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(testData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(testData);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(testData);
		}
	}
	//method for any buttons,checkboxes, radiobuttons, images and links
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	//method for validate title
	public static void Validatetitle(String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title,"Title is not matching");    //assert = returns false
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());

		}

	}
	//method for closing
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for generate date format
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY-MM-DD-HH-MM-SS");
		return df.format(date);

	}
	//Method for listboxes
	public static void  dropDownAction(String LocatorType, String LocatorValue, String testData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//convert testdata string into integer
			int value = Integer.parseInt(testData);  //parseint is use to convert string into integer 
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//convert testdata string into integer
			int value = Integer.parseInt(testData);  //parseint is use to convert string into integer 
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//convert testdata string into integer
			int value = Integer.parseInt(testData);  //parseint is use to convert string into integer 
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}
	}

	//method for capturing stock number into notepad
	public static void captureStock(String LocatorType, String LocatorValue) throws Throwable
	{
		String Stocknum = "";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			Stocknum = driver.findElement(By.name(LocatorValue)).getAttribute("value");

		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			Stocknum = driver.findElement(By.id(LocatorValue)).getAttribute("value");

		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			Stocknum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");

		}
		//create notepad
		FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");  //in stocknumber.txt the stock number is present is there
		BufferedWriter bw = new BufferedWriter(fw); //BufferedWriter is used to allocate  the memory for fw because it is our physical file
		//bw object is holding memory methods
		bw.write(Stocknum);
		bw.flush();
		bw.close(); //flush means overwriting previous steps

	}
	//method for validate stock number in table
	public static void stockTable() throws Throwable
	{
		// read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_data = br.readLine();  //read line means reading line by line and stock number is stored in exp_data
		//if search text box is already displayed dont click search panel button
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())  //This condition is only if condition not write full stop after sentence completion or dont open pharanthesis because this condition is true conditon
			//if search textbox not displayed click search panel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		//clear the text if anything is existing in textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		//enter stock number into search box
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(exp_data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Act_Data+"    "+exp_data,true);
		//comparing exp_data with act_data
		try {
			Assert.assertEquals(Act_Data, exp_data,"Stock number should not match"); //becoz assert always written false
		}catch(AssertionError a)
		{
			Reporter.log(a.getMessage(),true);                                    
		}
	}
	//method for supplier number to capture into notepad
	public static void captureSup(String LocatorType, String LocatorValue)throws Throwable
	{ 
		String Suppliernum = "";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			Suppliernum = driver.findElement(By.name(LocatorValue)).getAttribute("value");

		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			Suppliernum = driver.findElement(By.id(LocatorValue)).getAttribute("value");

		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			Suppliernum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");

		}
		//create notepad
		FileWriter fw = new FileWriter("./CaptureData/supplierNumber.txt");  //in stocknumber.txt the stock number is present is there
		BufferedWriter bw = new BufferedWriter(fw); //BufferedWriter is used to allocate  the memory for fw because it is our physical file
		//bw object is holding memory methods
		bw.write(Suppliernum);
		bw.flush();
		bw.close(); //flush means overwriting previous steps

		
	}
	//method for supplier table
	public static void supplierTable()throws Throwable
	{
		// read stock number from notepad
				FileReader fr = new FileReader("./CaptureData/supplierNumber.txt");
				BufferedReader br = new BufferedReader(fr);
				String exp_data = br.readLine();  //read line means reading line by line and stock number is stored in exp_data
				//if search text box is already displayed dont click search panel button
				if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())  //This condition is only if condition not write full stop after sentence completion or dont open pharanthesis because this condition is true conditon
					//if search textbox not displayed click search panel button
					driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
				//clear the text if anything is existing in textbox
				driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
				//enter stock number into search box
				driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(exp_data);
				driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
				Thread.sleep(3000);
				String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
				Reporter.log(Act_Data+"    "+exp_data,true);
				//comparing exp_data with act_data
				try {
					Assert.assertEquals(Act_Data, exp_data,"Stock number should not match"); //becoz assert always written false
				}catch(AssertionError a)
				{
					Reporter.log(a.getMessage(),true);                                    
				}
		
	}
	
	//method for capture customer number into notepad
	public static void captureCus(String LocatorType, String LocatorValue)throws Throwable
	{
		String Customernum = "";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			Customernum = driver.findElement(By.name(LocatorValue)).getAttribute("value");

		}
		if(LocatorType.equalsIgnoreCase("id"))
		{

			 Customernum = driver.findElement(By.id(LocatorValue)).getAttribute("value");

		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			 Customernum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");

		}
		//create notepad
		FileWriter fw = new FileWriter("./CaptureData/CustomerNumber.txt");  //in stocknumber.txt the stock number is present is there
		BufferedWriter bw = new BufferedWriter(fw); //BufferedWriter is used to allocate  the memory for fw because it is our physical file
		//bw object is holding memory methods
		bw.write(Customernum);
		bw.flush();
		bw.close();
		
	}
	//verify customer number in customer table
	public static void CustomerTable()throws Throwable
	{
		
		// read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/CustomerNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_data = br.readLine();  //read line means reading line by line and stock number is stored in exp_data
		//if search text box is already displayed dont click search panel button
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())  //This condition is only if condition not write full stop after sentence completion or dont open pharanthesis because this condition is true conditon
			//if search textbox not displayed click search panel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		//clear the text if anything is existing in textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		//enter stock number into search box
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(exp_data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);                   //table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Act_Data+"    "+exp_data,true);
		//comparing exp_data with act_data
		try {
			Assert.assertEquals(Act_Data, exp_data,"Stock number should not match"); //becoz assert always written false
		}catch(AssertionError a)
		{
			Reporter.log(a.getMessage(),true);                                    
		}

	}

}


