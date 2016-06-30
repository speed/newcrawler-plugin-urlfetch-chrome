package com.newcrawler.plugin.urlfetch.chrome;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.soso.plugin.UrlFetchPlugin;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

public class UrlFetchPluginService implements UrlFetchPlugin{
	private final static Log logger = LogFactory.getLog(UrlFetchPluginService.class);
	
	public static final String PROPERTIES_TIMEOUT_JAVASCRIPT = "timeout.javascript";
	public static final String PROPERTIES_TIMEOUT_CONNECTION = "timeout.connection";
	
	public static final String PROXY_IP = "proxy.ip";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_USER = "proxy.username";
	public static final String PROXY_PASS = "proxy.password";
	public static final String PROXY_TYPE = "proxy.type";
	public static final String CHROME_DRIVER = "chrome.driver";
	public static final String CHROME_EXTENSIONS_MODHEADER = "chrome.extensions.modHeader";
	
	public static final String PROPERTIES_JS_FILTER_REGEXS = "js.filter.regexs";
	public static final String PROPERTIES_JS_FILTER_TYPE = "js.filter.type";
	public static final String PROPERTIES_JS_CACHE_REGEXS = "js.cache.regexs";
	private static final String DEFAULT_JS_FILTER_TYPE = "include";
	
	private WebDriver driver=null;
	private DesiredCapabilities capabilities;
	private static final String extensionId="hckgkplabbelodmlbgjfocldjejlogbk";
	
	public static void main(String[] args) throws IOException{
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		
		try{
			Map<String, String> properties=new HashMap<String, String>(); 
			/*properties.put(PROXY_IP, "127.0.0.1");
			properties.put(PROXY_PORT, String.valueOf(8888));
			properties.put(PROXY_TYPE, "http");*/
			
			properties.put(PROPERTIES_JS_FILTER_TYPE, "include");
			
			properties.put(CHROME_DRIVER, "D:/js/chromedriver.exe");
			properties.put(CHROME_EXTENSIONS_MODHEADER, "C:/Users/wisers/git/newcrawler-plugin-urlfetch-chrome/crx/ModHeader.crx");
			
			Map<String, String> headers=new HashMap<String, String>(); 
			
			String crawlUrl="http://china.newcrawler.com/header";
			crawlUrl="http://item.jd.com/1861098.html"; 
			String method=null; 
			String userAgent="NewCrawler Spider 2.2"; 
			String encoding="GB2312";
			List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
			HttpCookieBo httpCookieBo=new HttpCookieBo("NewCralwer", "Speed");
			cookieList.add(httpCookieBo);
			
			UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			
			
			Map<String, Object> map1 = urlFetchPluginService.execute(urlFetchPluginBo);
			
			System.out.println(map1.get(RETURN_DATA_KEY_CONTENT));
			
			List<HttpCookieBo> cookies=(List<HttpCookieBo>)map1.get(RETURN_DATA_KEY_COOKIES);
			if(cookies!=null && !cookies.isEmpty()){
				for(HttpCookieBo cookieBo:cookies){
					System.out.println(cookieBo.getName()+"="+cookieBo.getValue());
				}
			}
			
			List<String> jsUrls = (List<String>)map1.get(RETURN_DATA_KEY_INCLUDE_JS);
			if(jsUrls!=null && !jsUrls.isEmpty()){
				for(String url:jsUrls){
					System.out.println(url);
				}
			}
		}finally{
			urlFetchPluginService.destory();
		}
	}
	
	public void destory() {
		if(driver!=null){
			driver.quit();
		}
	}

	public Map<String, Object> execute(UrlFetchPluginBo urlFetchPluginBo) throws IOException {
		Map<String, String> properties=urlFetchPluginBo.getProperties();
		Map<String, String> headers=urlFetchPluginBo.getHeaders();
		String crawlUrl=urlFetchPluginBo.getCrawlUrl();
		String method=urlFetchPluginBo.getMethod();
		List<HttpCookieBo> cookieList=urlFetchPluginBo.getCookieList();
		String userAgent=urlFetchPluginBo.getUserAgent();
		String encoding=urlFetchPluginBo.getEncoding();
		
		String jsFilterRegexs = null;
		String jsFilterType = DEFAULT_JS_FILTER_TYPE;
		String jsCacheRegexs = null;
		int timeoutConnection=60000;
		int timeoutJavascript=5000;
		
		String proxyIP=null;
		int proxyPort=-1;
		String proxyUsername=null;
		String proxyPassword=null;
		String proxyType=null;
		
		String chromeExtensions=null;
		String chromeDriver=null;
		
		if (properties != null) {
			if (properties.containsKey(PROPERTIES_JS_FILTER_REGEXS) && properties.get(PROPERTIES_JS_FILTER_REGEXS)!=null 
					&& !"".equals(properties.get(PROPERTIES_JS_FILTER_REGEXS).trim())) {
				jsFilterRegexs = properties.get(PROPERTIES_JS_FILTER_REGEXS).trim();
			}
			
			if (properties.containsKey(PROPERTIES_JS_CACHE_REGEXS) && properties.get(PROPERTIES_JS_CACHE_REGEXS)!=null 
					&& !"".equals(properties.get(PROPERTIES_JS_CACHE_REGEXS).trim())) {
				jsCacheRegexs = properties.get(PROPERTIES_JS_CACHE_REGEXS).trim();
			}

			if (properties.containsKey(PROPERTIES_JS_FILTER_TYPE) && properties.get(PROPERTIES_JS_FILTER_TYPE)!=null 
					&& !"".equals(properties.get(PROPERTIES_JS_FILTER_TYPE).trim())) {
				jsFilterType = properties.get(PROPERTIES_JS_FILTER_TYPE).trim();
			}
			

			if (properties.containsKey(PROPERTIES_TIMEOUT_JAVASCRIPT) && properties.get(PROPERTIES_TIMEOUT_JAVASCRIPT)!=null 
					&& !"".equals(properties.get(PROPERTIES_TIMEOUT_JAVASCRIPT).trim())) {
				timeoutJavascript = Integer.parseInt(properties.get(PROPERTIES_TIMEOUT_JAVASCRIPT).trim());
			}
			if (properties.containsKey(PROPERTIES_TIMEOUT_CONNECTION) && properties.get(PROPERTIES_TIMEOUT_CONNECTION)!=null 
					&& !"".equals(properties.get(PROPERTIES_TIMEOUT_CONNECTION).trim())) {
				timeoutConnection = Integer.parseInt(properties.get(PROPERTIES_TIMEOUT_CONNECTION).trim());
			}
			
			if (properties.containsKey(PROXY_IP) && !"".equals(properties.get(PROXY_IP).trim())) {
				proxyIP = properties.get(PROXY_IP).trim();
			}
			if (properties.containsKey(PROXY_PORT) && !"".equals(properties.get(PROXY_PORT).trim())) {
				proxyPort = Integer.parseInt(properties.get(PROXY_PORT).trim());
			}

			if (properties.containsKey(PROXY_USER) && !"".equals(properties.get(PROXY_USER).trim())) {
				proxyUsername = properties.get(PROXY_USER).trim();
			}
			
			if (properties.containsKey(PROXY_PASS) && !"".equals(properties.get(PROXY_PASS).trim())) {
				proxyPassword = properties.get(PROXY_PASS).trim();
			}
			
			if (properties.containsKey(PROXY_TYPE) && !"".equals(properties.get(PROXY_TYPE).trim())) {
				proxyType = properties.get(PROXY_TYPE).trim();
			}
			
			if (properties.containsKey(CHROME_EXTENSIONS_MODHEADER) && !"".equals(properties.get(CHROME_EXTENSIONS_MODHEADER).trim())) {
				chromeExtensions = properties.get(CHROME_EXTENSIONS_MODHEADER).trim();
			}
			
			if (properties.containsKey(CHROME_DRIVER) && !"".equals(properties.get(CHROME_DRIVER).trim())) {
				chromeDriver = properties.get(CHROME_DRIVER).trim();
			}
		}
		if(chromeDriver==null){
			logger.error("Chrome driver is required.");
			return null;
		}
		if(chromeExtensions==null){
			logger.error("Chrome extensions 'ModHeader' is required.");
			return null;
		}
		String filterRegexs = "";
		if (jsFilterRegexs != null && !"".equals(jsFilterRegexs.trim())) {
			String[] regexs = jsFilterRegexs.split("\\Q|$|\\E");
			int len = regexs.length;
			for (int i = 0; i < len; i++) {
				String regex = regexs[i];
				regex = regex.trim();
				regex = "^"+conversionRegexToStr(regex)+"$";
				if(regex.indexOf("*")!=-1){
					regex = regex.replaceAll("\\*", ".*");
				}
				if("".equals(filterRegexs)){
					filterRegexs=regex;
				}else{
					filterRegexs=filterRegexs+"|"+regex;
				}
			}
		}
		String cacheRegexs = "";
		if (jsCacheRegexs != null && !"".equals(jsCacheRegexs.trim())) {
			String[] regexs = jsCacheRegexs.split("\\Q|$|\\E");
			int len = regexs.length;
			for (int i = 0; i < len; i++) {
				String regex = regexs[i];
				regex = regex.trim();
				regex = "^"+conversionRegexToStr(regex)+"$";
				if(regex.indexOf("*")!=-1){
					regex = regex.replaceAll("\\*", ".*");
				}
				if("".equals(cacheRegexs)){
					cacheRegexs=regex;
				}else{
					cacheRegexs=cacheRegexs+"|"+regex;
				}
			}
		}
		
		if(headers==null){
			headers = new HashMap<String, String>();
		}
		if(cookieList!=null && !cookieList.isEmpty()){
			String cookie=getCookies(cookieList);
			if(cookie!=null && !"".equals(cookie)){
				headers.put("Cookie", cookie);
			}
		}
		if(userAgent!=null && !"".equals(userAgent)){
			headers.put("User-Agent", userAgent);
		}
		
		List<String> jsList = new ArrayList<String>();
		Map<String, Object> map=null;
		try {
			map=read(proxyIP, proxyPort, proxyUsername, proxyPassword, proxyType, chromeExtensions, chromeDriver, headers, crawlUrl, method, encoding, 
					jsFilterType, filterRegexs, jsList, cacheRegexs, timeoutConnection, timeoutJavascript);
		} catch (SocketException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return map;
	}
	
	private Map<String, Object> read(String proxyIP, int proxyPort, final String proxyUsername, final String proxyPassword, final String proxyType, String chromeExtensions, String chromeDriver, Map<String, String> headers, String crawlUrl, String method, String encoding,
			String jsFilterType, String jsFilterRegexs, List<String> jsList, String jsCacheRegexs, final long pageLoadTimeout, final long scriptTimeout) throws IOException{
		if(driver==null){
			synchronized (this) {
				if(driver==null){
					//https://sites.google.com/a/chromium.org/chromedriver/capabilities
					capabilities = DesiredCapabilities.chrome();
			        if(proxyIP!=null){
			        	// Add the WebDriver proxy capability.
			        	Proxy proxy = new Proxy();
			        	if(proxyType!=null && "socks5".equals(proxyType)){
			        		proxy.setSocksProxy(proxyIP+":"+proxyPort);
			        	}else{
			        		proxy.setHttpProxy(proxyIP+":"+proxyPort);
			        	}
			        	if(proxyUsername!=null && proxyPassword!=null){
		        			proxy.setSocksUsername(proxyUsername);
		        			proxy.setSocksPassword(proxyPassword);
		        		}
			        	capabilities.setCapability("proxy", proxy);
			        }
			        
			        HashMap<String, Object> settings = new HashMap<String, Object>(); 
		        	settings.put("images", 2); //disabled load images
		            HashMap<String, Object> prefs = new HashMap<String, Object>(); 
		            prefs.put("profile.managed_default_content_settings", settings); 
		            ChromeOptions options =new ChromeOptions(); 
		            options.setExperimentalOption("prefs", prefs); 
		            options.addExtensions(new File(chromeExtensions));
		            
		            capabilities.setCapability(ChromeOptions.CAPABILITY, options); 
		            
		            if(chromeDriver.startsWith("http://")){
		            	driver = new RemoteWebDriver(new URL(chromeDriver), capabilities);
		            }else{
		            	System.setProperty("webdriver.chrome.driver", chromeDriver);
		            	driver = new ChromeDriver(capabilities);
		            }
		            
				}
				driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.MILLISECONDS);
				driver.manage().timeouts().setScriptTimeout(scriptTimeout, TimeUnit.MILLISECONDS);
			}
		}
		String customHeaders="";
		for(String key:headers.keySet()){
			if("Accept-Encoding".equals(key)){
    			continue;
    		}
			if("".equals(customHeaders)){
				customHeaders="{enabled: true, name: '"+key+"', value: '"+headers.get(key)+"', comment: ''}";
			}else{
				customHeaders=customHeaders+","+"{enabled: true, name: '"+key+"', value: '"+headers.get(key)+"', comment: ''}";
			}
    	}
		
		// set the context on the extension so the localStorage can be accessed
    	driver.get("chrome-extension://"+extensionId+"/blank.html");
    	
    	// setup ModHeader with two headers (token1 and token2)
    	((JavascriptExecutor)driver).executeScript(
    		"localStorage.setItem('allUrls', '');"+	
    		"localStorage.setItem('jsFilterRegexs', '"+jsFilterRegexs+"');"+	
    		"localStorage.setItem('jsFilterType', '"+jsFilterType+"');"+	
    	    "localStorage.setItem('profiles', JSON.stringify([{                " +
    	    "  title: 'Selenium', hideComment: true, appendMode: '',           " +
    	    "  headers: [                                                      " +
    	    		customHeaders	+
    	    "  ],                                                              " +
    	    "  respHeaders: [],                                                " +
    	    "  filters: []                                                     " +
    	    "}]));                                                             " );
    	
        driver.get(crawlUrl);
        String pageSource=driver.getPageSource();
		String currentUrl=driver.getCurrentUrl();
		Object outputEncoding=((JavascriptExecutor)driver).executeScript("return document.charset;");
		
        Date nowDate=new Date();
		long nowTime=nowDate.getTime();
        List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
        Set<Cookie> cookieSet = driver.manage().getCookies();
		for (Cookie cookieObj : cookieSet) {
			HttpCookieBo pluginCookie=new HttpCookieBo(cookieObj.getName(), cookieObj.getValue());
			pluginCookie.setDomain(cookieObj.getDomain());
			pluginCookie.setPath(cookieObj.getPath());
			pluginCookie.setSecure(cookieObj.isSecure());
			
			long expiry=-1;
			Date expiryDate=cookieObj.getExpiry();
			if(expiryDate!=null){
				expiry=cookieObj.getExpiry().getTime()-nowTime;
				expiry=expiry/1000;
			}
			
			pluginCookie.setMaxAge(expiry);//
			pluginCookie.setHttpOnly(cookieObj.isHttpOnly());
			cookieList.add(pluginCookie);
		}
		driver.manage().deleteAllCookies();
		
		
		driver.get("chrome-extension://"+extensionId+"/blank.html");
        Object jsUrl=((JavascriptExecutor)driver).executeScript("return localStorage.getItem('allUrls');");
        
        String[] jsUrls = jsUrl.toString().split("\\Q|$|\\E");
        for(String js:jsUrls){
        	jsList.add(js);
        }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RETURN_DATA_KEY_CONTENT, pageSource);
		map.put(RETURN_DATA_KEY_REALURL, currentUrl);
		map.put(RETURN_DATA_KEY_INCLUDE_JS, jsList);
		map.put(RETURN_DATA_KEY_COOKIES, cookieList);
		map.put(RETURN_DATA_KEY_ENCODING, outputEncoding);
		return map;
	}
	
	private final String getCookies(List<HttpCookieBo> cookieList){
		String cookie="";
		for(HttpCookieBo httpCookie:cookieList){
			cookie+=httpCookie.getName()+"="+httpCookie.getValue()+";";
		}
		return cookie;
	}
	
	private static String conversionRegexToStr(String s) {
		StringBuffer sb = new StringBuffer();
        for (int i=0, len=s.length(); i<len; i++) {
            char c = s.charAt(i);
            switch (c) {
			case '^':
			case '$':
			case '(':
			case ')':
			case '+':
			case '?':
			case '.':
			case '[':
			case '/':
			case '{':
			case '|':
				sb.append('\\');
				sb.append(c);
				break;
			case '\\':
				int j=i+1;
				if(j<len){
					char c2 = s.charAt(j);
					switch (c2) {
					case 'd':
					case 'D':
					case 'w':
					case 'W':
					case 's':
					case 'S':
					case 'Q':
					case 'E':
					case 'b':
					case 'B':
						sb.append(c);
						sb.append(c);
						break;
					case '\\':
						sb.append(c);
						sb.append(c);
						i++;
						break;
					default:
						sb.append(c);//+ 20140714 jd page .replaceWith("<div id=\"plist\" class
						sb.append(c);
						break;
					}
				}else{
					sb.append(c);//+ 20140714
					sb.append(c);
				}
				break;	
			default:
				sb.append(c);
				break;
			}
        }
        return sb.toString();
	}

}
