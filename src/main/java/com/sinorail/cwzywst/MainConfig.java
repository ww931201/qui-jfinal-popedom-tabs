package com.sinorail.cwzywst;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.sinorail.cwzywst.action.EcharsAction;
import com.sinorail.cwzywst.action.IndexAction;
import com.sinorail.cwzywst.action.LoginAction;
import com.sinorail.cwzywst.action.PopedomAction;
import com.sinorail.cwzywst.action.PopedomMatchAction;
import com.sinorail.cwzywst.action.RoleAction;
import com.sinorail.cwzywst.action.UserAction;
import com.sinorail.cwzywst.aop.LoginInterceptor;
import com.sinorail.cwzywst.constant.Const;
import com.sinorail.cwzywst.model._MappingKit;
import com.sinorail.cwzywst.util.FileUtils;

public class MainConfig extends JFinalConfig {
	
	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		//读取数据库配置文件
		PropKit.use(Const.file_jdbc);
		//设置当前是否为开发模式
		me.setDevMode(PropKit.getBoolean("devMode"));
		//设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath("upload/temp/");
		//设置上传最大限制尺寸
		//me.setMaxPostSize(1024*1024*10);
		//设置默认下载文件路径 renderFile使用
		me.setBaseDownloadPath("download");
		//设置默认视图类型
		me.setViewType(ViewType.JFINAL_TEMPLATE);
		//设置404渲染视图
		me.setError404View("/static/errorPage/404.html");
		
	}
	
	/**
	 * 配置JFinal路由映射
	 */
	@Override
	public void configRoute(Routes me) {
		
		me.add("/", IndexAction.class, "WEB-INF/view");
		me.add("/login", LoginAction.class, "WEB-INF/view");
		me.add("/user", UserAction.class, "WEB-INF/view/system/user");
		me.add("/role", RoleAction.class, "WEB-INF/view/system/role");
		me.add("/popedom", PopedomAction.class, "WEB-INF/view/system/popedom");
		me.add("/popedomMatch", PopedomMatchAction.class, "WEB-INF/view/system/popedomMatch");
		me.add("/echars", EcharsAction.class, "WEB-INF/view/echars");
	} 
	 
	/**
	 * 配置JFinal插件
	 * 数据库连接池
	 * ORM
	 * 缓存等插件
	 * 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		//配置数据库连接池插件
		DruidPlugin dbPlugin=new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		dbPlugin.setValidationQuery("select 1 from dual");
		dbPlugin.addFilter(new StatFilter());
		
		WallFilter wall = new WallFilter();
		wall.setDbType("oracle");
		
		dbPlugin.addFilter(wall);
		
		dbPlugin.setDriverClass(Const.db_driver);
		//添加到插件列表中
		me.add(dbPlugin);
		
		//orm映射 配置ActiveRecord插件
		ActiveRecordPlugin arp=new ActiveRecordPlugin(dbPlugin);
		//arp.setContainerFactory(new CaseInsensitiveContainerFactory(false));
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new OracleDialect());
		
		//添加sql文件映射
		arp.setBaseSqlTemplatePath(PathKit.getRootClassPath());
		
		/********在此添加dao层sql文件 *********/
		arp.addSqlTemplate("sql/user.sql");
		arp.addSqlTemplate("sql/role.sql");
		arp.addSqlTemplate("sql/popedom.sql");
		/********在此添加数据库 表-Model 映射*********/
		_MappingKit.mapping(arp);
		
		me.add(arp);
	}
	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new LoginInterceptor());
		//me.addGlobalActionInterceptor(new ThemeGlobalInterceptor());
		
	}
	/**
	 * 配置全局处理器
	 */
	@Override
	public void configHandler(Handlers me) {

	}
	
	/**
	 * 配置模板引擎 
	 */
	@Override
	public void configEngine(Engine me) {
		//这里只有选择JFinal TPL的时候才用
		//配置共享函数模板
		//me.addSharedFunction("/view/common/layout.html")
	}
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

	@Override
	public void afterJFinalStart() {
		FileUtils.createDir(Const.temp_file_path);
	}
	
}
