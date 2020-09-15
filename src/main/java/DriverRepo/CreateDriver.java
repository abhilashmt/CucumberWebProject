package DriverRepo;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

public class CreateDriver {
	public static WebDriver driver;
	private static String browser;
	private static String os;
	private static Properties prop = new Properties();
	//
	// private static InputStream in = WebDriverFactory.class
	// .getResourceAsStream("src/test/resources/runConfig.properties");
	private static CreateDriver instance = null;

	/******** Initialize Driver Configuration when the class is instanced ********/
	private CreateDriver() {
		CreateDriver.initConfig();
	}

	/**
	 * Singleton pattern
	 * 
	 * @return a single instance
	 */
	public static CreateDriver getInstance() {
		if (instance == null) {
			instance = new CreateDriver();
		}
		return instance;
	}

	public static void killInstance() {
		instance = null;
	}

	/**
	 * Get the Browser from the POM
	 */
	public static WebDriver initConfig() {

		try {
			FileReader reader = new FileReader("src/test/resources/runConfig.properties");
			prop.load(reader);
			browser = prop.getProperty("browser");
			os = prop.getProperty("os");
			// logLevel = prop.getProperty("logLevel");

		} catch (IOException e) {

		}

		/****** Load the driver *******/
		// driver=WebDriverFactory.getInstance().driver;
		driver = WebDriverFactory.createNewWebDriver(browser, os);

		/******** Clean Cookies, maxinize and declare Timeout on the Driver *******/
		if (!browser.equalsIgnoreCase("edge")) {
			driver.manage().deleteAllCookies();
		}
		driver.manage().window().maximize();
		return driver;
	}

}
