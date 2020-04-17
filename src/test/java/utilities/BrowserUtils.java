package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class BrowserUtils {

    public static void wait(int seconds) {

        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTextFromWebElements (List<WebElement> elements){
        List<String> textValue = new ArrayList<>();
        for (WebElement element :elements) {
            if(!element.getText().isEmpty()){
                textValue.add(element.getText());
            }
        }
        return textValue;
    }
    /**
     * waits for backgrounds processes on the browser to complete
     *
     * @param timeOutInSeconds
     */
    public static void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeOutInSeconds);
            wait.until(expectation);
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }
}
