package com.jian.zheng;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTest {
	// private final static Logger logger = Logger.getLogger(ExcelTest.class);
	private static Map<String, String[]> HEADER_METHOD_MAP = new HashMap<String, String[]>();

	private static final DateFormat DFORMATTER = new SimpleDateFormat(
			"yyyy/MM/dd");
	private static final DateFormat formatter = new SimpleDateFormat("yyyy/MM");

	static {
		HEADER_METHOD_MAP.put("提交省", new String[] { "setProvince",
				"java.lang.String" });
		HEADER_METHOD_MAP.put("集团客户名称", new String[] { "setUserName",
				"java.lang.String" });
		HEADER_METHOD_MAP.put("集团客户编码", new String[] { "setUserId",
				"java.lang.String" });
		HEADER_METHOD_MAP.put("客户服务等级", new String[] { "setLevel",
				"java.lang.String" });
		HEADER_METHOD_MAP.put("业务名称", new String[] { "setBusinessName",
				"java.lang.String" });
	}

	
	/**
	 * 获取字节
	 * @param filePath
	 * @return
	 */
	private static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	private static String[] parseHeader(Row row) {
		String[] header = new String[row.getLastCellNum()];
		for (int i = 0; i < row.getLastCellNum(); i++) {
			header[i] = row.getCell(i).getStringCellValue();
			// logger.info("header["+i+"]:"+header[i]);
			System.out.println(header[i]);
		}
		return header;
	}

	/**
	 * 获取正确的workbook实例
	 * @param bs
	 * @param isXlsx
	 * @return
	 * @throws Exception
	 */
	public static List<SpecialBean> getWorkbook(byte[] bs, boolean isXlsx)
			throws Exception {
		Workbook workbook = null;
		if (isXlsx) {
			// logger.info("Excel 2007以上版本");
			workbook = new XSSFWorkbook(new ByteArrayInputStream(bs));
		} else {
			// logger.info("Excel 2003以下版本");
			workbook = new HSSFWorkbook(new ByteArrayInputStream(bs));
		}
		Sheet sheet = null;
		sheet = workbook.getSheetAt(0);
		int rowCount = sheet.getLastRowNum();// 得到的行数---少一行
		// int rowCount = sheet.getPhysicalNumberOfRows();//正确的行数
		// logger.info("rowCount:"+rowCount);
		if (rowCount < 1) {
			return Collections.emptyList();
		}
		return parseSheet(sheet);
	}

	private static List<SpecialBean> parseSheet(Sheet sheet) throws Exception {
		// logger.info("parseSheet:");
		List<SpecialBean> specialList = new ArrayList<SpecialBean>();
		// int rowCount = sheet.getLastRowNum();
		String[] header = parseHeader(sheet.getRow(0));
		for (Iterator<Row> i = sheet.rowIterator(); i.hasNext();) {
			Row row = i.next();
			if (row.getRowNum() == 0) {// 空行
				continue;
			}
			SpecialBean specialBean = new SpecialBean();
			specialList.add(specialBean);
			for (int j = 0; j < header.length; j++) {
				Cell cell = row.getCell(j);
				if (cell == null && j == 0) {
					specialList.remove(specialBean);
					break;
				}
				int columnIndex = cell.getColumnIndex();
				String[] methodAndType = HEADER_METHOD_MAP
						.get(header[columnIndex]);
				String methodName = methodAndType[0];
				Method method = specialBean.getClass().getMethod(methodName,
						Class.forName(methodAndType[1]));
				boolean isDate = false;
				if ("提交省".equals(header[columnIndex])) {
					isDate = true;
				}
				Object valueO = null;
				//判断合并单元格的值
				boolean isMerge = isMergedRegion(sheet, row.getRowNum(),
						columnIndex);
				if (isMerge) {//合并时为true
					if ("设备 功率".equals(header[columnIndex])) {
						valueO = getValue(cell, isDate);
					} else {
						valueO = getMergedRegionValue(sheet, row.getRowNum(),
								columnIndex, isDate);
						// logger.info("行："+(row.getRowNum()+1)+",列："+(columnIndex+1)+"为合并单元格");
					}
				} else {
					valueO = getValue(cell, isDate);
				}
				if ((valueO == null) && !isDate) {
					throw new Exception("行：列(" + (row.getRowNum() + 1) + ":"
							+ (columnIndex + 1) + ") '" + header[columnIndex]
							+ "'属性不能为空！");
				}
				if (valueO != null) {
					// logger.info("行：列("+(row.getRowNum()+1)+":"+(columnIndex+1)+")"+header[columnIndex]+methodName+" : "+valueO.toString());
				} else {
					// logger.info("行：列("+(row.getRowNum()+1)+":"+(columnIndex+1)+")"+header[columnIndex]+methodName+" : ");
				}
				if (method != null) {
					method.invoke(specialBean, valueO);
				}
			}

		}

		return specialList;
	}

	/**
	 * 文件中表格单元格的值
	 * 
	 * @param cell
	 * @param isDate
	 * @return
	 * @throws ParseException
	 */
	private static Object getValue(Cell cell, boolean isDate)
			throws ParseException {
		Object retVal = null;
		Cell hCell = cell;
		switch (hCell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			String cellValue = "";
			cellValue = hCell.getRichStringCellValue().toString();
			if (isDate) {
				return cellValue;
			}
			retVal = cellValue;
			break;
		case Cell.CELL_TYPE_NUMERIC:
			double num = hCell.getNumericCellValue();
			if (DateUtil.isCellDateFormatted(cell)) {
				retVal = getTime(num);
			} else {
				BigDecimal bd = new BigDecimal(num);
				retVal = bd.toString();
			}
			break;
		default:
			String cellValue1 = "";
			cellValue1 = hCell.getRichStringCellValue().toString();
			if (isDate) {
				if (cellValue1 == ""
						|| cellValue1.toString().trim().length() == 0) {
					return null;
				}
			}
			retVal = cellValue1;
			break;
		}
		return retVal;
	}

	/**
	 * 判断合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	private static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {

			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * excel中日期型数据的数值格式到yyyy-MM-dd HH:mm:ss转换 数值格式的日期是从1900年1月0日为起点计算出来的天数值
	 * 
	 * @param daynum
	 * @return
	 */
	private static Date getTime(double daynum) {
		long totalMillSeconds = Double.valueOf(daynum * 86400.0D * 1000)
				.longValue();
		long time1900 = -2209190400000L;

		return new Date(totalMillSeconds + time1900);
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @param isDate
	 * @return
	 * @throws ParseException
	 */
	public static Object getMergedRegionValue(Sheet sheet, int row, int column,
			boolean isDate) throws ParseException {

		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {

				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getValue(fCell, isDate);
				}
			}
		}

		return null;
	}

	public static void main(String[] args) {
		// ExcelTest test =new ExcelTest();
		String path = "E://test.xls";

		byte[] bytes = ExcelTest.getBytes(path);

		boolean isXlsx = false;
		String end = path.substring(path.indexOf(".") + 1);
		if ("xlsx".equalsIgnoreCase(end)) {
			isXlsx = true;
		} else {
			isXlsx = false;
		}
		// test.importMonthErrorData(bytes,isXlsx);
		try {
			ExcelTest.getWorkbook(bytes, isXlsx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
