package com.newcrawler.plugin.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newcrawler.plugin.urlfetch.chrome.UrlFetchPluginService;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

public class UrlFetchPluginServiceTest_cnki  {
	
	public static void main(String[] args) throws IOException{
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		
		try{
			Map<String, String> properties=new HashMap<String, String>(); 
			properties.put(UrlFetchPluginService.PROXY_IP, "127.0.0.1");
			properties.put(UrlFetchPluginService.PROXY_PORT, String.valueOf(8888));
			properties.put(UrlFetchPluginService.PROXY_TYPE, "http");
			
			properties.put(UrlFetchPluginService.PROPERTIES_JS_FILTER_TYPE, "include");
			
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "D:/js/chromedriver.exe");
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "D:/js/chromedriver_win32_2.25/chromedriver.exe");
			
			properties.put(UrlFetchPluginService.CHROME_EXTENSIONS_MODHEADER, "D:/Workspace/workspace j2ee/newcrawler-plugin-urlfetch-chrome/crx/ModHeader.crx");
			
			Map<String, String> headers=new HashMap<String, String>(); 
			
			String crawlUrl="http://epub.cnki.net/kns/brief/result.aspx?dbPrefix=scdb&action=scdbsearch&db_opt=SCDB";
			//crawlUrl="http://item.jd.com/1861098.html"; 
			String method=null; 
			String userAgent="Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36"; 
			String encoding="GB2312";
			List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
			
			UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			Map<String, Object> map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
			headers=new HashMap<String, String>(); 
			cookieList=new ArrayList<HttpCookieBo>();
			crawlUrl="http://epub.cnki.net/kns/brief/result.aspx?dbPrefix=scdb&action=scdbsearch&db_opt=SCDB";
			urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
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
			
			System.out.println(map1.get(UrlFetchPluginService.RETURN_DATA_KEY_CONTENT));
		}finally{
			urlFetchPluginService.destory();
		}
	}
    
}
