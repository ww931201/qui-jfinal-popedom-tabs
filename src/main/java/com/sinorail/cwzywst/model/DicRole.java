package com.sinorail.cwzywst.model;

import java.util.List;

import com.jfinal.plugin.activerecord.SqlPara;
import com.sinorail.cwzywst.model.base.BaseDicRole;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DicRole extends BaseDicRole<DicRole> {
	public static final DicRole dao = new DicRole().dao();

	public List<DicRole> list(DicRole role) {
		SqlPara sqlp = getSqlPara("role.listSelect", role);
		return find(sqlp);
	}
	/**
	 * 暂时未用
	 * @param role
	 * @return
	 */
	public List<DicRole> listByRoot(DicRole role) {
		SqlPara sqlp = getSqlPara("role.listSelectByRoot", role);
		return find(sqlp);
	}
	/**
	 * 根据角色代码查询角色是否存在(添加)
	 * @param roleCode
	 * @return
	 */
	public boolean isExistRoleName(String roleCode) {
		DicRole role = findFirst(getSql("role.findByRoleName"), roleCode);
		if(role == null){
			return false;
		}
		return true;
	}

	/**
	 * 根据角色名称和角色id查询角色是否存在(修改)
	 * @param roleCode
	 * @param roleId
	 * @return
	 */
	public boolean isExistRoleName(String roleCode, String roleId) {
		DicRole role = findFirst(getSql("role.findByRoleNameAndId"), roleCode, roleId);
		if(role == null){
			return false;
		}
		return true;
	}

	public boolean deleteBatchByIds(String ids) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			deleteById(id);
		}
		return true;
	}

	
}
