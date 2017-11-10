#namespace("popedom")
	
	### 全部权限
	#sql("paginateSelect")
		select ID, POPEDOM_NAME, PARENT_ID , FORWARD_ACTION,XH,REMARK,TARGET,NAV_ICON,NAV_ICON_WIDTH,NAV_ICON_HEIGHT,IS_MENU from DIC_POPEDOM ORDER BY XH
	#end
	
	### 全部菜单(root)
	#sql("menuList")
		select ID "id", POPEDOM_NAME "name", PARENT_ID "parentId", FORWARD_ACTION "url", TARGET "target",NAV_ICON "navIcon",NAV_ICON_WIDTH "navIconWidth",NAV_ICON_HEIGHT "navIconHeight" from DIC_POPEDOM where is_menu = 1 ORDER BY XH
	#end
	### 全部菜单(普通)
	#sql("menuListByRoleId")
		select p.ID "id", p.POPEDOM_NAME "name", p.PARENT_ID "parentId", p.FORWARD_ACTION "url", p.TARGET "target",p.NAV_ICON "navIcon",p.NAV_ICON_WIDTH "navIconWidth",p.NAV_ICON_HEIGHT "navIconHeight" from dic_role_popedom rp left join dic_popedom p on rp.popedom_id = p.id where rp.role_id = ? and p.is_menu = 1 ORDER BY XH
	#end
	
	### 权限分配列表(root)
	#sql("matchList")
		select p.id "id", p.POPEDOM_NAME "name", p.PARENT_ID "parentId", 'true' "open", case when (r.popedom_id is null) then 'false' else 'true'end  "checked" from DIC_POPEDOM p left join (select * from dic_role_popedom where role_id = ?) r on p.id = r.popedom_id ORDER BY p.XH
	#end
	### 权限分配列表(普通)
	#sql("matchListBySessionRoleId")
		select p.id "id", p.POPEDOM_NAME "name", p.PARENT_ID "parentId", 'true' "open", case when (r.popedom_id is null) then 'false' else 'true'end  "checked" from (select dp.* from dic_role_popedom drp left join dic_popedom dp on drp.popedom_id = dp.id where dp.id is not null and drp.role_id = ? ) p left join (select * from dic_role_popedom where role_id = ?) r on p.id = r.popedom_id ORDER BY p.XH
	#end
	
	### 登录权限验证
	#sql("queryByRoleId")
		select p.forward_action from dic_role_popedom rp left join dic_popedom p on rp.popedom_id = p.id where rp.role_id = ? and p.forward_action is not null
	#end
	
#end
