package com.deep007.goniub.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelUtils {

	private Workbook wb;
	private Sheet sheet;
	private Row row;

	public ReadExcelUtils(String filepath) {
		if (filepath == null) {
			return;
		}
		String ext = filepath.substring(filepath.lastIndexOf("."));
		try {
			InputStream is = new FileInputStream(filepath);
			if (".xls".equals(ext)) {
				wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(ext)) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @return String[] 表头内容的数组
	 * @throws Exception Exception
	 * @author zengwendong
	 */
	public String[] readExcelTitle() throws Exception {
		if (wb == null) {
			throw new Exception("Workbook对象为空！");
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = row.getCell(i).getCellFormula();
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * @return Map 包含单元格数据内容的Map对象
	 * @throws Exception Exception
	 * @author zengwendong
	 */
	public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
		if (wb == null) {
			throw new Exception("Workbook对象为空！");
		}
		Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
			while (j < colNum) {
				Object obj = getCellFormatValue(row.getCell(j));
				cellValue.put(j, obj);
				j++;
			}
			content.put(i, cellValue);
		}
		return content;
	}

	/**
	 * 
	 * 根据Cell类型设置数据
	 * 
	 * @param cell cell
	 * @return cell的value
	 * @author zengwendong
	 */
	private Object getCellFormatValue(Cell cell) {
		Object cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case NUMERIC:// 如果当前Cell的Type为NUMERIC
			case FORMULA: {
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式
					// data格式是带时分秒的：2013-7-10 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();
					// data格式是不带带时分秒的：2013-7-10
					Date date = cell.getDateCellValue();
					cellvalue = date;
				} else {// 如果是纯数字

					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			case STRING:// 如果当前Cell的Type为STRING
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			default:// 默认的Cell值
				cellvalue = "";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;
	}
	
	public static void main(String[] args) {
		try {
			String filepath = "/Users/stephen/Downloads/shuju53061.xls";
			ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
			// 对读取Excel表格标题测试
//			String[] title = excelReader.readExcelTitle();
//			System.out.println("获得Excel表格的标题:");
//			for (String s : title) {
//				System.out.print(s + " ");
//			}
			
			// 对读取Excel表格内容测试
			Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
			System.out.println("获得Excel表格的内容:");
			for (int i = 1; i <= map.size(); i++) {
				System.out.println(map.get(i));
				break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
