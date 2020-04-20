package work.task5;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BrowserUtils;
import utilities.ConfigReader;
import utilities.Driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tests {

    protected WebDriver driver;
    protected Actions actions;
    private By usernameBy = By.id("prependedInput");
    private By passwordBy = By.id("prependedInput2");
    private By logInBy = By.id("_submit");
    private By activitiesBy = By.xpath("//span[@class='title title-level-1' and contains(text(),'Activities')]");
    private By toCalendarEventBy = By.linkText("Calendar Events");
    private By createCalendarEventBy = By.cssSelector("[title='Create Calendar event']");
    private By repeatCheckboxBy = By.cssSelector("[id^='recurrence-repeat-view']");
    private By testersMeetingBy = By.xpath("(//td[text()='Testers Meeting'])[1]//../td[9]");

    /**
     * 1. Go to “https://qa1.vytrack.com/"
     * 2. Login as a store manager
     * 3. Navigate to “Activities -> Calendar Events”
     * 4. Hover on three dots “...” for “Testers meeting” calendar event
     */

    @Test(description = "Test Case #1")
    public void test1() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(3);
        WebElement testersMeeting = driver.findElement(testersMeetingBy);

        actions.moveToElement(testersMeeting).pause(2000).perform();
        WebElement testersMeetingView = driver.findElement(By.cssSelector("[href='/calendar/event/view/1846']"));
        WebElement testersMeetingEdit = driver.findElement(By.cssSelector("[href='/calendar/event/update/1846']"));
        WebElement testersMeetingDelete = driver.findElement(By.xpath("//*[@href='/calendar/event/update/1846']/following::li[1]/a"));

        testersMeetingDelete.click();

        BrowserUtils.wait(2);

        driver.findElement(By.cssSelector("[class='btn cancel']")).click();

        Assert.assertTrue(testersMeetingView.isEnabled(),"View option is not active");
        Assert.assertTrue(testersMeetingEdit.isEnabled(),"Edit option is not active");
        Assert.assertTrue(testersMeetingDelete.isEnabled(),"Delete option is not active");
//
//        System.out.println("Testers Meeting View option is available: " + testersMeetingView.isEnabled());
//        System.out.println("Testers Meeting Edit option is available: " + testersMeetingEdit.isEnabled());
//        System.out.println("Testers Meeting Delete option is available: " + testersMeetingDelete.isEnabled());


    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”4.Click on “Grid Options” button
     * 5.Deselect all options except “Title”
     * 6.Verify that “Title” column still displayed
     */
    @Test(description = "Test Case #2")
    public void test2() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(3);
        driver.findElement(By.cssSelector("[title='Grid Settings']")).click();
        List<WebElement> gridOptions = driver.findElements(By.xpath("(//tbody)[1]//tr//label"));

        for (WebElement each : gridOptions) {
            if (!each.getText().equals("Title")) {
                System.out.println(each.getText());
                each.click();
            }
        }
        String expected = "TITLE";
        String actual = driver.findElement(By.xpath("(//*[starts-with(@id,'grid-calendar-event-grid')]/..//span[@class='grid-header-cell__label'])[1]")).getText();

        Assert.assertEquals(actual, expected, "Title is missing");
    }

    /**
     * Test Case #3
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Expand “Save And Close” menu
     * 6.Verify that “Save And Close”, “Save And New” and “Save” options are available
     */
    @Test(description = "Test Case 3")
    public void test3() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(By.cssSelector("[class='btn-success btn dropdown-toggle']")).click();

        String actualSaveAndClose = driver.findElement(By.cssSelector("[class='action-button dropdown-item']")).getText();
        Assert.assertEquals(actualSaveAndClose, "Save And Close", "\"Save And Close\" is missing");

        String actualSaveAndNew = driver.findElement(By.xpath("//*[@class='main-group action-button dropdown-item' and contains(text(),'Save and New')]")).getText();
        Assert.assertEquals(actualSaveAndNew, "Save And New", "\"Save And New\" is missing");

        String actualSave = driver.findElement(By.xpath("(//*[@class='main-group action-button dropdown-item'])[2]")).getText();
        Assert.assertEquals(actualSave, "Save", "\"Save\" is missing");

    }

    /**
     * Test Case #4
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Then, click on “Cancel” button
     * 6.Verify that “All Calendar Events” page subtitle is displayed
     */
    @Test(description = "Test Case 4")
    public void test4() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(By.cssSelector("[title='Cancel']")).click();
        BrowserUtils.wait(2);

        String actual = driver.findElement(By.cssSelector("[class='oro-subtitle']")).getText();
        Assert.assertEquals(actual, "All Calendar Events", "Subtitle is NOT displayed");
    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Verify that difference between end and start time is exactly 1 hour
     */
    @Test(description = "Test Case 5")
    public void test5() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);

        String startTime = driver.findElement(By.cssSelector("[id^='time_selector_oro_calendar_event_form_start']")).getAttribute("value").split(":")[0];
        String endTime = driver.findElement(By.cssSelector("[id^='time_selector_oro_calendar_event_form_end']")).getAttribute("value").split(":")[0];

        int startTimeInt = Integer.parseInt(startTime);
        if (startTimeInt == 12) {
            startTimeInt = 0;
        }
        int endTimeInt = Integer.parseInt(endTime);

        Assert.assertEquals(startTimeInt + 1, endTimeInt, "Time mismatch");
    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “9:00 PM” as a start time
     * 6.Verify that end time is equals to “10:00 PM”
     */
    @Test(description = "Test Case 6")
    public void test6() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);

        WebElement dropdown = driver.findElement(By.cssSelector("[id^='time_selector_oro_calendar_event_form_start']"));
        dropdown.click();
        dropdown.findElement(By.xpath("//*[contains(text(),'9:00 PM')]")).click();
        BrowserUtils.wait(2);

        int intActualEndTime = Integer.parseInt(driver.findElement(By.cssSelector("[id^='time_selector_oro_calendar_event_form_end']")).getAttribute("value").split(":")[0]);

        Assert.assertEquals(intActualEndTime - 1, 9, "Time mismatch");
    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “All-Day Event” checkbox
     * 6.Verify that “All-Day Event” checkbox is selected
     * 7.Verify that start and end time input boxes are not displayed
     * 8.Verify that start and end date input boxes are displayed
     */
    @Test(description = "Test Case 7")
    public void test7() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        WebElement allDayEventCheckbox = driver.findElement(By.cssSelector("[id^='oro_calendar_event_form_allDay-uid']"));
        allDayEventCheckbox.click();
        Assert.assertTrue(allDayEventCheckbox.isSelected(), "All-Day Event Checkbox is Not selected");

        WebElement startTimeInput = driver.findElement(By.cssSelector("[id^='time_selector_oro_calendar_event_form_start']"));
        WebElement endTimeInput = driver.findElement(By.cssSelector("[id^='time_selector_oro_calendar_event_form_end']"));
        WebElement startDateInput = driver.findElement(By.cssSelector("[id^='date_selector_oro_calendar_event_form_start']"));
        WebElement endDateInput = driver.findElement(By.cssSelector("[id^='date_selector_oro_calendar_event_form_end']"));

        Assert.assertTrue(startTimeInput.isDisplayed(), "Start Time Input Box Is Displayed");
        Assert.assertTrue(endTimeInput.isDisplayed(), "End Time Input Box Is Displayed");

        Assert.assertTrue(startDateInput.isDisplayed(), "Start Date Input Box Is NOT Displayed");
        Assert.assertTrue(endDateInput.isDisplayed(), "End Date Input Box Is NOT Displayed");

    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “Repeat” checkbox
     * 6.Verify that “Repeat” checkbox is selected
     * 7.Verify that “Daily” is selected by default and following options are available in “Repeats” drop-down:
     */

    @Test(description = "Test Case 8")
    public void test8() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        WebElement repeatCheckbox = driver.findElement(repeatCheckboxBy);
        repeatCheckbox.click();
        BrowserUtils.wait(2);

        Assert.assertTrue(repeatCheckbox.isSelected(), "Repeat Checkbox is not Selected");

        Select select = new Select(driver.findElement(By.cssSelector("[id^='recurrence-repeats-view']")));

        Assert.assertEquals(select.getFirstSelectedOption().getText(), "Daily", "\"Daily\" is missing");

        List<String> expectedOptions = new ArrayList<>(Arrays.asList("Daily", "Weekly", "Monthly", "Yearly"));
        List<String> actualOptions = new ArrayList<>();
        for (WebElement each : select.getOptions()) {
            String option = each.getText();
            actualOptions.add(option);
        }
        Assert.assertEquals(actualOptions, expectedOptions, "Mismatch");
    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “Repeat” checkbox
     * 6.Verify that “Repeat” checkbox is selected
     * 7.Verify that “Repeat Every” radio button is selected
     * 8.Verify that “Never” radio button is selected as an “Ends” option.
     * 9.Verify that following message as a summary is displayed: “Summary: Daily every 1 day”
     */
    @Test(description = "Test Case 9")
    public void test9() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        WebElement repeatCheckbox = driver.findElement(repeatCheckboxBy);
        repeatCheckbox.click();
        BrowserUtils.wait(2);
        Assert.assertTrue(repeatCheckbox.isSelected(), "Repeat Checkbox is not Selected");

        WebElement repeatEveryCheckbox = driver.findElement(By.xpath("(//input[@type='radio'])[1]"));
        Assert.assertTrue(repeatEveryCheckbox.isSelected(), "\"Repeats Every\" Radio Button is NOT Selected");

        WebElement endsNeverCheckbox = driver.findElement(By.xpath("//*[@class='recurrence-subview-control__text' and contains(text(),'Never')]/preceding-sibling::input"));
        Assert.assertTrue(endsNeverCheckbox.isSelected(), "Ends \"Never\" Radio Button is NOT Selected ");
//        WebElement endsNeverCheckbox2 = driver.findElement(By.xpath("//*[@class='control-label wrap']/following::label[text()='Ends']/following::input[@type='radio']"));
//        Assert.assertTrue(endsNeverCheckbox2.isSelected(), "Ends \"Never\" Radio Button is NOT Selected ");

        String actualMessage = "Summary: " + driver.findElement(By.xpath("//span[contains(text(),'Daily every 1 day')]")).getText();

        Assert.assertEquals(actualMessage, "Summary: Daily every 1 day", "Message is not displayed");
    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “Repeat” checkbox
     * 6.Select “After 10 occurrences” as an “Ends” option.
     * 7.Verify that following message as a summary is displayed: “Summary: Daily every 1 day, end after 10 occurrences”
     */
    @Test(description = "Test Case 10")
    public void test10() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        WebElement repeatCheckbox = driver.findElement(repeatCheckboxBy);
        repeatCheckbox.click();

        WebElement endsAfterCheckbox = driver.findElement(By.xpath("//*[@class='recurrence-subview-control__text' and contains(text(),'After')]/preceding-sibling::input"));
        endsAfterCheckbox.click();
        driver.findElement(By.cssSelector("input[data-related-field='occurrences']")).sendKeys("10", Keys.ENTER);
        BrowserUtils.wait(2);

        String expectedMessage = "Summary: Daily every 1 day, end after 10 occurrences";
        String actualMessage = driver.findElement(By.xpath("//*[contains(text(),'Summary:')]")).getText() + " "
                + driver.findElement(By.xpath("//span[contains(text(),'Daily every 1 day')]")).getText()
                + driver.findElement(By.xpath("//span[contains(text(),', end after 10 occurrences')]")).getText();
        Assert.assertEquals(actualMessage, expectedMessage, "Message mismatch");
    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “Repeat” checkbox
     * 6.Select “By Nov 18, 2021” as an “Ends” option.
     * 7.Verify that following message as a summary is displayed: “Summary: Daily every 1 day, end byNov 18, 2021”
     */
    @Test(description = "Test Case 11")
    public void test11() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        WebElement repeatCheckbox = driver.findElement(repeatCheckboxBy);
        repeatCheckbox.click();
        WebElement endsByCheckbox = driver.findElement(By.xpath("//*[@class='recurrence-subview-control__text' and contains(text(),'By')]/preceding-sibling::input"));
        endsByCheckbox.click();
        driver.findElement(By.cssSelector("[class='datepicker-input hasDatepicker']")).sendKeys("Nov 18, 2021", Keys.ENTER);
        BrowserUtils.wait(2);

        String expectedMessage = "Summary: Daily every 1 day, end by Nov 18, 2021";
        String actualMessage = driver.findElement(By.xpath("//*[contains(text(),'Summary:')]")).getText() + " "
                + driver.findElement(By.xpath("//span[contains(text(),'Daily every 1 day')]")).getText()
                + driver.findElement(By.xpath("//span[contains(text(),', end by Nov 18, 2021')]")).getText();

        Assert.assertEquals(actualMessage, expectedMessage, "Message mismatch");

    }

    /**
     * 1.Go to “https://qa1.vytrack.com/"
     * 2.Login as a store manager
     * 3.Navigate to “Activities -> Calendar Events”
     * 4.Click on “Create Calendar Event” button
     * 5.Select “Repeat” checkbox
     * 6.Select “Weekly” options as a “Repeat” option
     * 7.Select “Monday and Friday” options as a “Repeat On” options
     * 8.Verify that “Monday and Friday” options are selected
     * 9.Verify that following message as a summary is displayed: “Summary: Weekly every 1 week onMonday, Friday”
     */
    @Test(description = "Test Case 12")
    public void test12() {
        actions.moveToElement(driver.findElement(activitiesBy)).perform();
        BrowserUtils.wait(3);
        driver.findElement(toCalendarEventBy).click();
        BrowserUtils.wait(2);
        driver.findElement(createCalendarEventBy).click();
        BrowserUtils.wait(2);
        WebElement repeatCheckbox = driver.findElement(repeatCheckboxBy);
        repeatCheckbox.click();

        Select select = new Select(driver.findElement(By.cssSelector("[id^='recurrence-repeats-view']")));
        select.selectByVisibleText("Weekly");

//        driver.findElement(By.xpath("//*[@class='multi-checkbox-control_text' and contains(text(),'M')]/preceding-sibling::input")).click();
//        driver.findElement(By.xpath("//*[@class='multi-checkbox-control_text' and contains(text(),'F')]/preceding-sibling::input")).click();
        driver.findElement(By.cssSelector("input[value='monday']")).click();
        driver.findElement(By.cssSelector("input[value='friday']")).click();
        BrowserUtils.wait(3);

        String expectedMessage = "Summary: Weekly every 1 week on Monday, Friday";

        String actualMessage = driver.findElement(By.xpath("//*[contains(text(),'Summary:')]")).getText() + " "
                + driver.findElement(By.xpath("//span[contains(text(),'Weekly every 1 week on Monday, Friday')]")).getText();

        Assert.assertEquals(actualMessage, expectedMessage, "Message mismatch");

    }

    @BeforeMethod
    public void setup() {
        driver = Driver.getDriver();
        driver.get(ConfigReader.getProperty("qa1"));
        driver.manage().window().maximize();

        actions = new Actions(driver);

        BrowserUtils.wait(2);
        driver.findElement(usernameBy).sendKeys(ConfigReader.getProperty("username"));
        driver.findElement(passwordBy).sendKeys(ConfigReader.getProperty("password"));
        BrowserUtils.wait(2);
        driver.findElement(logInBy).click();
        BrowserUtils.wait(2);
    }

    @AfterMethod
    public void teardown() {
        BrowserUtils.wait(2);
        driver.quit();
    }
}
