package com.newcrawler.plugin.test;

import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sun.jndi.ldap.Connection;

public class Selenium2ChromeTest  {
	
    public static void main(String[] args) throws IOException {
    	
    	ChromeDriver driver=null;
    	try{
    		
    		System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
    		System.setProperty("webdriver.chrome.driver", "d:/js/chromedriver.exe");
        	
        	HashMap<String, Object> settings = new HashMap<String, Object>(); 
        	settings.put("images", 2); //disabled load images
            

            HashMap<String, Object> prefs = new HashMap<String, Object>(); 
            prefs.put("profile.managed_default_content_settings", settings); 
            

            ChromeOptions options =new ChromeOptions(); 
            options.setExperimentalOption("prefs", prefs); 
            options.addExtensions(new File("D:/js/chromemodifyheaders/chromemodifyheaders.crx"));
            
            DesiredCapabilities chromeCaps = DesiredCapabilities.chrome(); 
            chromeCaps.setCapability(ChromeOptions.CAPABILITY, options); 
            
        	driver = new ChromeDriver(chromeCaps);
        	
        	driver.navigate().to("http://china.newcrawler.com/header");
        	
            String content=driver.getPageSource();
            
            FileUtils.writeStringToFile(new File("/root/list.html"), content);
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
