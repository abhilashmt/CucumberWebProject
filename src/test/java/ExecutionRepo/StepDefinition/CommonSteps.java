package ExecutionRepo.StepDefinition;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ExecutionRepo.Hooks;
import ObjectRepo.readOR;
import cucumber.api.java.en.When;

public class CommonSteps {
	static WebDriver driver;
	readOR objOR;
	static WaitStep wait;

	public CommonSteps() {
		driver = Hooks.driver;
		objOR = new readOR();
		wait = new WaitStep();
	}

	@When("user switch to frame \"([^\"]*)\"")
	public void switchToFrame(String sFrame) {
		List<WebElement> arrFrames = driver.findElements(By.tagName("iframe"));
		WebElement iFrame = null;
		if (arrFrames.size() != 0) {
			for (WebElement frame : arrFrames) {
				iFrame = frame;
				if (frame.getAttribute("id").contentEquals(sFrame)) {
					break;
				}
			}
			driver.switchTo().frame(sFrame);
		}
	}

	public void scrollToView(WebElement obj) throws Exception {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", obj);
		int x = obj.getLocation().getX();
		int y = obj.getLocation().getY();
		((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + ", " + y + ");");
		Thread.sleep(1000);
	}
	
	@When("user switch to new window")
	public void switch_to_window() throws Throwable {
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		}
	}
	@When("user close current window")
	public void close_current_window() throws Throwable {
		driver.close();
	}
}
