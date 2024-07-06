package driverFactory;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver; // global variables declaration
	String inputpath = "./FileInput/DataEngine.xlsx"; // Input path holding testcases i.e fileinputstream
	String outputpath = "./FileOutput/HybridResults.xlsx"; // Outpath is holding results file i.e fileoutputstream
	ExtentReports reports; // Driverscript class is used to run all the files present in both classes
	ExtentTest logger;
	String TCsheet = "MasterTestCases";
	private ExtentReports report;

	@Test
	public void startTest() throws Throwable {
		// Local variables
		String Module_status = "";
		String Module_new = "";
		// create object for excel file util class
		ExcelFileUtil xl = new ExcelFileUtil(inputpath); // rowcount, getcelldata, setcelldata we need to call with the
															// object = xl
		// iterate all test cases in TCSheet //xl holding path as well as methods
		for (int i = 1; i <= xl.rowCount(TCsheet); i++) {
			if (xl.getCellData(TCsheet, i, 2).equalsIgnoreCase("Y")) {
				// store each sheet into one variable
				String TCModule = xl.getCellData(TCsheet, i, 1);
				// define path of html
				report = new ExtentReports(
						"./target/ExtentReports/" + TCModule + FunctionLibrary.generateDate() + ".html");
				// set pre-condition for extentreports i.e start logging
				logger = report.startTest(TCModule);
				// To find the name of the author in reports
				logger.assignAuthor("Pranay Chandekar");
				for (int j = 1; j <= xl.rowCount(TCModule); j++) {
					// read each cell from TCModule
					String Description = xl.getCellData(TCModule, j, 0);
					String ObjectType = xl.getCellData(TCModule, j, 1);
					String Ltype = xl.getCellData(TCModule, j, 2);
					String Lvalue = xl.getCellData(TCModule, j, 3);
					String TestData = xl.getCellData(TCModule, j, 4);
					try {
						if (ObjectType.equalsIgnoreCase("StartBrowser")) {
							driver = FunctionLibrary.startBrowser();
							// information for every keyword present in excel i.e description
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("OpenUrl")) {
							driver = FunctionLibrary.OpenUrl();
							logger.log(LogStatus.INFO, Description);

						}
						if (ObjectType.equalsIgnoreCase("Waitforelement")) {
							FunctionLibrary.Waitforelement(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);

						}
						if (ObjectType.equalsIgnoreCase("typeaction")) {
							FunctionLibrary.typeAction(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);

						}

						if (ObjectType.equalsIgnoreCase("ClickAction")) {
							FunctionLibrary.clickAction(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);

						}

						if (ObjectType.equalsIgnoreCase("ValidataTitle")) {
							FunctionLibrary.Validatetitle(TestData);
							logger.log(LogStatus.INFO, Description);

						}
						if (ObjectType.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);

						}
						if (ObjectType.equalsIgnoreCase("dropDownAction")) {
							FunctionLibrary.dropDownAction(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("captureStock")) {
							FunctionLibrary.captureStock(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("stockTable")) {
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captureSup"))
						{
							FunctionLibrary.captureSup(Ltype, Lvalue);
							logger.log(LogStatus.INFO,Description);
						}
						if(ObjectType.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("captureCus"))
						{
							FunctionLibrary.captureCus(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(ObjectType.equalsIgnoreCase("CustomerTable"))
						{
							FunctionLibrary.CustomerTable();
							logger.log(LogStatus.INFO, Description);
						}
						
						
						
                        
						// write as pass into status cell in TCModule sheet
						xl.setCellData(TCModule, j, 5, "PASS", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_status = "True";

					} catch (Throwable t) {
						System.out.println(t.getMessage());
						// Write as fail into status in TCModule sheet
						xl.setCellData(TCModule, j, 5, "FAIL", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_new = "False";
						// take screenshot
						File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File(
								"./target/Screenshot/" + Description + FunctionLibrary.generateDate() + ".png"));

					}
					if (Module_status.equalsIgnoreCase("True")) {
						xl.setCellData(TCsheet, i, 3, "PASS", outputpath);
					}
					if (Module_new.equalsIgnoreCase("False")) {
						xl.setCellData(TCsheet, i, 3, "FAIL", outputpath);
					}
					report.endTest(logger);
					report.flush();

				}
			} else {
				// write as blocked for testcases flag to N
				xl.setCellData(TCsheet, i, 3, "Blocked", outputpath);
			}
		}
	}

}
