package com.newcrawler.plugin.test;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Selenium2ChromeRemoteDriverTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		WebDriver driver = null;
		try {
			System.setProperty("webdriver.chrome.driver", "/Users/liaolianwu/Documents/workspace/newcrawler-plugin-urlfetch-chrome/driver/chromedriver_mac64/chromedriver");
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setPlatform(Platform.LINUX);
            //capabilities.setVersion("53.0.2785.143");
			driver = new RemoteWebDriver(new URL("http://192.168.45.21:5555/wd/hub"),  capabilities);

			driver.navigate().to("http://www.newcrawler.com/header");

			String content = driver.getPageSource();
			
			System.out.println("Page source: " + content);
			System.out.println("getCurrentUrl: " + driver.getCurrentUrl());
		} finally {
			// Close the browser
			if (driver != null) {
				driver.quit();
			}
		}
	}

}
