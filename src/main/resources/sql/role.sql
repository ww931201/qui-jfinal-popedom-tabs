#namespace("role")
	
	### 全部角色
	#sql("listSelectByRoot")
		select ID, ROLE_NAME, REMARK, PARENT_ID from DIC_ROLE WHERE 1=1 
		#if(ROLE_NAME ??)
			and ROLE_NAME like '%#(ROLE_NAME)%'
		#end
	#end
	### 全部角色
	
	#sql("listSelect")
		select ID, ROLE_NAME, REMARK, PARENT_ID from DIC_ROLE WHERE 1=1 
		#if(ROLE_NAME ??)
			and ROLE_NAME like '%#(ROLE_NAME)%'
		#end
	#end
	
	### 根据角色代码查询角色是否存在(添加)
	#sql("findByRoleName")
		select * from DIC_ROLE where ROLE_NAME = ?
	#end
	### 根据角色名称和角色id查询角色是否存在(修改)
	#sql("findByRoleNameAndId")
		select * from DIC_ROLE where ROLE_NAME = ? and id != ?
	#end
	
#end
