package org.javacoo.crawler.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.javacoo.crawler.config.CrawlerConfig;
/**
 * 工具类
 * @author javacoo
 * @since 2012-05-02
 */
public class CommonUtils {
	/**
	 * 组装全局字符串替换mao
	 * @param commonReplaceWords
	 * @return
	 */
	public static Map<String, String> populateWordsMap(String str) {
		if(StringUtils.isNotBlank(str)){
			Map<String, String> wordsMap = new HashMap<String, String>();
			String[] keyValueArr = str.split(",");
			String[] tempArr = null;
			for(String keyValue : keyValueArr){
				if(keyValue.contains("=")){
					tempArr = keyValue.split("=");
					wordsMap.put(tempArr[0], tempArr[1]);
				}else{
					wordsMap.put(keyValue, CrawlerConfig.defaultWords);
				}
			}
			return wordsMap;
		}
		return null;
	}

}
