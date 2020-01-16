/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Dipa.Kalbande
 */
class XPathClass {

    String searchBar = "//input[@id=\"twotabsearchtextbox\"]";
    String searchItem = "//span[text()=\"Apple iPhone XR (64GB) - Yellow\"]";
    String searchPrice = "//span[@id=\"priceblock_ourprice\"]";

    String flipSearchPrice = "//div[text()=\"Apple iPhone XR (Yellow, 64 GB)\"]/../../div[2]/div/div/div";
}

public class FigosoftPriceCompareTest extends XPathClass {

    String searchString = "iPhone XR (64GB) - Yellow";

    @Test
    public void FigosoftPriceCompareTest() throws InterruptedException {
        driver.get("https://www.amazon.in/");
        WebElement searchBarElement = driver.findElement(By.xpath(searchBar));
        searchBarElement.sendKeys(searchString, Keys.ENTER);
        driver.findElement(By.xpath(searchItem)).click();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        String amazonPrice = driver.findElement(By.xpath(searchPrice)).getText();
        Assert.assertTrue(amazonPrice != null && !amazonPrice.isEmpty());

        driver.get("https://www.flipkart.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            if (driver.findElement(By.xpath("//button[text() =\"✕\"]")).isDisplayed()) {
                driver.findElement(By.xpath("//button[text() =\"✕\"]")).click();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        driver.findElement(By.name("q")).sendKeys(searchString, Keys.ENTER);
        String flipkartPrice = driver.findElement(By.xpath(flipSearchPrice)).getText();

        Assert.assertTrue(flipkartPrice != null && !flipkartPrice.isEmpty());

        int amazonPriceInt = parseStringToInt(amazonPrice);
        int flipkartPriceInt = parseStringToInt(flipkartPrice);

        if (amazonPriceInt > flipkartPriceInt) {
            System.out.println(searchString + " is cheaper on Flipkart.");
        } else if (amazonPriceInt < flipkartPriceInt) {
            System.out.println(searchString + " is cheaper on Amazon.");
        } else {
            System.out.println(searchString + " is priced same on both Flipkart & Amazon.");
        }

    }

    public static int parseStringToInt(String s) {
        s = s.replaceAll("[^0-9.]", ""); //remove commas
        return (int) Math.round(Double.parseDouble(s)); //return rounded double cast to int
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    WebDriver driver;

    @BeforeMethod
    public void setUpMethod() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\Driver\\79\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        driver.close();
    }
}
