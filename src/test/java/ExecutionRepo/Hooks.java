package ExecutionRepo;

import java.net.MalformedURLException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import DriverRepo.CreateDriver;
import ReporterRepo.Reporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;

public class Hooks {

	public static WebDriver driver;
	public static Scenario scenario;
	
	@BeforeStep
	/**
	 * Close the unnecessary popup's
	 */
	public static void closeInvalidPopups() {
		//Add the required code
	}

	@Before
	public void before(Scenario scenario) {
		this.scenario = scenario;
		Reporter.scenario = scenario;

	}

	@Before
	/**
	 * Delete all cookies at the start of each scenario to avoid shared state
	 * between tests
	 */
	public static void initDriver() throws MalformedURLException, Exception {
		driver = CreateDriver.getInstance().driver;
	}

	@After
	/**
	 * Embed a screenshot in test report if test is marked as failed
	 */
	public void embedScreenshot(Scenario scenario) throws Exception {
		if (scenario.isFailed()) {
			try {
				byte[] scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				scenario.embed(scrFile, "image/png");
			} catch (WebDriverException somePlatformsDontSupportScreenshots) {
				System.err.println(somePlatformsDontSupportScreenshots.getMessage());
			}
		}
	}

}
