package com.newcrawler.plugin.test;

import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.newcrawler.plugin.urlfetch.chrome.UrlFetchPluginService;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;
import com.sun.jndi.ldap.Connection;

public class UrlFetchPluginServiceTest  {
	
	public static void main(String[] args) throws IOException{
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		
		try{
			Map<String, String> properties=new HashMap<String, String>(); 
			/*properties.put(PROXY_IP, "127.0.0.1");
			properties.put(PROXY_PORT, String.valueOf(8888));
			properties.put(PROXY_TYPE, "http");*/
			
			properties.put(UrlFetchPluginService.PROPERTIES_JS_FILTER_TYPE, "include");
			
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "D:/js/chromedriver.exe");
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "http://192.168.45.21:5555/wd/hub");
			
			properties.put(UrlFetchPluginService.CHROME_EXTENSIONS_MODHEADER, "/Users/liaolianwu/Documents/workspace/newcrawler-plugin-urlfetch-chrome/crx/ModHeader.crx");
			
			Map<String, String> headers=new HashMap<String, String>(); 
			
			String crawlUrl="http://www.lagou.com/";
			//crawlUrl="http://item.jd.com/1861098.html"; 
			String method=null; 
			String userAgent="NewCrawler Spider 2.2"; 
			String encoding="GB2312";
			List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
			HttpCookieBo httpCookieBo=new HttpCookieBo("NewCralwer", "Speed");
			cookieList.add(httpCookieBo);
			
			UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			
			
			Map<String, Object> map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
			System.out.println(map1.get(UrlFetchPluginService.RETURN_DATA_KEY_CONTENT));
			
			List<HttpCookieBo> cookies=(List<HttpCookieBo>)map1.get(UrlFetchPluginService.RETURN_DATA_KEY_COOKIES);
			if(cookies!=null && !cookies.isEmpty()){
				for(HttpCookieBo cookieBo:cookies){
					System.out.println(cookieBo.getName()+"="+cookieBo.getValue());
				}
			}
			
			List<String> jsUrls = (List<String>)map1.get(UrlFetchPluginService.RETURN_DATA_KEY_INCLUDE_JS);
			if(jsUrls!=null && !jsUrls.isEmpty()){
				for(String url:jsUrls){
					System.out.println(url);
				}
			}
		}finally{
			urlFetchPluginService.destory();
		}
	}
    
}
