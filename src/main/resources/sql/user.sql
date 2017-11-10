#namespace("user")
	
	### 全部用户(超级管理员)
	#sql("paginateSelect")
		select ID, USER_NAME, USER_CODE, ROLE_NAME, REMARK, ENTRY_TIME from DIC_USER WHERE 1=1 
		#if(USER_NAME ??)
			and USER_NAME like '%#(USER_NAME)%'
		#end
		#if(USER_CODE ??)
			and USER_CODE like '%#(USER_CODE)%'
		#end
		order by ENTRY_TIME
	#end
	
	### 全部子用户
	#sql("paginateSelectChild")
		select ID, USER_NAME, USER_CODE, ROLE_NAME, REMARK, ENTRY_TIME from DIC_USER WHERE PARENT_ID = '#(PARENT_ID)'
		#if(USER_NAME ??)
			and USER_NAME like '%#(USER_NAME)%'
		#end
		#if(USER_CODE ??)
			and USER_CODE like '%#(USER_CODE)%'
		#end
		order by ENTRY_TIME
	#end
	
	### 根据ID查找用户详情
	#sql("selectById")
		select * from VIEW_DIC_USER where ID = ?
	#end
	
	### 根据用户代码查询用户是否存在(添加)
	#sql("findByUserCode")
		select * from DIC_USER where USER_CODE = ?
	#end
	### 根据用户代码和用户id查询用户是否存在(修改)
	#sql("findByUserCodeAndId")
		select * from DIC_USER where USER_CODE = ? and id != ?
	#end
	
	### 判断登录用户是否存在
	#sql("isPassed")
		select * from DIC_USER where USER_CODE = ? AND PASSWD = ?
	#end
	
	### 判断用户名是否存在
	#sql("isExist")
		select * from DIC_USER where USER_NAME = ?
	#end
	
	### 获取全部单位列表
	#sql("unitAll")
		select id "id", unit_name "name", parent_id "parentId", 'true' "open" , remark, '/libs/icons/user_group.png' "icon", case when remark=1 then 'false' else 'false' end "clickExpand" from DIC_UNIT where remark = 1 or remark = 2  order by xh
	#end
	### 根据单位id获取单位列表
	#sql("unitByUnitId")
		select id "id", unit_name "name", parent_id "parentId", 'true' "open" , remark, '/libs/icons/user_group.png' "icon", case when remark=1 then 'false' else 'false' end "clickExpand" from DIC_UNIT where parent_id = ?  order by xh
	#end
	
	### 根据Id获取科室
	#sql("officeById")
		select id "id", unit_name "name", parent_id "parentId", 'true' "open" , remark, '/libs/icons/user_group.png' "icon" from DIC_UNIT where id = ? and remark = 3  order by xh
	#end
	
	### 获取全部角色列表
	#sql("roleAll")
		select id "id", role_name "name", 0 "parentId", 'false' "open" , remark, '/libs/icons/user-green.png' "icon"  from DIC_ROLE order by xh
	#end
	### 通过parentId获取角色列表
	#sql("roleListByParentId")
		select id "id", role_name "name", 0 "parentId", 'false' "open" , remark, '/libs/icons/user-green.png' "icon"  from DIC_ROLE where parent_id = ? or id = ? order by xh
	#end
#end
