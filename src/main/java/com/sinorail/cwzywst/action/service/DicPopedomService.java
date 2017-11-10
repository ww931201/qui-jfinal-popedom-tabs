package com.sinorail.cwzywst.action.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sinorail.cwzywst.model.DicPopedom;

public class DicPopedomService {
	
	@Before(Tx.class)
	public Boolean exchangePosition(DicPopedom p, DicPopedom mp) {
		return p.update() && mp.update();
	}
	
	
	/**
	 * 拼接popedomListTree
	 * @param popedom
	 * @param popedomList
	 * @return
	 */
	public List<DicPopedom> spellPodeDomTree (List<DicPopedom> popedomList) {
		List<DicPopedom> temp = new ArrayList<DicPopedom>();
		for (DicPopedom dicPopedom : popedomList) {
			if(dicPopedom.getParentId().equals("0")){
				temp.add(filterByParentId(dicPopedom, popedomList));
			}
		}
		return temp;
	}

	/**
	 * 递归拼装Tree
	 * @param ppd
	 * @param popedomList
	 * @return
	 */
	private DicPopedom filterByParentId(DicPopedom ppd, List<DicPopedom> popedomList) {
		List<DicPopedom> temp = new ArrayList<DicPopedom>();
		for (DicPopedom dicPopedom : popedomList) {
			if(dicPopedom.getParentId().equals(ppd.getId())){
				temp.add(filterByParentId(dicPopedom, popedomList));
			}
		}
		ppd.put("children", temp);
		return ppd;
	}
	
}
