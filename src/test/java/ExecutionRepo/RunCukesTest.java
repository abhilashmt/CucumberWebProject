package ExecutionRepo;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import DriverRepo.CreateDriver;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
//		tags="@Regression",
		monochrome = true,
		plugin = { "pretty", "json:target/result/cucumber.json",
				"html:target/result/cucumber-reports" },
		features = "src/test/resources/features/Testing.feature"
//		features = "classpath:features"
)

public class RunCukesTest {
	@AfterClass
	public static void testMethod() {
		System.out.println("after class");
		CreateDriver.killInstance();
		Hooks.driver.quit();
		Hooks.driver = null;
	}
}
