package com.sinorail.cwzywst.action.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sinorail.cwzywst.model.DicRole;

public class DicRoleService {
	
	@Before(Tx.class)
	public Boolean exchangePosition(DicRole p, DicRole mp) {
		return p.update() && mp.update();
	}
	
	
	/**
	 * 拼接popedomListTree
	 * @param popedom
	 * @param popedomList
	 * @return
	 */
	public List<DicRole> spellPodeDomTree (List<DicRole> popedomList) {
		List<DicRole> temp = new ArrayList<DicRole>();
		for (DicRole DicRole : popedomList) {
			if(DicRole.getParentId().equals("0")){
				temp.add(filterByParentId(DicRole, popedomList));
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
	private DicRole filterByParentId(DicRole ppd, List<DicRole> popedomList) {
		List<DicRole> temp = new ArrayList<DicRole>();
		for (DicRole DicRole : popedomList) {
			if(DicRole.getParentId().equals(ppd.getId())){
				temp.add(filterByParentId(DicRole, popedomList));
			}
		}
		ppd.put("children", temp);
		return ppd;
	}
	
}
