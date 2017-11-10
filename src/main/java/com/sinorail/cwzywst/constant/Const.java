package com.sinorail.cwzywst.constant;

import java.io.File;
import java.math.BigDecimal;


public class Const {

	public static String file_jdbc = "jdbc_file.properties";
	public static String db_driver = "oracle.jdbc.driver.OracleDriver";
	
	public static String rootName = "root";
	
	public static String c_page = "page";
	public static String c_rows = "rows";
	
	public static String s_sort = "sort";
	public static String s_order = "order";
	
	public static String r_total = "total";
	public static String r_rows = "rows";
	public static String r_footer = "footer";
	
	/**
	 * 是否 删除 1 是 2 否
	 */
	public static BigDecimal is_del = new BigDecimal(1);
	public static BigDecimal not_del = new BigDecimal(2);
	
	public static String temp_file_path = "temp"+File.separator;
	
}
