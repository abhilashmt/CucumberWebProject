package DriverRepo;

import java.util.HashMap;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class WebDriverFactory {
	static WebDriver driver;
	static String resourceFolder = "resources/files/software/";
	private static WebDriverFactory instance = null;

	private WebDriverFactory() {
		// driver = createNewWebDriver("chrome", "windows");
	}

	/**
	 * Singleton pattern
	 * 
	 * @return a single instance
	 */
	public static WebDriverFactory getInstance() {
		if (instance == null) {
			instance = new WebDriverFactory();
		}
		return instance;
	}

	public static WebDriver createNewWebDriver(String browser, String os) {

		/******** The driver selected is Local: Firefox ********/
		if ("FIREFOX".equalsIgnoreCase(browser)) {
			if ("WINDOWS".equalsIgnoreCase(os)) {
				System.setProperty("webdriver.gecko.driver", "src/main/java/WindowsDrivers/geckodriver.exe");
			} else {
				System.setProperty("webdriver.gecko.driver", "src/main/java/WindowsDrivers/geckodriver");
			}
			driver = new FirefoxDriver();
		}

		/******** The driver selected is Chrome ********/

		else if ("CHROME".equalsIgnoreCase(browser)) {
			if ("WINDOWS".equalsIgnoreCase(os)) {
				System.setProperty("webdriver.chrome.driver", "src/main/java/WindowsDrivers/chromedriver.exe");

			} else {
				System.setProperty("webdriver.chrome.driver", "src/main/java/WindowsDrivers/chromedriver");
			}
			ChromeOptions options = new ChromeOptions();
			options.setAcceptInsecureCerts(true);
			options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
			HashMap<String, Object> chromePref = new HashMap<>();
			chromePref.put("download.default_directory", System.getProperty("java.io.tmpdir"));
			options.setExperimentalOption("prefs", chromePref);
			// Added by Adam Please do not remove -- for chromedirver 2.46^
			options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
			options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
			// options.addArguments("--headless"); // only if you are ACTUALLY running
			// headless
			options.addArguments("--no-sandbox"); // https://stackoverflow.com/a/50725918/1689770
			options.addArguments("--disable-infobars"); // https://stackoverflow.com/a/43840128/1689770
			options.addArguments("--disable-dev-shm-usage"); // https://stackoverflow.com/a/50725918/1689770
			options.addArguments("--disable-browser-side-navigation"); // https://stackoverflow.com/a/49123152/1689770
			options.addArguments("--disable-gpu"); // https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-ex
			// End Added by Adam
			driver = new ChromeDriver(options);
			// driver = new ChromeDriver(capabilities);
		}

		/******** The driver selected is Internet Explorer ********/
		else if ("IE".equalsIgnoreCase(browser)) {
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

			// Settings to Accept the SSL Certificate in the Capability object
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			System.setProperty("webdriver.ie.driver", "src/main/java/WindowsDrivers/IEDriverServer.exe");

			driver = new InternetExplorerDriver(capabilities);

		} else if ("EDGE".equalsIgnoreCase(browser)) {
			System.setProperty("webdriver.edge.driver", "src/main/java/WindowsDrivers/EdgeDriver.exe");
			DesiredCapabilities capabilities = DesiredCapabilities.edge();
			driver = new EdgeDriver(capabilities);
		}
		else if ("HEADLESS".equalsIgnoreCase(browser)) {
			driver = new HtmlUnitDriver();
		}
		/******** The driver is not selected ********/
		else {
			return null;
		}

		return driver;
	}
}
