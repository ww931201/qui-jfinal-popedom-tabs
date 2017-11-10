package com.sinorail.cwzywst.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.plugin.activerecord.Record;

public class ExcelUtils {
	
	
	/**
	 * 对外提供读取excel 的方法
	 * @param file
	 * @param startRowNum 起始行数,从0开始
	 * @return
	 * @throws IOException
	 */
    public static List<List<Object>> readExcel(File file, Integer startRowNum) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return readHSSFExcel(file, startRowNum);
        } else if ("xlsx".equals(extension)) {
            return readXSSFExcel(file, startRowNum);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }
    
    /**
	 * 对外提供读取excel 的方法：按单元格读取
	 * @param file
	 * @param startRowNum 起始行数,从0开始,结束到行末尾
	 * @return
	 * @throws IOException
	 */
    public static List<List<Object>> readExcel(File file, Integer startRowNum,Integer endRowNum) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return readHSSFExcel(file, startRowNum,endRowNum);
        } else if ("xlsx".equals(extension)) {
            return readXSSFExcel(file, startRowNum,endRowNum);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }
    
   
    /**
     * 根据RecordList封装excel并导出
     * 
     * 注意: 表头和字段名顺序个数必须一致
     * 
     * @param list RecordList
     * @param title 表头名称数组
     * @param field 字段名称数组
     * @param file	输出到目标文件的file
     * @throws IOException
     */
    public static void export(List<Record> list, String[] title, String[] field, File file) throws IOException{
    	
		HSSFWorkbook wb = new HSSFWorkbook();
				
		try {
			HSSFSheet s = wb.createSheet();
			
			HSSFCellStyle cs = wb.createCellStyle();
			HSSFCellStyle csbody = wb.createCellStyle();
			HSSFFont f = wb.createFont();
			f.setFontHeightInPoints((short) 12);
			f.setBold(true);
			
			cs.setVerticalAlignment(VerticalAlignment.CENTER);
			csbody.setVerticalAlignment(VerticalAlignment.CENTER);
			

			cs.setFont(f);
			cs.setFillForegroundColor((short) 0xA);
			
			wb.setSheetName(0, "sheet1");
			int rownum;
			//System.out.println("***************"+list.size());
			for (rownum = 0; rownum <= list.size(); rownum++) {
				HSSFRow r = null;
				Record es = null;
				int titleRows = 1;
				if(rownum < titleRows) { //表头
					r = s.createRow(rownum);
					r.setHeight((short) 0x240);
					for (int cellnum = 0; cellnum < title.length; cellnum ++) {
						HSSFCell c = r.createCell(cellnum);
						c.setCellValue(title[cellnum]);
						c.setCellStyle(cs);
						s.setColumnWidth(cellnum, (int) (title[cellnum].length()*1000));
					}
				} else {					
					r = s.createRow(rownum);
					r.setHeight((short) 0x160);
					es = list.get(rownum-titleRows);
					for (int cellnum = 0; cellnum < title.length; cellnum ++) {
						HSSFCell c = r.createCell(cellnum);
						Object value = es.get(field[cellnum]);
						if(value == null) {
							c.setCellValue("");
						} else {
							c.setCellValue(value.toString());
						}
						c.setCellStyle(csbody);
					}
				}
				
			}
			// create a sheet, set its title then delete it
			wb.createSheet();	
			wb.setSheetName(1, "DeletedSheet");
			wb.removeSheetAt(1);
			// end deleted sheet
			FileOutputStream out = new FileOutputStream(file);
			try {
				wb.write(out);
			} finally {
				out.close();
			}
		} finally {
			wb.close();
		}
    }

    /**
     * 根据行数和列数确定的位置获取excel文件中单元格的值	
     * @param file excel文件
     * @param rowNum 行数
     * @param colNum 列数
     * @return 获取到单元格的值
     * @throws IOException
     */
    public static Object getCellValueFromExcel(File file, Integer rowNum, Integer colNum) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return getValueHSSFExcel(file, rowNum, colNum);
		} else if ("xlsx".equals(extension)) {
			return getValueXSSFExcel(file, rowNum, colNum);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}
    

    /**
     * 根据行数和列数确定的位置替换excel文件中单元格的值 (xls)
     * @param file 要替换的excel文件
     * @param rowNum 行数
     * @param colNum 列数
     * @param value 要替换的值
     * @param aimfile 
     * @return 替换后的excel文件
     * @throws IOException 
     */
    public static File replaceCell(File file, Integer rowNum, Integer colNum, String value, File aimfile) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return replaceCellValueHSSFExcel(file, rowNum, colNum, value, aimfile);
		} else if ("xlsx".equals(extension)) {
			return replaceCellValueXSSFExcel(file, rowNum, colNum, value, aimfile);
		} else {
			throw new IOException("不支持的文件类型");
		}
    }
    
    /**
     * 
     * 
     * @param file
     * @param rowNum
     * @param colNum
     * @param value
     * @param aimfile
     * @return
     */
    private static File replaceCellValueHSSFExcel(File file, Integer rowNum, Integer colNum, String value, File aimfile) {
		// 创建一个HSSF workbook
		POIFSFileSystem fs;

		try {

			fs = new POIFSFileSystem(file);

			HSSFWorkbook wb = new HSSFWorkbook(fs);

			try {

				HSSFSheet sheet = wb.getSheetAt(0);
				HSSFRow row = sheet.getRow(rowNum);
				HSSFCell cell = row.getCell(colNum);
				cell.setCellValue(value);
				
				wb.write(aimfile);
			} finally {
				wb.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return aimfile;
    }
    
    /**
     * 
     * @param file
     * @param rowNum
     * @param colNum
     * @param value
     * @param aimfile
     * @return
     */
    private static File replaceCellValueXSSFExcel(File file, Integer rowNum, Integer colNum, String value, File aimfile) {

		try {

			// 创建一个HSSF workbook
			XSSFWorkbook wb = new XSSFWorkbook(file);

			try {

				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = sheet.getRow(rowNum);
				XSSFCell cell = row.getCell(colNum);
				cell.setCellValue(value);
				
				OutputStream out = new FileOutputStream(aimfile);
				wb.write(out);
			} finally {
				wb.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return aimfile;
	}

	/**
     * 读取Office HSSF excel 07版本之前
     * @param file
     * @param startRowNum 起始行数,从0开始,行末输入为null,就结束.
     * @return
     */
	private static List<List<Object>> readHSSFExcel(File file, Integer startRowNum)  {

        //创建一个HSSF workbook
        POIFSFileSystem fs;
        
        List<List<Object>> list = new LinkedList<List<Object>>();
        
		try {
			
			fs = new POIFSFileSystem(file);
			
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			
			try {
				//System.out.println(file.getPath()+file.getName());
				
				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					
					HSSFSheet sheet = wb.getSheetAt(k);
					int rows = sheet.getPhysicalNumberOfRows();
					
					//System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows + " row(s).");
					
					
					for (int r = startRowNum; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}

						List<Object> linked = new LinkedList<Object>();
						//System.out.println("\nROW " + row.getRowNum() + " has " + row.getPhysicalNumberOfCells() + " cell(s).");
						for (int c = 0; c < row.getLastCellNum(); c++) {
							HSSFCell cell = row.getCell(c);
							//String value1;
							Object value = null;
							if(cell != null) {
								switch (cell.getCellTypeEnum()) {

									case FORMULA:
										//value1 = "FORMULA value=" + cell.getCellFormula();
										//value = cell.getCellFormula();
										value = cell.getNumericCellValue();
										break;

									case NUMERIC:
										//value1 = "NUMERIC value=" + cell.getNumericCellValue();
										value = cell.getNumericCellValue();
										break;

									case STRING:
										//value1 = "STRING value=" + cell.getStringCellValue();
										value = cell.getStringCellValue();
										break;

									case BLANK:
										//value1 = "<BLANK>";
										value = null;
										break;

									case BOOLEAN:
										//value1 = "BOOLEAN value-" + cell.getBooleanCellValue();
										value = cell.getBooleanCellValue();
										break;

									case ERROR:
										//value1 = "ERROR value=" + cell.getErrorCellValue();
										value = cell.getErrorCellValue();
										break;

									default:
										//value1 = "UNKNOWN value of type " + cell.getCellTypeEnum();
										value = cell.getCellTypeEnum();
								}
								//System.out.print("CELL col=" + cell.getColumnIndex() + " VALUE="+ value1);
							}
							linked.add(value);
						}
						list.add(linked);
						//System.out.println();
					}
				}
			} finally {
				wb.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
    }
	
	/**
     * 读取Office HSSF excel 07版本之前
     * @param file
     * @param startRowNum 起始行数,从0开始,行末尾结束(endRowNum)
     * @return
     */
	private static List<List<Object>> readHSSFExcel(File file, Integer startRowNum,Integer endRowNum)  {

        //创建一个HSSF workbook
        POIFSFileSystem fs;
        
        List<List<Object>> list = new LinkedList<List<Object>>();
        
		try {
			
			fs = new POIFSFileSystem(file);
			
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			
			try {
				//System.out.println(file.getPath()+file.getName());
				
				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					
					HSSFSheet sheet = wb.getSheetAt(k);
					int rows = sheet.getPhysicalNumberOfRows();
					
					//System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows + " row(s).");
					
					
					for (int r = startRowNum; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}

						List<Object> linked = new LinkedList<Object>();
						//System.out.println("\nROW " + row.getRowNum() + " has " + row.getPhysicalNumberOfCells() + " cell(s).");
						for (int c = 0; c < endRowNum; c++) {
							HSSFCell cell = row.getCell(c);
							//String value1;
							Object value = null;
							if(cell != null) {
								switch (cell.getCellTypeEnum()) {

									case FORMULA:
										//value1 = "FORMULA value=" + cell.getCellFormula();
										//value = cell.getCellFormula();
										value = cell.getNumericCellValue();
										break;

									case NUMERIC:
										//value1 = "NUMERIC value=" + cell.getNumericCellValue();
										value = cell.getNumericCellValue();
										break;

									case STRING:
										//value1 = "STRING value=" + cell.getStringCellValue();
										value = cell.getStringCellValue();
										break;

									case BLANK:
										//value1 = "<BLANK>";
										value = null;
										break;

									case BOOLEAN:
										//value1 = "BOOLEAN value-" + cell.getBooleanCellValue();
										value = cell.getBooleanCellValue();
										break;

									case ERROR:
										//value1 = "ERROR value=" + cell.getErrorCellValue();
										value = cell.getErrorCellValue();
										break;

									default:
										//value1 = "UNKNOWN value of type " + cell.getCellTypeEnum();
										value = cell.getCellTypeEnum();
								}
								//System.out.print("CELL col=" + cell.getColumnIndex() + " VALUE="+ value1);
							}
							linked.add(value);
						}
						list.add(linked);
						//System.out.println();
					}
				}
			} finally {
				wb.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
    }

	/**
	 * 读取Office XSSF excel
	 * 
	 * @param file 0行开始读取整行(整行长度),row.getLastCellNum()结束
	 * @param startRowNum
	 * @return
	 */
    private static List<List<Object>> readXSSFExcel(File file, Integer startRowNum) {
	
	    //创建一个XSSF workbook
	    
	    List<List<Object>> list = new LinkedList<List<Object>>();
	    
		try {
			
			XSSFWorkbook wb = new XSSFWorkbook(file);
			
			try {
				//System.out.println(file.getPath()+file.getName());
				
				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					
					XSSFSheet sheet = wb.getSheetAt(k);
					int rows = sheet.getPhysicalNumberOfRows();
					
					//System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows + " row(s).");
					
					
					for (int r = startRowNum; r < rows; r++) {
						XSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}
	
						List<Object> linked = new LinkedList<Object>();
						//System.out.println("\nROW " + row.getRowNum() + " has " + row.getPhysicalNumberOfCells() + " cell(s).");
						for (int c = 0; c < row.getLastCellNum(); c++) {
							XSSFCell cell = row.getCell(c);
							//String value1;
							Object value = null;
							if(cell != null) {
								switch (cell.getCellTypeEnum()) {
	
									case FORMULA:
										//value1 = "FORMULA value=" + cell.getCellFormula();
										//value = cell.getCellFormula();
										value = cell.getNumericCellValue();
										break;
									
									case NUMERIC:
										//value1 = "NUMERIC value=" + cell.getNumericCellValue();
										
										value = cell.getNumericCellValue();
										
										//如果是日期格式
										if(DateUtil.isCellDateFormatted(cell)){ 
											value = cell.getDateCellValue();
										}
										
										break;
	
									case STRING:
										//value1 = "STRING value=" + cell.getStringCellValue();
										value = cell.getStringCellValue();
										break;
	
									case BLANK:
										//value1 = "<BLANK>";
										value = null;
										break;
	
									case BOOLEAN:
										//value1 = "BOOLEAN value-" + cell.getBooleanCellValue();
										value = cell.getBooleanCellValue();
										break;
	
									case ERROR:
										//value1 = "ERROR value=" + cell.getErrorCellValue();
										value = cell.getErrorCellValue();
										break;
	
									default:
										//value1 = "UNKNOWN value of type " + cell.getCellTypeEnum();
										value = cell.getCellTypeEnum();
								}
								//System.out.print("CELL col=" + cell.getColumnIndex() + " VALUE="+ value1);
							}
							linked.add(value);
						}
						list.add(linked);
						//System.out.println();
					}
				}
			} finally {
				wb.close();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return list;
    }

    /**
     * 
     * 读取Office XSSF excel 07版本以后
     * @param file  0行开始,行末结束(行标题长度),读取一行数据
     * @param startRowNum
     * @param endRowNum
     * @return
     */
    private static List<List<Object>> readXSSFExcel(File file, Integer startRowNum,Integer endRowNum) {
	
	    //创建一个XSSF workbook
	    
	    List<List<Object>> list = new LinkedList<List<Object>>();
	    
		try {
			
			XSSFWorkbook wb = new XSSFWorkbook(file);
			
			try {
				//System.out.println(file.getPath()+file.getName());
				
				for (int k = 0; k < wb.getNumberOfSheets(); k++) {
					
					XSSFSheet sheet = wb.getSheetAt(k);
					int rows = sheet.getPhysicalNumberOfRows();
					
					//System.out.println("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows + " row(s).");
					
					for (int r = startRowNum; r < rows; r++) {
						XSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}
	
						List<Object> linked = new LinkedList<Object>();
						//System.out.println("\nROW " + row.getRowNum() + " has " + row.getPhysicalNumberOfCells() + " cell(s).");
						for (int c = 0; c < endRowNum; c++) {
							XSSFCell cell = row.getCell(c);
							//String value1;
							Object value = null;
							if(cell != null) {
								switch (cell.getCellTypeEnum()) {
	
									case FORMULA:
										//value1 = "FORMULA value=" + cell.getCellFormula();
										//value = cell.getCellFormula();
										value = cell.getNumericCellValue();
										break;
									
									case NUMERIC:
										//value1 = "NUMERIC value=" + cell.getNumericCellValue();
										
										value = cell.getNumericCellValue();
										
										//如果是日期格式
										if(DateUtil.isCellDateFormatted(cell)){ 
											value = cell.getDateCellValue();
										}
										
										break;
	
									case STRING:
										//value1 = "STRING value=" + cell.getStringCellValue();
										value = cell.getStringCellValue();
										break;
	
									case BLANK:
										//value1 = "<BLANK>";
										value = null;
										break;
	
									case BOOLEAN:
										//value1 = "BOOLEAN value-" + cell.getBooleanCellValue();
										value = cell.getBooleanCellValue();
										break;
	
									case ERROR:
										//value1 = "ERROR value=" + cell.getErrorCellValue();
										value = cell.getErrorCellValue();
										break;
	
									default:
										//value1 = "UNKNOWN value of type " + cell.getCellTypeEnum();
										value = cell.getCellTypeEnum();
								}
								//System.out.print("CELL col=" + cell.getColumnIndex() + " VALUE="+ value1);
							}
							linked.add(value);
						}
						list.add(linked);
						//System.out.println();
					}
				}
			} finally {
				wb.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return list;
    }
    
	private static Object getValueHSSFExcel(File file, Integer rowNum, Integer colNum) {

		Object value = null;
		// 创建一个HSSF workbook
		POIFSFileSystem fs;

		try {

			fs = new POIFSFileSystem(file);

			HSSFWorkbook wb = new HSSFWorkbook(fs);

			try {

				// System.out.println(file.getPath()+file.getName());

				HSSFSheet sheet = wb.getSheetAt(0);
				HSSFRow row = sheet.getRow(rowNum);
				HSSFCell cell = row.getCell(colNum);
				//String value1;
				if (cell != null) {
					switch (cell.getCellTypeEnum()) {

					case FORMULA:
						//value1 = "FORMULA value=" + cell.getCellFormula();
						// value = cell.getCellFormula();
						value = cell.getNumericCellValue();
						break;

					case NUMERIC:
						//value1 = "NUMERIC value=" + cell.getNumericCellValue();
						value = cell.getNumericCellValue();
						break;

					case STRING:
						//value1 = "STRING value=" + cell.getStringCellValue();
						value = cell.getStringCellValue();
						break;

					case BLANK:
						//value1 = "<BLANK>";
						value = null;
						break;

					case BOOLEAN:
						//value1 = "BOOLEAN value-" + cell.getBooleanCellValue();
						value = cell.getBooleanCellValue();
						break;

					case ERROR:
						//value1 = "ERROR value=" + cell.getErrorCellValue();
						value = cell.getErrorCellValue();
						break;

					default:
						//value1 = "UNKNOWN value of type " + cell.getCellTypeEnum();
						value = cell.getCellTypeEnum();
					}
					// System.out.print("CELL col=" + cell.getColumnIndex() + "
					// VALUE="+ value1);
				}
			} finally {
				wb.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	private static Object getValueXSSFExcel(File file, Integer rowNum, Integer colNum) {

		Object value = null;
		// 创建一个HSSF workbook
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(file);
			try {

				// System.out.println(file.getPath()+file.getName());

				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = sheet.getRow(rowNum);
				XSSFCell cell = row.getCell(colNum);
				//String value1;
				if (cell != null) {
					switch (cell.getCellTypeEnum()) {

					case FORMULA:
						//value1 = "FORMULA value=" + cell.getCellFormula();
						// value = cell.getCellFormula();
						value = cell.getNumericCellValue();
						break;

					case NUMERIC:
						//value1 = "NUMERIC value=" + cell.getNumericCellValue();
						value = cell.getNumericCellValue();
						break;

					case STRING:
						//value1 = "STRING value=" + cell.getStringCellValue();
						value = cell.getStringCellValue();
						break;

					case BLANK:
						//value1 = "<BLANK>";
						value = null;
						break;

					case BOOLEAN:
						//value1 = "BOOLEAN value-" + cell.getBooleanCellValue();
						value = cell.getBooleanCellValue();
						break;

					case ERROR:
						//value1 = "ERROR value=" + cell.getErrorCellValue();
						value = cell.getErrorCellValue();
						break;

					default:
						//value1 = "UNKNOWN value of type " + cell.getCellTypeEnum();
						value = cell.getCellTypeEnum();
					}
					// System.out.print("CELL col=" + cell.getColumnIndex() + "
					// VALUE="+ value1);
				}
			} finally {
				wb.close();
			}
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}
	
}
