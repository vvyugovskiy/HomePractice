package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Driver {

    public static WebDriver driver;

    private Driver() {

    }

    public static WebDriver getDriver() {
        String browser=ConfigReader.getProperty("browser");
        if (driver == null) {
            browser = browser.toLowerCase();
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver();
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    return new FirefoxDriver();
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver(new ChromeOptions().setHeadless(true));
                case "firefox-headless":
                    WebDriverManager.firefoxdriver().setup();
                    return new FirefoxDriver(new FirefoxOptions().setHeadless(true));
            }
        }
        return null;
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

//    public static void main(String[] args) {
//        Driver.getDriver("chrome").findElement(By.id("sad")); // initially its null
//        Driver.getDriver("chrome").findElement(By.id("sadasdas")); // its not null
//    }
}
