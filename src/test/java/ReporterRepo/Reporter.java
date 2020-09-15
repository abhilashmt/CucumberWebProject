package ReporterRepo;

import org.openqa.selenium.WebDriver;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import junit.framework.Assert;

public class Reporter{
	public static Scenario scenario;

	public static void report(String status, String sMessage) {
		if (status.contentEquals("FAIL")) {
//			if (!isbScenFail()) {
//				setbScenFail(true);
//				setbFeatureFail(true);
//			}
			Assert.fail(sMessage);
		}
		scenario.write(sMessage);
	}

}
