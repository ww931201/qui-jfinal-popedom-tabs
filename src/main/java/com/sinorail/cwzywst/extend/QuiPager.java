package com.sinorail.cwzywst.extend;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;

/**
 * 适用qui返回分页查询结果
 * @author lenovo
 *
 */
public class QuiPager {

	private Map<String, Object> pager = new HashMap<String, Object>();

	public QuiPager(Page<Object> page) {
		pager.put("pager.pageNo", page.getPageNumber());
		pager.put("pager.totalRows", page.getTotalRow());
		pager.put("rows", page.getList());
	}

	public QuiPager(Page<Object> page, String sort, String direction) {
		pager.put("pager.pageNo", page.getPageNumber());
		pager.put("pager.totalRows", page.getTotalRow());
		pager.put("rows", page.getList());
		pager.put("sort", sort);
		pager.put("direction", direction);
	}
	
	public Map<String, Object> getPager() {
		return pager;
	}

	public void setPager(Map<String, Object> pager) {
		this.pager = pager;
	}
	
}
