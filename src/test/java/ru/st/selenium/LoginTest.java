package ru.st.selenium;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.testng.*;
import org.testng.annotations.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class LoginTest extends TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testLogin() throws Exception {
    driver.get(baseUrl + "/php4dvd/");
    WebElement usernameField = driver.findElement(By.id("username"));
    WebElement passwordField = driver.findElement(By.name("password"));
    WebElement button = driver.findElement(By.name("submit"));
	usernameField.clear();
    usernameField.sendKeys("admin1");
	passwordField.clear();
    passwordField.sendKeys("admin1");
	button.click();
    assertTrue(isElementPresent(By.xpath("//td[@class='error'][contains(text(), 'Incorrect user name or password')]")));
    usernameField.clear();
    usernameField.sendKeys("admin");
    passwordField.clear();
    passwordField.sendKeys("admin");
    button.click();
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

