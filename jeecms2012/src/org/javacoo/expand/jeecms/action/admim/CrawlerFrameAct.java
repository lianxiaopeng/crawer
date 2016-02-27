package org.javacoo.expand.jeecms.action.admim;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CrawlerFrameAct {
	
	@RequestMapping("/frame/commontype_main.do")
	public String commontypeMain(){
		return "frame/commontype_main";
	}
	
	@RequestMapping("/frame/crawler_main.do")
	public String crawlerMain(){
		return "frame/crawler_main";
	}
	
	@RequestMapping("/frame/crawler_left.do")
	public String crawlerLeft(ModelMap model) {
		return "frame/crawler_left";
	}

	@RequestMapping("/frame/crawler_right.do")
	public String crawlerRight(ModelMap model) {
		return "frame/crawler_right";
	}
	
}
