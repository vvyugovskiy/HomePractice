package utilities;

import org.openqa.selenium.WebElement;

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
}
