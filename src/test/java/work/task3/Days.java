package work.task3;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import work.utilities.BrowserUtils;
import work.utilities.DriverFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Days {


    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = DriverFactory.getDriver("chrome");
        driver.manage().window().maximize();
        BrowserUtils.wait(1);
    }

    @Test
    public void randomFridayTest() {
        driver.get("http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox");
        BrowserUtils.wait(1);


        int fridayCount = 0;
        while (fridayCount < 3) {
            Random rd = new Random();
            int rdN = rd.nextInt(5);
            WebElement chBoxDays = driver.findElement(By.xpath("(//*[@class='gwt-CheckBox'])[" + (rdN + 1) + "]"));
            chBoxDays.click();
            System.out.println("Selected checkbox: " + chBoxDays.getText());
            if (chBoxDays.getText().equals("Friday")) {
                fridayCount++;
            }
            chBoxDays.click();
        }
        Assert.assertTrue(fridayCount == 3);
    }

    @Test
    public void birthDateTest() {
        driver.get("http://practice.cybertekschool.com/dropdown");
        Select testYear = new Select(driver.findElement(By.id("year")));
        Select testMonth = new Select(driver.findElement(By.id("month")));
        Select testDay = new Select(driver.findElement(By.id("day")));
        String testDate = testYear.getFirstSelectedOption().getText() + ":" + testMonth.getFirstSelectedOption().getText() + ":" + testDay.getFirstSelectedOption().getText();
        System.out.println("Test Date = " + testDate);
        SimpleDateFormat date = new SimpleDateFormat("yyyy:MMMM:dd");
        Date currentDate = new Date();
        String actualDate = date.format(currentDate).toString();
        System.out.println("Actual Date: " + actualDate);

        Assert.assertEquals(testDate, actualDate, "Date mismatch");
    }

    @Test(description = "YEARS, MONTHS, DAYS")
    public void test3() {
        driver.get("http://practice.cybertekschool.com/dropdown");
        int tr = ThreadLocalRandom.current().nextInt(1921, 2020);
        Select testYear = new Select(driver.findElement(By.id("year")));
        testYear.selectByVisibleText("" + tr);
        String pickedYear = testYear.getFirstSelectedOption().getText();
        System.out.println(pickedYear);
        BrowserUtils.wait(1);

        for (int i = 0; i < 12; i++) {
            Select testMonth = new Select(driver.findElement(By.id("month")));
            testMonth.selectByIndex(i);
            Select testDays = new Select(driver.findElement(By.id("day")));
            List<WebElement> days = testDays.getOptions();
            ArrayList<Integer> lst = new ArrayList<>(Arrays.asList(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31));
            int year = Integer.parseInt(pickedYear);
            if ((year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))) {
                lst.set(1, 29);
            }
            ;
            System.out.println(lst);
            Assert.assertEquals((Integer) days.size(), lst.get(i), "mismatch");
        }
        BrowserUtils.wait(1);
    }

//    public static boolean isLeapYear(int year) {

//        if (year % 4 == 0) {
//            if (year % 100 == 0) {
//                return year % 400 == 0;
//            }
//        }
//        return year % 4 == 0;
//    }

    @Test(description = "DEPARTMENTS SORT")
    public void departmentSortTest() {
        driver.get("https://amazon.com");
        WebElement allW = driver.findElement(By.xpath("//select[@id='searchDropdownBox']//preceding-sibling::*/span"));
        String allExpected = allW.getText();

        Assert.assertEquals(allExpected, "All");
        Select allSelect = new Select(driver.findElement(By.id("searchDropdownBox"))); // xpath("//*[starts-with(@value,'search-alias')]")
        List<WebElement> allOptions = allSelect.getOptions();
        for (int i = 0; i < allOptions.size() - 1; i++) {
            String value = allOptions.get(i).getText();
            String nextValue = allOptions.get(i + 1).getText();
            System.out.println(value.compareTo(nextValue));
            Assert.assertTrue((value.compareTo(nextValue) <= 0));
            break;
        }
    }

    @Test
    public void mainDepartmentsTest() {
        driver.get("https://www.amazon.com/gp/site-directory");
        List<WebElement> allHeaders = driver.findElements(By.tagName("h2"));
        List<WebElement> allOptions = new Select(driver.findElement(By.id("searchDropdownBox"))).getOptions();

        Set<String> allHeadersSet = new HashSet<>();
        Set<String> allOptionsSet = new HashSet<>();

        for (WebElement each : allOptions) {
            allOptionsSet.add(each.getText());
        }
        for (WebElement each : allHeaders) {
            allHeadersSet.add(each.getText());
        }

        for (String each : allHeadersSet) {
            if (!allOptionsSet.contains(each)) {
                System.out.println(each);
            }
        }

        Assert.assertTrue(allOptionsSet.containsAll(allHeadersSet));
    }

    @Test
    public void links() {
        driver.get("https://www.w3schools.com/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("a tags on the page: " + links.size());
        int displayed = 0;
        for (WebElement each : links) {
            if (each.isDisplayed()) {
                System.out.println(each.getText());
                System.out.println(each.getAttribute("href"));
                displayed++;
            }
        }
        System.out.println("Elements with <a> tags isDisplayed: " + displayed);
    }

    @Test
    public void validLinks() {
        driver.get("https://www.selenium.dev/documentation/en/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        Iterator<WebElement> it = links.iterator();
        String url = "";
        while (it.hasNext()) {
            url = it.next().getAttribute("href");
            // verify link isValid
            if (!(url == null || url.isEmpty())) {

                try {
                    URL href = new URL(url);
                    HttpURLConnection httpC = (HttpURLConnection) href.openConnection();
                    httpC.setConnectTimeout(3000);
                    httpC.connect();

                    Assert.assertEquals(httpC.getResponseCode(), 200); // verify valid

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(url);
                continue;
            }
        }

    }

    @Test
    public void cart() {
        driver.get("https://amazon.com");
        WebElement searchWindow = driver.findElement(By.id("twotabsearchtextbox"));
        WebElement searchButton = driver.findElement(By.xpath("//*[@class='nav-search-submit nav-sprite']"));
        searchWindow.sendKeys("wooden spoon");
        searchButton.click();
        BrowserUtils.wait(2);
        List<WebElement> spoons = driver.findElements(By.xpath("//*[@cel_widget_id='SEARCH_RESULTS-SEARCH_RESULTS']")); //*[@class="a-section a-spacing-medium"]
       /* Aersilan Aji*/ List<WebElement> prices = spoons.stream().map(each -> each.findElement(By.xpath(".//span[@class='a-price-whole']"))).collect(Collectors.toList());

//       List<WebElement> prices = driver.findElements(By.xpath("//span[@class='a-price']//span[@class='a-offscreen']")); //*[@cel_widget_id="SEARCH_RESULTS-SEARCH_RESULTS"]//div//div//div//div//div[4]//div//div//span//span

        for (WebElement each : spoons) {
            System.out.println(each.getText());
        }
        System.out.println(spoons.size());
        System.out.println(prices.size());

        Random rd = new Random();
        int rdNumber = rd.nextInt(60);
        String spoonChosen = spoons.get(rdNumber).getText();
        String priceChoosen = prices.get(rdNumber).getText();
//        System.out.println(spoonChosen);
        System.out.println(priceChoosen);
//        String[] lst = spoonChosen.split(" ");
        BrowserUtils.wait(1);
        spoons.get(rdNumber).click();

        // [class="a-section a-spacing-medium"]
    }

    @AfterMethod
    public void teardown() {
        BrowserUtils.wait(5);
        driver.quit();
    }


}
