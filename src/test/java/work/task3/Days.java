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
import utilities.BrowserUtils;
import utilities.Driver;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Days {

    private WebDriver driver;
    private By primeCheckboxBy = By.xpath("//*[@id='p_85/2470955011']");
    private By searchFieldBy = By.id("twotabsearchtextbox");
    private By primeBy = By.xpath("(//i[@class='a-icon a-icon-checkbox'])[1]");
    private By brandsSideBy = By.xpath("//*[@id='brandsRefinements']//span[@class='a-size-base a-color-base']");
    private By firstItemInSearchBy = By.xpath("(//*[@aria-label='Amazon Prime']//preceding::span[@class='a-size-base-plus a-color-base a-text-normal'])[1]");

    @BeforeMethod
    public void setup() {
        driver = Driver.getDriver();
        driver.manage().window().maximize();
        BrowserUtils.wait(1);
    }

    @Test(description = "DAYS")
    public void test1() {
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

    @Test(description = "TODAY'S DATE")
    public void test2() {
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
            System.out.println(lst);
            Assert.assertEquals((Integer) days.size(), lst.get(i), "mismatch");
        }
        BrowserUtils.wait(1);
    }

    @Test(description = "DEPARTMENTS SORT")
    public void test4() {
        driver.get("https://amazon.com");
        WebElement allW = driver.findElement(By.xpath("//select[@id='searchDropdownBox']//preceding-sibling::*/span"));
        String allExpected = allW.getText();

        Assert.assertEquals(allExpected, "All");
        Select allSelect = new Select(driver.findElement(By.id("searchDropdownBox"))); // xpath("//*[starts-with(@value,'search-alias')]")
        List<WebElement> allOptions = allSelect.getOptions();
        for (int i = 0; i < allOptions.size() - 1; i++) {
            String value = allOptions.get(i).getText();
            String nextValue = allOptions.get(i + 1).getText();
//            System.out.println(value.compareTo(nextValue));
            Assert.assertTrue((value.compareTo(nextValue) <= 0));
            break;
        }
    }

    @Test(description = "MAIN DEPARTMENTS")
    public void test5() {
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

    @Test(description = "LINKS")
    public void test6() {
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


    @Test(description = "VALID LINKS")
    public void test7() {
        driver.get("https://www.selenium.dev/documentation/en/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("a occurrences " + links.size());

        Iterator<WebElement> it = links.iterator();
        String url = "";
        int validLinks = 0;
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
        System.out.println("Valid Links " + validLinks);
    }

    /**
     * 1. go to https://amazon.com
     * 2. search for "wooden spoon"
     * 3. click search
     * 4. remember the name and the price of a random result
     * 5. click on that random result
     * 6. verify default quantity of items is 1
     * 7. verify that the name and the price is the same as the one from step 5
     * 8. verify button "Add to Cart" is visible
     */

    @Test(description = "CART")
    public void test8() {
        driver.get("https://amazon.com");

        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon", Keys.ENTER);
        BrowserUtils.wait(3);
        List<WebElement> items = driver.findElements(By.cssSelector("[class='sg-col-inner']"));

        items.removeIf(p -> p.findElements(By.cssSelector("[aria-label='Amazon Prime']")).isEmpty()); // remove all non-prime items
        List<WebElement> prices = items.stream().map(p -> p.findElement(By.cssSelector("[class='a-price'] > [aria-hidden='true']"))).collect(Collectors.toList());
        List<WebElement> descriptions = items.stream().map(p -> p.findElement(By.cssSelector("[class='a-size-base-plus a-color-base a-text-normal']"))).collect(Collectors.toList());
        System.out.println("Number of prices: " + prices.size());
        System.out.println("Number of descriptions: " + descriptions.size());
        Random random = new Random();
        int randomNumber = random.nextInt(descriptions.size());
        prices.removeIf(p -> !p.isDisplayed()); //remove invisible items
        descriptions.removeIf(p -> !p.isDisplayed()); //remove invisible items

        //replace new line with .
        List<String> parsedPrices = BrowserUtils.getTextFromWebElements(prices).parallelStream().map(p -> p.replace("\n", ".")).collect(Collectors.toList());
        List<String> parsedDescriptions = BrowserUtils.getTextFromWebElements(prices);
        parsedDescriptions.removeIf(String::isEmpty);
        String expectedPrice = parsedPrices.get(randomNumber);
        WebElement randomItem = descriptions.get(randomNumber);
        String expectedDescription = randomItem.getText().trim();
        System.out.println("Prices: " + parsedPrices);
        System.out.println("Descriptions: " + BrowserUtils.getTextFromWebElements(descriptions));
        randomItem.click();//click on random item
        WebElement quantity = driver.findElement(By.xpath("//span[text()='Qty:']/following-sibling::span"));
        int actual = Integer.parseInt(quantity.getText().trim());
        Assert.assertEquals(actual, 1);
        WebElement productTitle = driver.findElement(By.id("productTitle"));
        WebElement productPrice = driver.findElement(By.cssSelector("[id='priceInsideBuyBox_feature_div'] > div"));
        String actualDescription = productTitle.getText().trim();
        String actualPrice = productPrice.getText().trim();
        Assert.assertEquals(actualDescription, expectedDescription);
        Assert.assertEquals(actualPrice, expectedPrice);
        driver.quit();
    }
//    @Test(description = "CART")
//    public void test8() {
//        driver.get("https://amazon.com");
//        WebElement searchWindow = driver.findElement(By.id("twotabsearchtextbox"));
//        WebElement searchButton = driver.findElement(By.xpath("//*[@class='nav-search-submit nav-sprite']"));
//        searchWindow.sendKeys("wooden spoon");
//        searchButton.click();
//        BrowserUtils.wait(2);
//        List<WebElement> spoons = driver.findElements(By.xpath("//*[@cel_widget_id='SEARCH_RESULTS-SEARCH_RESULTS']")); //*[@class="a-section a-spacing-medium"]
//        /* Aersilan Aji*/
//        List<WebElement> prices = spoons.stream().map(each -> each.findElement(By.xpath("//span[@class='a-price-whole']"))).collect(Collectors.toList());
//
////        List<WebElement> prices = driver.findElements(By.xpath("//span[@class='a-price']//span[@class='a-offscreen']")); //*[@cel_widget_id="SEARCH_RESULTS-SEARCH_RESULTS"]//div//div//div//div//div[4]//div//div//span//span
//
//        System.out.println(spoons.size());
//        System.out.println(prices.size());
//
//        Random rd = new Random();
//        int rdNumber = rd.nextInt(60);
//        String spoonChosen = spoons.get(rdNumber).getText();
//        String priceChosen = prices.get(rdNumber).getText();
//        System.out.println(priceChosen);
//        BrowserUtils.wait(1);
//        spoons.get(rdNumber).click();
//}

    /**
     * PRIME
     * 1.go to https://amazon.com
     * 2.search for "wooden spoon"
     * 3.click search
     * 4.remember name first result that has prime label
     * 5.select Prime checkbox on the left
     * 6.verify that name first result that has prime label is same as step 4
     * 7. check the last checkbox under Brand on the left
     * 8. verify that name first result that has prime label is different
     */

    @Test(description = "PRIME")
    public void test9() {
        driver.get("https://amazon.com");
        driver.findElement(searchFieldBy).sendKeys("wooden spoon", Keys.ENTER);
        driver.findElement(By.xpath("(//i[@class='a-icon a-icon-checkbox'])[1]")).click();

//        WebElement firstPrime = driver.findElement(By.xpath("//*[@aria-label='Amazon Prime']"));

        String firstPrimeSpoon = driver.findElement(firstItemInSearchBy).getText();
        System.out.println(firstPrimeSpoon);

        WebElement primeCheckbox = driver.findElement(primeCheckboxBy);
        primeCheckbox.click();

        String otherPrimeSpoon = driver.findElement(firstItemInSearchBy).getText();
        System.out.println(otherPrimeSpoon);

        Assert.assertEquals(firstPrimeSpoon, otherPrimeSpoon, "Items mismatch");

        List<WebElement> brands = driver.findElements(brandsSideBy);
        BrowserUtils.wait(2);
        int i = brands.size() - 1;
        System.out.println(brands.get(i).getText());
        brands.get(i).click();

        String newPrimeSpoon = driver.findElement(firstItemInSearchBy).getText();

        Assert.assertNotSame(firstPrimeSpoon, newPrimeSpoon, "Item mismatch");
    }

    /**
     * 1. go to https://amazon.com
     * 2. search for "wooden spoon"
     * 3. remember all Brand names on the left
     * 4. select Prime checkbox on the left
     * 5. verify that same Brand names are still displayed
     */
    @Test(description = "MORE SPOONS")
    public void test10() {
        driver.get("https://amazon.com");
        driver.findElement(searchFieldBy).sendKeys("wooden spoon", Keys.ENTER);

        List<WebElement> brands = driver.findElements(brandsSideBy);

        ArrayList<String> brandsList = new ArrayList<>();
        for (WebElement each : brands) {
            brandsList.add(each.getText());
        }
        System.out.println(brandsList);

        WebElement primeCheckbox = driver.findElement(primeBy);
        primeCheckbox.click();
        BrowserUtils.wait(2);
        System.out.println("=======================================================");

        List<WebElement> brandsAfterPrimeClicked = driver.findElements(brandsSideBy);

        ArrayList<String> brandsAfterPrimeClickedList = new ArrayList<>();
        for (WebElement each : brandsAfterPrimeClicked) {
            brandsAfterPrimeClickedList.add(each.getText());
        }
        System.out.println(brandsAfterPrimeClickedList);

        Assert.assertTrue(brandsList.equals(brandsAfterPrimeClickedList), "Brands do not match");

    }

    /**
     * 1. go to https://amazon.com
     * 2. search for "wooden spoon"
     * 3. click on Price option Under $25 on the left
     * 4. verify that all results are cheaper than $25
     */

    @Test(description = "CHEAP SPOONS")
    public void test11() {
        driver.get("https://amazon.com");
        driver.findElement(searchFieldBy).sendKeys("wooden spoon", Keys.ENTER);
        driver.findElement(By.linkText("Under $25")).click();

        List<WebElement> pricesBelow25 = driver.findElements(By.cssSelector("[class=a-price-whole]"));
        for (WebElement each : pricesBelow25) {
            System.out.println(each.getText());
            Assert.assertTrue(Integer.parseInt(each.getText()) < 25, "Wrong price");
        }
    }

    @AfterMethod
    public void teardown() {
        BrowserUtils.wait(3);
        driver.quit();
    }


}
