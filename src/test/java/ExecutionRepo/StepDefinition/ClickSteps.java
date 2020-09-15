package ExecutionRepo.StepDefinition;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ExecutionRepo.Hooks;
import ObjectRepo.readOR;
import ReporterRepo.Reporter;
import cucumber.api.java.en.*;

public class ClickSteps {
	static WebDriver driver;
	readOR objOR;
	static WaitStep wait;
	static CommonSteps commonObj;

	public ClickSteps() {
		driver = Hooks.driver;
		objOR = new readOR();
		wait = new WaitStep();
		commonObj = new CommonSteps();
	}

	// Page element click
	public void ElementClick(String sPageName, String sControlName) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sControlName);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObject(sPageName, sControlName);
		commonObj.scrollToView(obj);
		try {
			obj.click();
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
		Thread.sleep(2000);
	}

	// Click an element inside a frame
	// Pass the frame id as 'sFrame'
	public void ElementClick(String sPageName, String sFrame, String sControlName) throws Throwable {
		String sXpath = objOR.getXpath(sPageName, sFrame, sControlName);
		wait.waitForElementDisplayed("//iframe[@id='" + sFrame + "']");
		commonObj.switchToFrame(sFrame);
		wait.waitForElementDisplayed(sXpath);
		WebElement obj = objOR.getObjectFromFrame(sPageName, sFrame, sControlName);
		try {
			obj.click();
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
		driver.switchTo().defaultContent();
		Thread.sleep(2000);
	}

	@When("^user perform \"([^\"]*)\" keyboard click$")
	public void user_perform_keybord_click(String sKey) throws Throwable {
		Keys sVal = null;
		switch (sKey.toUpperCase()) {
		case "ENTER":
			sVal = Keys.ENTER;
			break;
		case "TAB":
			sVal = Keys.TAB;
			break;
		case "PAGE_DOWN":
			sVal = Keys.PAGE_DOWN;
			break;
		case "BACKSPACE":
			sVal = Keys.BACK_SPACE;
			break;
		default:
			Reporter.report("INFO", "Invalid key type " + sKey);
			Reporter.report("FAIL", "");
		}
		Actions act = new Actions(driver);
		act.sendKeys(sVal).build().perform();
	}

	@When("^user try to mouse hover the control \"([^\"]*)\" in the page \"([^\"]*)\"$")
	public void user_try_to_mouse_hover_the_control_in_the_page(String sControlName, String sPageName)
			throws Throwable {
		WebElement oControl = objOR.getObject(sPageName, sControlName);
		Actions act = new Actions(driver);
		Thread.sleep(2000);
		act.moveToElement(oControl).build().perform();
	}

	@When("user click on \"([^\"]*)\" in \"([^\"]*)\" page")
	public void user_click_on_in_page(String sControl, String sPage) throws Throwable {
		ElementClick(sPage, sControl);
	}

	@When("user click on \"([^\"]*)\" in \"([^\"]*)\" frame of \"([^\"]*)\" page")
	public void user_click_on_in_frame(String sControl, String sFrame, String sPage) throws Throwable {
		ElementClick(sPage, sFrame, sControl);
	}

	@When("user click on the link with text \"([^\"]*)\"")
	public void user_click_on_in_link_text(String sText) throws Throwable {
		wait.waitForElementDisplayed("//*[contains(text(),'" + sText + "')]");
		Thread.sleep(1000);
		WebElement obj = driver.findElement(By.linkText(sText));
		obj.click();
		Thread.sleep(1000);
	}
}
