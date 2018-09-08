package com.nri.timesheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Function;

public class DailyAutoUpdate {

	public static WebDriver driver = null;
	public static WebDriverWait wait = null;
	public static FluentWait<WebDriver> fwait = null;
	private static Properties reader;
	private static FileReader file;
	private static Map<String, String> attend;
	private static Map<String, String> time;
	private static String username;
	private static String password;
	private static String browser;
	private static String operaPath;
	private static String maxPageLoadIntervalInSec;
	private static String maxElementLoadIntervalInSec;
	private static final Logger logger = LoggerFactory.getLogger(DailyAutoUpdate.class);
	
	private DailyAutoUpdate() {
	};// Singleton Class
	
	public static void getscreenshot() {
		File image = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File dir = new File("screenshot");
		if(!dir.exists())
			dir.mkdirs();
		File dest = new File(dir.getAbsolutePath() + "/" + image.getName());
		image.renameTo(dest);
		logger.info("Screenshot image: " + dest.getAbsolutePath());
    }

	public static boolean fetchInfo() {
		try {
			file = new FileReader("Info.properties");
			reader = new Properties();
			reader.load(file);
			attend = new HashMap<>();
			time = new HashMap<>();
			username = reader.getProperty("Username");
			password = reader.getProperty("Password");
			attend.put("user", "UserID");
			attend.put("pass", "PWD");
			attend.put("loginURL", reader.getProperty("AttendanceLoginURL"));
			attend.put("dashboardURL", reader.getProperty("AttendanceDashboardURL"));
			attend.put("targetURL", reader.getProperty("AttendanceTargetURL"));
			attend.put("message", "Error");
			time.put("user", "username");
			time.put("pass", "password");
			time.put("loginURL", reader.getProperty("TimesheetLoginURL"));
			time.put("dashboardURL", reader.getProperty("TimesheetDashboardURL"));
			time.put("message", "Invalid");
			time.put("activity", reader.getProperty("Activity"));
			time.put("project", reader.getProperty("Project/TimesheetCode"));
			time.put("duration", reader.getProperty("Duration"));
			time.put("singleActivity", reader.getProperty("SingleActivity"));
			time.put("singleProject", reader.getProperty("SingleProject"));
			browser = reader.getProperty("Browser");
			operaPath = reader.getProperty("OperaPath");
			maxPageLoadIntervalInSec = reader.getProperty("MaxPageLoadIntervalInSec");
			maxElementLoadIntervalInSec = reader.getProperty("MaxElementLoadIntervalInSec");
			if (maxElementLoadIntervalInSec == null || maxElementLoadIntervalInSec.matches(".*[a-zA-Z]+.*"))
				throw new NullPointerException("maxElementLoadIntervalInSec not found/non parsable.");
			if (maxPageLoadIntervalInSec == null || maxPageLoadIntervalInSec.matches(".*[a-zA-Z]+.*"))
				throw new NullPointerException("maxPageLoadIntervalInSec not found/non parsable.");
			return true;
		} catch (FileNotFoundException e) {
			logger.info("File doesn't exist!\n" + e.getMessage());
		} catch (IOException e) {
			logger.info("Cound not load/parse file!\n" + e.getMessage());
		} catch (MissingResourceException | NullPointerException e) {
			logger.info("Resource not found\n" + e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static void getDriverInstance(String browser, String operaLauncherPath) throws IllegalStateException {
		if (driver == null)
			switch (browser) {
			case "chrome":
				System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver.exe");
				driver = new ChromeDriver();
				break;
			case "firefox":
				System.setProperty("webdriver.gecko.driver", "Drivers/geckodriver.exe");
				driver = new FirefoxDriver();
				break;
			case "edge":
				System.setProperty("webdriver.edge.driver", "Drivers/MicrosoftWebDriver.exe");
				driver = new EdgeDriver();
				break;
			case "explorer":
				System.setProperty("webdriver.ie.driver", "Drivers/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				break;
			case "opera":
				System.setProperty("webdriver.opera.driver", "Drivers/operadriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.setBinary(operaLauncherPath);
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				driver = new OperaDriver(capabilities);
				break;
			default:
				throw new WebDriverException("Unsupported Browser!");
			}
		driver.manage().timeouts().pageLoadTimeout(Long.parseLong(maxPageLoadIntervalInSec), TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, Long.parseLong(maxElementLoadIntervalInSec));
		fwait = new FluentWait<WebDriver>(driver).pollingEvery(300, TimeUnit.MILLISECONDS)
				.withTimeout(Long.parseLong(maxElementLoadIntervalInSec), TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
	}

	public void loginUser(Map<String, String> portal) throws NoSuchElementException {
		driver.findElement(By.name(portal.get("user"))).clear();
		driver.findElement(By.name(portal.get("user"))).sendKeys(username);
		driver.findElement(By.name(portal.get("pass"))).sendKeys("abc");
		driver.findElement(By.name(portal.get("pass"))).clear();
		driver.findElement(By.name(portal.get("pass"))).sendKeys(password + Keys.ENTER);
		try {
			fwait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					WebElement WebElement = null;
					try {
						WebElement = driver
								.findElement(By.xpath("//*[contains(text(),'" + portal.get("message") + "')]"));
						logger.info("Invalid Credentials or Network Problem!");
					} catch (NoSuchElementException e) {
						if (driver.getCurrentUrl().equals(portal.get("dashboardURL")))
							throw new WebDriverException();// Just to break out of wait...
						else
							throw e;
					}
					return WebElement;
				}
			});
		} catch (NoSuchElementException e) {
			throw new TimeoutException();
		} catch (WebDriverException e) {
			// Successfully logged in!!! Continue Rest...
			throw new TimeoutException();// This exception holds no meaning,just to continue rest of the part.
		}
	}

	public void isPresent(Map<String, String> portal) throws Exception {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Manage Attendance Records')]")));
		driver.findElement(By.xpath("//*[contains(text(),'Manage Attendance Records')]")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Report')]")));
		Actions action = new Actions(driver);		
        action.moveToElement(driver.findElement(By.xpath("//a[contains(text(),'Report')]"))).perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Attendance History')]")));
		driver.findElement(By.xpath("//a[contains(text(),'Attendance History')]")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("EndDate")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("t-no-data")));
		driver.findElement(By.id("StartDate")).clear();
		String today = driver.findElement(By.id("EndDate")).getAttribute("value");
		driver.findElement(By.id("StartDate")).sendKeys(today + Keys.ENTER);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(),'Total time span')]")));
		String status = driver.findElement(By.className("t-last")).getText();
		String signIn = driver.findElement(By.xpath("//td[contains(text(),'"+today+"')]/following::td")).getText();
		if (signIn.equals("")) {
			throw new Exception(status.equals("Absent") ? "User is not Present today!" : "Its a holiday!");
		}
		else if (!(status.equals("")||status.contains("Late"))) {
			logger.info("You are working really hard! Do NOT attempt this stunt on regular basis!!! :P");
		}
	}

	public void addEntry(Map<String, String> portal) throws NoSuchElementException, InterruptedException {
		driver.findElement(By.id("tmadd")).click();
		driver.findElement(By.cssSelector("tr:nth-child(2)")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("deleteBtn")));
		driver.findElement(By.className("hasDatepicker")).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Activity")));
		Select activityList = new Select(driver.findElement(By.name("Activity")));
		while (!activityList.getFirstSelectedOption().getText().equals("Service"))
			;
		activityList.selectByVisibleText(portal.get("singleActivity"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Project")));
		Select projectList = new Select(driver.findElement(By.name("Project")));
		try {
			fwait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.xpath("//option[text()='" + portal.get("singleProject") + "']"));
				}
			});
		} catch (TimeoutException e) {
			logger.info("Project List not updated yet or project not found!");
			throw e;
		}
		activityList.selectByVisibleText(portal.get("activity"));
		try {
			fwait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					WebElement WebElement = driver
							.findElement(By.xpath("//option[text()='" + portal.get("project") + "']"));
					projectList.selectByVisibleText(portal.get("project"));
					return WebElement;
				}
			});
		} catch (TimeoutException e) {
			logger.info("Project List not updated yet or project not found!");
			throw e;
		}
		driver.findElement(By.name("Hour")).sendKeys(portal.get("duration"));
		driver.findElement(By.id("tmsave")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("deleteBtn")));
	}

	public static void main(String[] args) throws InterruptedException {
		logger.info("Programmed By:- Abhishek Sarkar");		
		if (fetchInfo()) {
			DailyAutoUpdate update = new DailyAutoUpdate();
			try {
				getDriverInstance(browser, browser.equals("opera") ? operaPath : "");
				try {
					driver.get(attend.get("loginURL"));
					try {
						update.loginUser(attend);
						throw new Exception("");
					} catch (TimeoutException e) {
						try {
						update.isPresent(attend);
						} catch (WebDriverException we) {
							throw we;
						} catch (Exception we) {
							throw we;
						}
					}
					driver.get(time.get("loginURL"));
					try {
						update.loginUser(time);
					} catch (NoSuchElementException | TimeoutException e) {
						wait.until(ExpectedConditions.urlToBe(time.get("dashboardURL")));
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gbox_nriTimeSheet")));
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_nriTimeSheet")));
						update.addEntry(time);
						try {
							fwait.until(new Function<WebDriver, WebElement>() {
								public WebElement apply(WebDriver driver) {
									WebElement WebElement = null;
									try {
										WebElement = driver.findElement(By.xpath("//div[contains(text(),'saved')]"));
										logger.info("Entry Saved Successfully!");
									} catch (NoSuchElementException ne) {
										WebElement = driver.findElement(By.xpath("//div[contains(text(),'Existing')]"));
										logger.info("Entry Already Exists!");
									}
									return WebElement;
								}
							});
						} catch (NoSuchElementException ne) {
							throw new TimeoutException();
						}
					}
				} catch (NoSuchElementException e) {
					logger.info("Element Not Found!\n" + e.getMessage());
					getscreenshot();
				} catch (TimeoutException e) {
					logger.info("Server not Responded!\n" + e.getMessage());
					getscreenshot();
				} catch (NoSuchWindowException e) {
					logger.info("Browser got explicitly closed!\n" + e.getMessage());
				} catch (WebDriverException e) {
					logger.info("Browser Error!\n" + e.getMessage());
					getscreenshot();
					throw e;
				} catch (Exception e) {
					if(!e.getMessage().equals(""))
					logger.info(e.getMessage());
					getscreenshot();
				} finally {
					logger.info("Ready to Close!");
					driver.quit();
					logger.info("Task Completed!"
							+ (browser.equals("opera") ? "\nPlease close the browser manually!" : ""));
				}
			} catch (IllegalStateException e) {
				logger.info("Driver doesn't Exist!\n" + e.getMessage());
			} catch (SessionNotCreatedException e) {
				logger.info("Browser got explicitly closed or Inappropriate Driver!\n" + e.getMessage());
			} catch (WebDriverException e) {
				logger.info("Browser name/path is erroneous!\n" + e.getMessage());
			}
		}
	}
}