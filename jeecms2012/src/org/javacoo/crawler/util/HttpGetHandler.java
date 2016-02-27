package org.javacoo.crawler.util;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.javacoo.crawler.constants.Constants;
import org.javacoo.crawler.data.CrawlURI;
/**
 * HttpGet
 * @author javacoo
 * @since 2011-12-17
 */
public class HttpGetHandler{
	
	public static HttpGet getHttpGet(CrawlURI uri){
		Log log =  LogFactory.getLog(HttpGet.class);
		String pathType = uri.getPathType();
		//取得当前URL的路径
		String rawPath = uri.getRawPath();
		
		if(Constants.PATH_TYPE_0.equals(pathType)){
			rawPath = uri.getUrl();
		}
		
		
		
		//绝对路径
		if(Constants.PATH_TYPE_2.equals(pathType)){
			//如果是相对当前路径，则取得父路径的RawPath在加上当前RawPath
			if(null != uri.getParentURI()){
				rawPath = getRawPath(uri.getParentURI().getRawPath())+rawPath;
			}
		}
		log.info("==============当前访问路径========"+rawPath);
		HttpGet httpGet = new HttpGet(rawPath);
		httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
	    httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.5");
	    httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.6) Gecko/20100625 Firefox/3.6.6 Greatwqs");
	    httpGet.setHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	    httpGet.setHeader("Keep-Alive", "300");
	    httpGet.setHeader("Connection", "Keep-Alive");
	    httpGet.setHeader("Cache-Control", "no-cache"); 
		return httpGet;
	}
	/**
	 * 取得URI对象的路径
	 * @param uri
	 * @return
	 */
	private static String getRawPath(String rawPath){
		if(StringUtils.isBlank(rawPath) || rawPath.lastIndexOf("/") == rawPath.indexOf("/")){
			rawPath = "";
		}else {
			int endPos = rawPath.lastIndexOf("/");
			rawPath = rawPath.substring(0, endPos);
		}
		return rawPath;
	}

}
