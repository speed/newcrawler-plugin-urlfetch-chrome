package com.newcrawler.plugin.test;

import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.sun.jndi.ldap.Connection;

public class Selenium2ChromeTest  {
	
    public static void main(String[] args) throws IOException {
    	
    	RemoteWebDriver driver=null;
    	try{
    		
    		//System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
    		System.setProperty("webdriver.chrome.driver", "http://192.168.45.21:5555/wd/hub");
    		System.setProperty("webdriver.chrome.driver", "/Users/liaolianwu/Documents/workspace/newcrawler-plugin-urlfetch-chrome/driver/chromedriver_mac64/chromedriver");
    		
        	HashMap<String, Object> settings = new HashMap<String, Object>(); 
        	settings.put("images", 2); //disabled load images
            

            HashMap<String, Object> prefs = new HashMap<String, Object>(); 
            prefs.put("profile.managed_default_content_settings", settings); 
            

            ChromeOptions options =new ChromeOptions(); 
            options.setExperimentalOption("prefs", prefs); 
            options.addExtensions(new File("/Users/liaolianwu/Documents/workspace/newcrawler-plugin-urlfetch-chrome/crx/ModHeader.crx"));
            
            
            
            DesiredCapabilities chromeCaps = DesiredCapabilities.chrome(); 
            chromeCaps.setCapability(ChromeOptions.CAPABILITY, options); 
            

            Proxy proxy = new Proxy();
        	proxy.setHttpProxy("192.168.44.244:8888");
        	proxy.setSslProxy("192.168.44.244:8888");

        	driver = new ChromeDriver(chromeCaps);
			
        	driver.navigate().to("http://list.jd.com/list.html?cat=737,794,798");
        	
            String content=driver.getPageSource();
            
            //FileUtils.writeStringToFile(new File("/root/list.html"), content);
            // Check the title of the page
            System.out.println("Page source: " + content);
            System.out.println("getCurrentUrl: " + driver.getCurrentUrl());
    	}finally{
    		 //Close the browser
    		if(driver!=null){
    			driver.quit();
    		}
    	}
    }
    
}
