package ObjectRepo;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ExecutionRepo.Hooks;
import ReporterRepo.Reporter;

public class readOR {
	static WebDriver driver;
	static JavascriptExecutor executor;
	static Robot rb;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		JsonObject cntrlObject = readJson("LandingPage", "AppLandingPage", "NECRM");
		System.out.println(cntrlObject.get("ControlProperty").getAsString());
		// getActiveDriver();
		// getObject("Page1", "Control2");
	}

	public readOR() {
		driver = Hooks.driver;
	}

	// Get object from page
	public static WebElement getObject(String sPage, String sControl) throws Exception {
		WebElement obj;
		JsonObject cntrlObject = readJson(sPage, sControl);
		String sPropertyType, sProperty;
		sPropertyType = cntrlObject.get("PropertyType").getAsString();
		sProperty = cntrlObject.get("ControlProperty").getAsString();
		switch (sPropertyType.toLowerCase()) {
		case "xpath":
			obj = driver.findElement(By.xpath(sProperty));
			break;
		case "id":
			obj = driver.findElement(By.id(sProperty));
			break;
		case "name":
			obj = driver.findElement(By.name(sProperty));
			break;
		case "className":
			obj = driver.findElement(By.className(sProperty));
			break;
		default:
			obj = null;
		}
		return obj;
	}

	// Get object from frame
	public static WebElement getObjectFromFrame(String sPage, String sFrame, String sControl) throws Exception {
		WebElement obj;
		JsonObject cntrlObject = readJson(sPage, sFrame, sControl);
		String sPropertyType, sProperty;
		sPropertyType = cntrlObject.get("PropertyType").getAsString();
		sProperty = cntrlObject.get("ControlProperty").getAsString();
		switch (sPropertyType.toLowerCase()) {
		case "xpath":
			obj = driver.findElement(By.xpath(sProperty));
			break;
		case "id":
			obj = driver.findElement(By.id(sProperty));
			break;
		case "name":
			obj = driver.findElement(By.name(sProperty));
			break;
		case "className":
			obj = driver.findElement(By.className(sProperty));
			break;
		default:
			obj = null;
		}
		return obj;
	}

	public static JsonObject readJson(String sPage, String sControl) throws Exception {
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(new FileReader("src/test/java/ObjectRepo/OR.json"));
		JsonObject jsonObject = (JsonObject) obj;
		JsonObject pageObject = (JsonObject) jsonObject.get(sPage);
		JsonObject cntrlObject = (JsonObject) pageObject.get(sControl);
		return cntrlObject;
	}

	public static JsonObject readJson(String sPage, String sFrame, String sControl) throws Exception {
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(new FileReader("src/test/java/ObjectRepo/OR.json"));
		JsonObject jsonObject = (JsonObject) obj;
		JsonObject pageObject = (JsonObject) jsonObject.get(sPage);
		JsonObject frameObject = (JsonObject) pageObject.get(sFrame);
		JsonObject cntrlObject = (JsonObject) frameObject.get(sControl);
		return cntrlObject;
	}

	public static String getXpath(String sPage, String sControl) throws Exception {
		WebElement obj;
		JsonObject cntrlObject = readJson(sPage, sControl);
		String sPropertyType, sProperty = null;
		sPropertyType = cntrlObject.get("PropertyType").getAsString();
		if (sPropertyType.toUpperCase().contentEquals("XPATH")) {
			sProperty = cntrlObject.get("ControlProperty").getAsString();
		} else if (sPropertyType.toUpperCase().contentEquals("ID")) {
			sProperty = cntrlObject.get("ControlProperty").getAsString();
			sProperty = "//*[@id='" + sProperty + "']";
		} else {
			Reporter.report("INFO", "Invalid control property " + sPropertyType);
			Reporter.report("FAIL", "");
		}
		return sProperty;
	}

	public static String getXpath(String sPage, String sFrame, String sControl) throws Exception {
		WebElement obj;
		JsonObject cntrlObject = readJson(sPage, sFrame, sControl);
		String sPropertyType, sProperty = null;
		sPropertyType = cntrlObject.get("PropertyType").getAsString();
		if (sPropertyType.toUpperCase().contentEquals("XPATH")) {
			sProperty = cntrlObject.get("ControlProperty").getAsString();
		} else if (sPropertyType.toUpperCase().contentEquals("ID")) {
			sProperty = cntrlObject.get("ControlProperty").getAsString();
			sProperty = "//*[@id='" + sProperty + "']";
		} else {
			Reporter.report("INFO", "Invalid control property " + sPropertyType);
			Reporter.report("FAIL", "");
		}
		return sProperty;
	}

}
