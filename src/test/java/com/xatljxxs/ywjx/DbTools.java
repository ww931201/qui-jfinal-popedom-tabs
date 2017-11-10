package com.xatljxxs.ywjx;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.sohnny.util.dataBase.DataBaseSchema;
import com.sohnny.util.dataBase.OracleSchema;

public class DbTools {
	public static void main(String[] args) {

		DataBaseSchema dbs = new OracleSchema();

		dbs.start();

		List<Record> rList = dbs.getFieldsAttributeByTableName("E_SUPPLIESTEMPLATE");

		System.out.println("字段个数: " + rList.size());

		// createGrid(rList);
		// createJSNewRowData(rList,"popedom");
		createSeletSql(rList, "q");
		createQuiGrid(rList, "suppliestemplate");
		createFiledArray(rList);
	}

	public static void createQuiGrid(List<Record> rList,String modelName) {
		String colName;
		String colComment;
		//列表
		for (Record r : rList) {
			// System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			colName = r.getStr(DataBaseSchema.col_name);
			colComment = r.getStr(DataBaseSchema.col_comment);
			System.out.println("{ display: '" + colComment + "', name: '" + colName
					+ "', align: 'center', editor: { type: 'text'}, width: '" + 100 / 8 + "%'},");
		}
		//详情
		System.out.println("\r\n******************详情**********************\r\n");
		for (Record r : rList) {
			// System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			colName = r.getStr(DataBaseSchema.col_name);
			colComment = r.getStr(DataBaseSchema.col_comment);
			System.out.println("<tr><td>"+colComment+"</td><td>#("+modelName+"."+colName+" ??)</td></tr>");
		}
		//保存页
		System.out.println("\r\n******************保存**********************\r\n");
		System.out.println("<input type='hidden' name='"+modelName+".ID' value='#("+modelName+".ID ??)' />");
		for (Record r : rList) {
			// System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			colName = r.getStr(DataBaseSchema.col_name);
			colComment = r.getStr(DataBaseSchema.col_comment);
			System.out.println("<tr><td>"+colComment+"：</td><td><input type='text' name='"+modelName+"."+colName+"' value='#("+modelName+"."+colName+" ??)' class='validate[required,length[0,30]]'/><span class='star'>*</span></td></tr>");
		}
	}
	
	 public static void createFiledArray (List<Record> rList) {
		 String colName;
		 for(Record r : rList) {
			 //System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			 colName=r.getStr(DataBaseSchema.col_name);
			 String colComment = r.getStr(DataBaseSchema.col_comment);
			 System.out.print("\""+colComment+"\", ");
		 }
		 System.out.println();
		 for(Record r : rList) {
			 //System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			 colName=r.getStr(DataBaseSchema.col_name);
			 String colComment = r.getStr(DataBaseSchema.col_comment);
			 System.out.print("\""+colName+"\", ");
		 }
		 System.out.println("带正则的表达式数组");
		 
		 	System.out.print("String[][] field = {");
		 for(Record r : rList) {
			 //System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			 colName=r.getStr(DataBaseSchema.col_name);
			 String colComment = r.getStr(DataBaseSchema.col_comment);
			 System.out.print("{\""+colName+"\",\".*\",\""+colComment+"\"}, ");
		 }
		 	System.out.println("}");
	 }

	public static void createSeletSql(List<Record> rList, String tableTempName) {
		String colName;
		for (Record r : rList) {
			// System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			colName = r.getStr(DataBaseSchema.col_name);
			System.out.print(tableTempName + "." + colName + ", ");
		}
	}

	public static void createJSNewRowData(List<Record> rList, String modelName) {
		String colName;
		String colComment;
		for (Record r : rList) {
			// System.out.println(r.get("COLUMN_NAME")+"\t"+r.getStr("COLUMN_COMMENT"));
			colName = r.getStr(DataBaseSchema.col_name);
			colComment = r.getStr(DataBaseSchema.col_comment);
			System.out.println(modelName + "." + colName + " : " + colComment);
		}
	}

}
