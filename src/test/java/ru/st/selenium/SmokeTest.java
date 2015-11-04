package ru.st.selenium;

import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.testng.*;
import org.testng.annotations.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SmokeTest extends TestBase {
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private WebDriverWait wait;
	
	@BeforeClass
	public void openHomePage() {
		wait = new WebDriverWait(driver, 10);
		driver.get(baseUrl + "/php4dvd/");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.name("submit")).click();
	}

	@Test(priority = 2)
	public void addCorrectDescription() throws Exception {
		driver.findElement(By.xpath("//img[@title='Add movie']")).click();
		driver.findElement(By.name("name")).sendKeys("Rocky");
		driver.findElement(By.name("year")).sendKeys("1976");
		driver.findElement(By.name("submit")).click();
		assertTrue(driver.findElement(By.xpath("//div[@class='maininfo_full']/h2")).getText().equals("Rocky (1976)"));
		driver.findElement(By.xpath("//a[contains(text(), 'Home')]")).click();
	}

	@Test(priority = 1)
	public void addIncorrectDescription() throws Exception {
		driver.findElement(By.xpath("//img[@title='Add movie']")).click();
		driver.findElement(By.name("name")).sendKeys("Rocky");
		driver.findElement(By.name("submit")).click();
		assertTrue(isElementPresent(By.cssSelector("div.addmovie")));
		assertTrue(driver.findElement(By.xpath("//label[@class='error'][@for='year']")).isDisplayed());
		driver.findElement(By.xpath("//a[contains(text(), 'Home')]")).click();
	}

	@Test(priority = 3)
	public void removeDescription() throws Exception {
		int before = driver.findElements(By.className("movie_box")).size();
		driver.findElement(By.cssSelector("div.movie_box")).click();
		driver.findElement(By.xpath("//img[@title='Remove']")).click();
		Alert alert = driver.switchTo().alert();
		assertTrue(alert.getText().equalsIgnoreCase("Are you sure you want to remove this?"));
		alert.accept();
		assertTrue(driver.findElements(By.className("movie_box")).size() == before - 1);
	}
	
	@Test(priority = 4)
	public void successfulSearch() {
		if (!isElementPresent(By.className("movie_box"))) {
			return;
		}
		List<WebElement> titles = driver.findElements(By.xpath("//div[@class='movie_box']/div[@class='title']"));
		String title = titles.get(titles.size()-1).getText();
		driver.findElement(By.id("q")).sendKeys(title + Keys.ENTER);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("movie_box")));
		assertTrue(isElementPresent(By.xpath("//div[@class='movie_box']/div[@class='title'][contains(text(), '" + title + "')]")));
		driver.findElement(By.id("q")).clear();
		driver.findElement(By.id("q")).sendKeys(Keys.ENTER);
	}
	
	@Test(priority = 5)
	public void noResultsSearch() {
		String query = "аааaaaaa";
		while (isTitlePresent(query)) {
			query += query;
		}
		driver.findElement(By.id("q")).sendKeys(query + Keys.ENTER);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='content'][text()='No movies where found.']")));
		driver.findElement(By.id("q")).clear();
		driver.findElement(By.id("q")).sendKeys(Keys.ENTER);
	}

	private boolean isTitlePresent(String tmp) {
		List<WebElement> titles = driver.findElements(By.xpath("//div[@class='movie_box']/div[@class='title']"));
		for (WebElement title : titles) {
			if (title.getText().equalsIgnoreCase(tmp)) {
				return true;
			}
		}
		return false;
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
