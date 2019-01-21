package com.newcrawler.plugin.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Selenium2ChromeTest4  {
	
	/**
	 * http://stackoverflow.com/questions/37456794/chrome-modify-headers-in-selenium-java-i-am-able-to-add-extension-crx-through
	 * https://mweeeh.wordpress.com/2014/10/30/webdriver-add-preconfigured-extension-to-chromedriver-with-java/
	 * 
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
    	
    	ChromeDriver driver=null;
    	try{
    		
    		System.setProperty("webdriver.chrome.driver", "d:/js/chromedriver.exe");
        	
        	HashMap<String, Object> settings = new HashMap<String, Object>(); 
        	settings.put("images", 2); //disabled load images
            

            HashMap<String, Object> prefs = new HashMap<String, Object>(); 
            prefs.put("profile.managed_default_content_settings", settings); 
            

            ChromeOptions options =new ChromeOptions(); 
            options.setExperimentalOption("prefs", prefs); 
            //options.addExtensions(new File("C:\\Users\\wisers\\git\\newcrawler-plugin-urlfetch-chrome\\crx\\ModHeader.crx"));
            
            DesiredCapabilities chromeCaps = DesiredCapabilities.chrome(); 
            chromeCaps.setCapability(ChromeOptions.CAPABILITY, options); 
            
        	driver = new ChromeDriver(chromeCaps);
        	
        	/*// set the context on the extension so the localStorage can be accessed
        	driver.get("chrome-extension://hckgkplabbelodmlbgjfocldjejlogbk/icon.png");
        	
        	// setup ModHeader with two headers (token1 and token2)
        	((JavascriptExecutor)driver).executeScript(
        	    "localStorage.setItem('profiles', JSON.stringify([{                " +
        	    "  title: 'Selenium', hideComment: true, appendMode: '',           " +
        	    "  headers: [                                                      " +
        	    //"    {enabled: true, name: 'token1', value: 'test1', comment: ''}, " +
        	    //"    {enabled: true, name: 'token2', value: 'test2', comment: ''}  " +
        	    "  ],                                                              " +
        	    "  respHeaders: [],                                                " +
        	    "  filters: []                                                     " +
        	    "}]));                                                             " );*/
        	
        	
        	driver.navigate().to("http://119.254.209.77");
        	
        	((JavascriptExecutor)driver).executeScript("document.getElementById('_ctl0__ctl0_Content_MenuHyperLink14').click();");
        			
            String content=driver.getPageSource();
            
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
