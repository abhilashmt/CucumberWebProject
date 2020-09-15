package ExecutionRepo.StepDefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ExecutionRepo.Hooks;
import ObjectRepo.readOR;
import ReporterRepo.Reporter;
import cucumber.api.java.en.When;

public class FillSteps {
	static WebDriver driver;
	readOR objOR;
	static WaitStep wait;
	static CommonSteps commonObj;
	static int iCount = 0;

	public FillSteps() {
		driver = Hooks.driver;
		objOR = new readOR();
		wait = new WaitStep();
		commonObj = new CommonSteps();
	}

	// Fill element in a page
	public void FillTextbox(String sPageName, String sControlName, String sValue) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sControlName);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObject(sPageName, sControlName);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", obj);
		try {
			Thread.sleep(500);
			obj.clear();
			Thread.sleep(500);
			obj.sendKeys(sValue);
			Thread.sleep(500);
			obj.sendKeys(Keys.TAB);
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
		Thread.sleep(2000);
	}

	// Fill textbox inside a frame
	// Pass the frame id as 'sFrame'
	public void FillTextbox(String sPageName, String sFrame, String sControlName, String sValue) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sFrame, sControlName);
		wait.waitForElementDisplayed("//iframe[@id='" + sFrame + "']");
		commonObj.switchToFrame(sFrame);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObjectFromFrame(sPageName, sFrame, sControlName);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", obj);
		try {
			obj.click();
			Thread.sleep(500);
			int iSize = obj.getAttribute("value").length();
			for (int i = 0; i < iSize; i++) {
				obj.sendKeys(Keys.BACK_SPACE);
			}
			Thread.sleep(500);
			obj.sendKeys(sValue);
			obj.sendKeys(Keys.TAB);
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
		driver.switchTo().defaultContent();
		Thread.sleep(2000);
	}

	// Select dropdown in a page
	public void SelectDropdownValue(String sPageName, String sControlName, String sValue) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sControlName);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObject(sPageName, sControlName);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", obj);
		try {
			Select dropdown = new Select(obj);
			dropdown.selectByVisibleText(sValue);
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
		Thread.sleep(2000);
	}

	public void SelectCheckboxValue(String sPageName, String sControlName, String sValue) throws Exception {
		String sXpath = objOR.getXpath(sPageName, sControlName);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObject(sPageName, sControlName);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", obj);
		try {
			if (sValue.toUpperCase().contentEquals("CHECK")) {
				if(!obj.getAttribute("checked").contentEquals("true")) {
					obj.click();
				}				
			} else if (sValue.toUpperCase().contentEquals("UNCHECK")) {
				if(obj.getAttribute("checked").contentEquals("true")) {
					obj.click();
				}
			} else {
				Reporter.report("INFO", "Invalid control value for checkbox " + sValue);
				Reporter.report("FAIL", "");
			}
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
	}

	@When("user enter \"([^\"]*)\" in \"([^\"]*)\" textbox in \"([^\"]*)\" page")
	public void enter_textbox_value(String sValue, String sControl, String sPage) throws Throwable {
		FillTextbox(sPage, sControl, sValue);
	}

	@When("user select \"([^\"]*)\" in \"([^\"]*)\" dropdown in \"([^\"]*)\" page")
	public void select_dropdown_value(String sValue, String sControl, String sPage) throws Throwable {
		SelectDropdownValue(sPage, sControl, sValue);
	}

	@When("user \"([^\"]*)\" the \"([^\"]*)\" checkbox in \"([^\"]*)\" page")
	public void enter_checkbox_value(String sValue, String sControl, String sPage) throws Throwable {
		SelectCheckboxValue(sPage, sControl, sValue);
	}
}
