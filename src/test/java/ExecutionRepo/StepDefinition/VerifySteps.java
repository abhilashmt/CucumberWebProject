package ExecutionRepo.StepDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import ExecutionRepo.Hooks;
import ObjectRepo.readOR;
import ReporterRepo.Reporter;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class VerifySteps {
	static WebDriver driver;
	readOR objOR;
	static WaitStep wait;
	static CommonSteps commonObj;

	public VerifySteps() {
		driver = Hooks.driver;
		objOR = new readOR();
		wait = new WaitStep();
		commonObj = new CommonSteps();
	}

	// Verify element displayed in a page
	public void VerifyElementDisplayed(String sPageName, String sControlName) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sControlName);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObject(sPageName, sControlName);
	}

	// Verify element not displayed in a page
	public void VerifyElementNotDisplayed(String sPageName, String sControlName) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sControlName);
		wait.waitForElementDisappear(sXpath);
	}

	// Verify element displayed in a page
	public boolean isDisplayed(String sPageName, String sControlName) throws Throwable {
		boolean bTrue = false;
		try {
			String sXpath = objOR.getXpath(sPageName, sControlName);
			if (driver.findElement(By.xpath(sXpath)).isDisplayed()) {
				bTrue = true;
			}
		} catch (TimeoutException e) {
			// TODO: handle exception
		}
		return bTrue;
	}

	// Verify element not displayed in a page
	public boolean isNotDisplayed(String sPageName, String sControlName) throws Throwable {
		boolean bTrue = false;
		try {
			String sXpath = objOR.getXpath(sPageName, sControlName);
			driver.findElement(By.xpath(sXpath));
		} catch (NoSuchElementException e) {
			bTrue = true;
		}
		return bTrue;
	}

	// Verify element displayed in a page
	public boolean isEnabled(String sPageName, String sControlName) throws Throwable {
		boolean bTrue = false;
		try {
			String sXpath = objOR.getXpath(sPageName, sControlName);
			wait.waitForElementDisplayed(sXpath);
			WebElement obj = objOR.getObject(sPageName, sControlName);
			if (obj.isEnabled()) {
				bTrue = true;
			}
		} catch (TimeoutException te) {

		}
		return bTrue;
	}

	// Verify element displayed inside a frame
	// Pass the frame id as 'sFrame'
	public void VerifyElementDisplayed(String sPageName, String sFrame, String sControlName) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sFrame, sControlName);
		wait.waitForElementDisplayed("//iframe[@id='" + sFrame + "']");
		commonObj.switchToFrame(sFrame);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObjectFromFrame(sPageName, sFrame, sControlName);
	}

	@When("user verify \"([^\"]*)\" control is displayed in \"([^\"]*)\" page")
	public void verify_control_displayed(String sControl, String sPage) throws Throwable {
		VerifyElementDisplayed(sPage, sControl);
	}

	@When("user verify \"([^\"]*)\" control is displaying with \"([^\"]*)\" text in \"([^\"]*)\" page")
	public void verify_control_displayed(String sControl, String sValue, String sPage) throws Throwable {
		if (isDisplayed(sPage, sControl)) {
			WebElement obj = objOR.getObject(sPage, sControl);
			if (!obj.getText().contentEquals(sValue)) {
				Reporter.report("INFO",
						sControl + " is displaying with text " + obj.getText() + " in " + sPage + " page");
				Reporter.report("FAIL", "");
			}
		} else {
			Reporter.report("INFO", sControl + " is not displayed in " + sPage + " page");
			Reporter.report("FAIL", "");
		}
	}

	@When("user verify \"([^\"]*)\" textbox is displaying with \"([^\"]*)\" text in \"([^\"]*)\" page")
	public void verify_textbox_displayed(String sControl, String sValue, String sPage) throws Throwable {
		if (isDisplayed(sPage, sControl)) {
			WebElement obj = objOR.getObject(sPage, sControl);
			if (!obj.getAttribute("value").contentEquals(sValue)) {
				Reporter.report("INFO",
						sControl + " is displaying with text " + obj.getAttribute("value") + " in " + sPage + " page");
				Reporter.report("FAIL", "");
			}
		} else {
			Reporter.report("INFO", sControl + " is not displayed in " + sPage + " page");
			Reporter.report("FAIL", "");
		}
	}

	@Then("^verify below controls in \"([^\"]*)\" page$")
	public void verify_below_controls_in_page(String sPage, DataTable dt) throws Throwable {
		boolean bFail = false;
		List<List<String>> expValues = dt.asLists();
		for (int i = 0; i < expValues.size(); i++) {
			try {
				String sKey = expValues.get(i).get(0);
				String sVal = expValues.get(i).get(1);
				switch (sVal.toUpperCase()) {
				case "DISPLAYED":
					if (!isDisplayed(sPage, sKey)) {
						bFail = true;
						Reporter.report("INFO", sKey + " is not displayed");
					}
					break;
				case "NOT DISPLAYED":
					if (!isNotDisplayed(sPage, sKey)) {
						bFail = true;
						Reporter.report("INFO", sKey + " is displayed");
					}
					break;
				case "ENABLED":
					if (!isEnabled(sPage, sKey)) {
						bFail = true;
						Reporter.report("INFO", sKey + " is disabled");
					}
					break;
				case "DISABLED":
					if (isEnabled(sPage, sKey)) {
						bFail = true;
						Reporter.report("INFO", sKey + " is enabled");
					}
					break;
				default:
					bFail = true;
					Reporter.report("INFO", "The control status " + sVal + " is not valid for " + sKey);
				}
			} catch (Exception e) {
				bFail = true;
				Reporter.report("INFO", e.getMessage());
			}
		}
		if (bFail) {
			Reporter.report("FAIL", "");
		}
	}

	@Then("^verify \"([^\"]*)\" dropdown in \"([^\"]*)\" page contains below values$")
	public void verify_dropdown_contains_below_values(String sDropdown, String sPage, DataTable dt) throws Throwable {
		List<List<String>> expValues = dt.asLists();
		wait.waitForElementDisplayed(objOR.getXpath(sPage, sDropdown));
		WebElement oControl = objOR.getObject(sPage, sDropdown);
		Select select = new Select(oControl);
		boolean bFail = false;
		List<WebElement> arrOptions = select.getOptions();
		List<String> actValues = new ArrayList<String>();
		for (WebElement option : arrOptions) {
			actValues.add(option.getText());
		}
		for (int i = 0; i < expValues.size(); i++) {
			String sExpVal = expValues.get(i).get(0);
			if (!actValues.contains(sExpVal)) {
				bFail = true;
				Reporter.report("INFO", sExpVal + " is not displayed in the dropdown");
			}
		}
		if (bFail) {
			Reporter.report("INFO", sDropdown + " dropdown values are not displaying as expected");
			Reporter.report("INFO", "Expected: " + expValues + ", Actual values: " + actValues);
			Reporter.report("FAIL", "");
		} else {
			Reporter.report("PASS", "Successfully verified " + sDropdown + " values as " + expValues);
		}
	}
}
