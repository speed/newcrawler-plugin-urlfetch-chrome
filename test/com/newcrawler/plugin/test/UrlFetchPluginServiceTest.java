package com.newcrawler.plugin.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newcrawler.plugin.urlfetch.chrome.UrlFetchPluginService;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

public class UrlFetchPluginServiceTest  {
	
	public static void main(String[] args) throws IOException{
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		
		try{
			Map<String, String> properties=new HashMap<String, String>(); 
			properties.put(UrlFetchPluginService.PROXY_IP, "192.168.44.244");
			properties.put(UrlFetchPluginService.PROXY_PORT, String.valueOf(8888));
			properties.put(UrlFetchPluginService.PROXY_TYPE, "http");
			
			properties.put(UrlFetchPluginService.PROPERTIES_JS_FILTER_TYPE, "include");
			
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "D:/js/chromedriver.exe");
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "http://192.168.45.21:5555/wd/hub");
			properties.put(UrlFetchPluginService.CHROME_DRIVER, "/Users/liaolianwu/Documents/workspace/newcrawler-plugin-urlfetch-chrome/driver/chromedriver_mac64/chromedriver2.26");
			
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
			
			List<HttpCookieBo> cookieList2=(List<HttpCookieBo>)map1.get(UrlFetchPluginService.RETURN_DATA_KEY_COOKIES);
			if(cookieList2!=null){
				//cookieList.addAll(cookieList2);
			}
			headers=new HashMap<String, String>(); 
			crawlUrl="https://a.lagou.com/collect?v=1&_v=j31&a=1233001226&t=pageview&_s=1&dl=https%3A%2F%2Fwww.lagou.com%2F&ul=zh-cn&de=UTF-8&dt=&sd=32-bit&sr=1024x768&vp=400x300&je=0&_u=MEAAAAQAK~&jid=&cid=&tid=UA-41268416-1&_r=1&z=1444070207";
			urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
			List<HttpCookieBo> cookieList3=(List<HttpCookieBo>)map1.get(UrlFetchPluginService.RETURN_DATA_KEY_COOKIES);
			if(cookieList3!=null){
				for(HttpCookieBo httpCookieBoTemp:cookieList3){
					int index=cookieList.indexOf(httpCookieBoTemp);
					if(index!=-1){
						cookieList.set(index, httpCookieBoTemp);
					}else{
						cookieList.add(httpCookieBoTemp);
					}
				}
			}
			HttpCookieBo httpCookieBoTest=new HttpCookieBo("newcrawler", "newcralwer");
			
			cookieList.add(httpCookieBoTest);
			headers=new HashMap<String, String>(); 
			crawlUrl="http://www.lagou.com/jobs/list_%E7%88%AC%E8%99%AB?px=default&city=%E6%B7%B1%E5%9C%B3#filterBox";
			urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
			System.out.println(map1.get(UrlFetchPluginService.RETURN_DATA_KEY_CONTENT));
		}finally{
			urlFetchPluginService.destory();
		}
	}
    
}
