package ExecutionRepo.StepDefinition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ExecutionRepo.Hooks;
import ReporterRepo.Reporter;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class LoginSteps {

	WebDriver driver;
	static ClickSteps clickObj;
	static WaitStep wait;
	static FillSteps fillObj;
	static VerifySteps verifyObj;

	public LoginSteps() {
		driver = Hooks.driver;
		clickObj = new ClickSteps();
		wait = new WaitStep();
		fillObj = new FillSteps();
		verifyObj = new VerifySteps();
	}

	public static void main(String[] args) throws Exception {

	}

	public static Map<String, String> getUserLoginData(String sRole) throws Exception {
		Properties prop = new Properties();
		Map<String, String> mapUserData = new HashMap<String, String>();
		FileReader fr = new FileReader(new File("src/test/resources/runConfig.properties"));
		prop.load(fr);
		String sEnv = prop.getProperty("Environment");
		String sLocation = prop.getProperty("Location");
		String sAppUrl = getApplicatinoUrl(sEnv);
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/ExecutionData/UserData.json")));
		JSONArray user = new JSONArray(data);
		boolean bFound = false;
		String sUserName = null, sPassword = null;
		for (Object obj : user) {
			JSONObject userObj = (JSONObject) obj;
			String jsEnv = userObj.get("Environment").toString();
			String jsRole = userObj.get("UserRole").toString();
			if ((jsEnv.toUpperCase().contentEquals(sEnv.toUpperCase()))
					&& (jsRole.toUpperCase().contentEquals(sRole.toUpperCase()))) {
				bFound = true;
				sUserName = userObj.get("UserName").toString();
				sPassword = userObj.get("Password").toString();
				break;
			}
		}
		if (!bFound) {
			Reporter.report("INFO", "Failed to find the user data correspond to Environment: " + sEnv + ", Location: "
					+ sLocation + ", Role: " + sRole + " in the UserData.json file");
			Reporter.report("FAIL", "");
		}
		mapUserData.put("URL", sAppUrl);
		mapUserData.put("Username", sUserName);
		mapUserData.put("Password", sPassword);
		return mapUserData;
	}

	private static String getApplicatinoUrl(String sEnv) throws Exception {
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/ExecutionData/ApplicationUrl.json")));
		JSONArray urlArray = new JSONArray(data);
		String appUrl = null;
		for (Object obj : urlArray) {
			JSONObject urlObj = (JSONObject) obj;
			String jsEnv = urlObj.get("Environment").toString();
			if (jsEnv.toUpperCase().contentEquals(sEnv.toUpperCase())) {
				appUrl = urlObj.get("Url").toString();
				break;
			}
		}
		if (appUrl == null) {
			Reporter.report("INFO", "Failed to find the application url for the environment " + sEnv
					+ " in the ApplicationUrl.json file");
			Reporter.report("FAIL", "");
		}
		return appUrl;
	}

	@Given("^user login to System with \"([^\"]*)\" role$")
	public void user_login_to_app_with_role(String sRole) throws Throwable {
		Map<String, String> userData = getUserLoginData(sRole);
		driver.get(userData.get("URL"));
		try {
			fillObj.FillTextbox("LoginPage", "Username_textbox", userData.get("Username"));
			fillObj.FillTextbox("LoginPage", "Password_textbox", userData.get("Password"));
			clickObj.ElementClick("LoginPage", "Submit_button");
			wait.dynamicWait();
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
	}

	@Given("user navigate to \"([^\"]*)\"")
	public void user_navigate_to(String sUrl) {
		try {
			driver.get(sUrl);
		} catch (Exception e) {
			Reporter.report("INFO", e.getMessage());
			Reporter.report("FAIL", "");
		}
	}

	@Given("user automate the link \"([^\"]*)\"")
	public void automate_link(String sUrl) {
		for (int i = 0; i < 700; i++) {
			try {
				driver.get(sUrl);
				try {
					wait.waitForElementDisplayed("/(//h3[@class='post-title entry-title'])[1]");
				} catch (Exception e) {

				}
				System.out.println(driver.getTitle());
			} catch (Exception e) {
				Reporter.report("INFO", e.getMessage());
				Reporter.report("FAIL", "");
			}
		}
	}
}
