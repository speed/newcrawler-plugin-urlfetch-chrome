package com.newcrawler.plugin.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.newcrawler.plugin.urlfetch.chrome.UrlFetchPluginService;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

public class UrlFetchPluginServiceTest  {
	
	public static void main(String[] args) throws IOException{
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		try{
			Properties systemProp = new Properties();
			InputStream resourceAsStream=UrlFetchPluginService.class.getClass().getResourceAsStream("/system-test.properties");
			try {
		        if (null != resourceAsStream) {
		        	systemProp.load(resourceAsStream);
		        }
		    }finally{
		    	try {
		    		if(resourceAsStream!=null)
		    			resourceAsStream.close();
				} catch (IOException e) {
					
				}
		    }
			Map<String, String> properties=new HashMap<String, String>(); 
			for(Object key:systemProp.keySet()){
				properties.put((String)key, (String)systemProp.get(key));
			}
			properties.put(UrlFetchPluginService.PROPERTIES_JS_FILTER_TYPE, "include");
			
			Map<String, String> headers=new HashMap<String, String>(); 
			
			String crawlUrl="http://www.google.com/";
			//crawlUrl="http://item.jd.com/1861098.html"; 
			String method=null; 
			String userAgent=null; 
			String encoding=null;
			List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
			
			UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			Map<String, Object> map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
			System.out.println(map1.get(UrlFetchPluginService.RETURN_DATA_KEY_CONTENT));
		}finally{
			urlFetchPluginService.destory();
		}
	}
    
}
