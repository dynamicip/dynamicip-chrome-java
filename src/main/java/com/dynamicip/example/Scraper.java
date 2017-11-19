package com.dynamicip.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class Scraper {
  public static void main(String[] args) throws Exception {
    System.out.println("Running web scraper...");

    ChromeDriver driver = createDriver();
    try {
      // Load a javascript-enabled page.
      driver.navigate().to("https://examples.dynamicip.com/single-page-apps/basic");

      // Wait for javascript. This particular solution is specific to jQuery.
      waitForJQuery(driver);

      // Extract the DOM.
      String renderedHTML = (String)driver.executeScript("return document.documentElement.outerHTML");

      // Display the result.
      System.out.println("Page response:");
      System.out.println(renderedHTML);
    }
    finally {
      driver.quit();
    }
  }

  private static ChromeDriver createDriver() throws Exception {
    ChromeOptions options = new ChromeOptions();

    // Configure Chrome to use DynamicIP as a proxy.
    options.addArguments("--proxy-server=https://dynamicip.com:443");

    // Perform proxy authentication via a custom plugin (see 'resources/chrome_extension' dir).
    options.addArguments(String.format("--load-extension=%s", getJarDirectory().resolve("chrome_extension").toAbsolutePath().toString()));

    // ChromeDriver's default behaviour is to allow invalid certificates.
    // To improve security, we explicitly unset this flag here.
    options.setExperimentalOption("excludeSwitches", Arrays.asList("ignore-certificate-errors"));

    return new ChromeDriver(options);
  }

  private static void waitForJQuery(JavascriptExecutor driver) throws Exception {
    try {
      Integer jsTimeoutSecs = 30;
      for (Integer i = 0; i < jsTimeoutSecs; i++) {
        Boolean isJQueryComplete = (Boolean)driver.executeScript("return jQuery.active === 0");
        if (isJQueryComplete) return;
        Thread.sleep(1000);
      }
    }
    catch (Exception ex) {
      throw new Exception("Failed to load page. Problem may be temporary - please try again.", ex);
    }
  }

  private static Path getJarDirectory() throws Exception {
    return new File(Scraper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toPath().getParent();
  }
}
