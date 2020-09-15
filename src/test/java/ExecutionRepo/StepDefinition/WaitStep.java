package ExecutionRepo.StepDefinition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ExecutionRepo.Hooks;
import ObjectRepo.readOR;

public class WaitStep {
	static WebDriver driver;
	static readOR objOR;
	static int maxWait = 5;

	public WaitStep() {
		driver = Hooks.driver;
		objOR = new readOR();
		Properties prop = new Properties();
		FileReader fr;
		try {
			fr = new FileReader(new File("src/test/resources/runConfig.properties"));
			prop.load(fr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxWait = Integer.parseInt(prop.getProperty("MaxWaitTime"));
	}

	public static void dynamicWait() throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, maxWait);
		WebDriverWait lessWt = new WebDriverWait(driver, 10);
		try {
			lessWt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='app-loading-indicator']")));
			Thread.sleep(1000);
			wt.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='app-loading-indicator']")));
			Thread.sleep(1000);
		} catch (TimeoutException te) {
			if (!te.getMessage().contains("waiting for visibility of element")) {
				System.out.println(te.getMessage());
			}
		}
	}

	public static void waitForElementDisplayed(WebElement elm) throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, maxWait);
		try {
			wt.until(ExpectedConditions.visibilityOf(elm));
		} catch (TimeoutException te) {
			dynamicWait();
		}
		wt.until(ExpectedConditions.visibilityOf(elm));
	}

	public static void waitForElementDisplayed(String sXpath) throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, maxWait);
		try {
			wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sXpath)));
		} catch (TimeoutException te) {
			dynamicWait();
		}
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sXpath)));
	}

	public static void waitForElementClickable(String sXpath) throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, maxWait);
		try {
			wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sXpath)));
		} catch (TimeoutException te) {
			dynamicWait();
		}
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(sXpath)));
	}

	public static void waitForElementClickable(WebElement obj) throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, maxWait);
		try {
			wt.until(ExpectedConditions.elementToBeClickable(obj));
		} catch (TimeoutException te) {
			dynamicWait();
		}
		wt.until(ExpectedConditions.elementToBeClickable(obj));
	}

	public static void waitForElementDisappear(String sXpath) throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, maxWait);
		try {
			wt.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(sXpath)));
		} catch (TimeoutException te) {
			dynamicWait();
		}
		wt.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(sXpath)));
	}

	public static void waitForElementDisappear(String sXpath, int iTime) throws Exception {
		WebDriverWait wt = new WebDriverWait(driver, iTime);
		try {
			wt.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(sXpath)));
		} catch (TimeoutException te) {
			dynamicWait();
		}
		wt.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(sXpath)));
	}
}
