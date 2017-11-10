package com.sinorail.cwzywst.action;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sinorail.cwzywst.extend.QuiController;

public class EcharsAction extends QuiController {
	
	private static final Logger LOG = Logger.getLogger(EcharsAction.class);
	
	public void index() {
		
		render("echarsView.html");
	}
	
	public void loadData(){
		//options.legend.data = result.legend;
		//直接设置的是对象中的data的值。
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		
		
		map2.put("text", "后台设置的title的值");
          
		Integer[] integers = {5, 10, 15, 20, 25, 30, 35};
		map3.put("name", "小明");
		map3.put("type", "bar");
		map3.put("data", integers);
		
		String[] strings = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		map4.put("data", strings);
		
		String[] strs = {"小明"};
		map5.put("data", strs);
		
		map.put("title", map2);
		map.put("series", map3);
		map.put("xAxis", map4);
		map.put("legend", map5);
		renderJson(map); 
	}
}
