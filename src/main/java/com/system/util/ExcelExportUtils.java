package com.system.util;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.system.exception.ExcelException;

/**
 * @Description Excel导入导出工具类
 */
@Slf4j
public class ExcelExportUtils {

	/**
	 * @MethodName : exportToExcel
	 * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值:65535）
	 * @param fileName
	 *            文件名
	 * @param excelDataMaps
	 *            数据源
	 * @param dataFieldMap
	 *            表头
	 * @param sheetName
	 *            工作表的名称
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static void exportToExcel(String fileName, List<Map<String, Object>> excelDataMaps, LinkedHashMap<String, String> dataFieldMap, String sheetName, HttpServletRequest request, HttpServletResponse response) throws ExcelException {
		List<ExcelDataBean> excelDatas = new ArrayList<ExcelDataBean>();
		ExcelDataBean excelDataBean = new ExcelDataBean(sheetName, excelDataMaps, dataFieldMap);
		excelDatas.add(excelDataBean);

		exportToExcel(excelDatas, fileName, true, request, response);
	}

	/**
	 * @MethodName : exportToExcel
	 * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值:65535）,使用指定文件名（默认不添加时间后缀）
	 * @param excelDatas
	 * @param fileName
	 * @param response
	 * @throws ExcelException
	 */
	public static void exportToExcel(List<ExcelDataBean> excelDatas, String fileName, HttpServletRequest request, HttpServletResponse response) throws ExcelException {
		exportToExcel(excelDatas, fileName, false, request, response);
	}

	/**
	 * @MethodName : exportToExcel
	 * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小）
	 * @param excelDatas
	 *            组合数据源，包含多个sheet表
	 * @param fileName
	 *            导出文件的名称
	 * @param addfileDate
	 *            是否添加文件名时间后缀
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static void exportToExcel(List<ExcelDataBean> excelDatas, String fileName, boolean addfileDate, HttpServletRequest request, HttpServletResponse response) throws ExcelException {
		// 文件名默认设置为当前时间：年月日时分秒
		if (addfileDate) {
			fileName = fileName != null ? fileName + "-" + DateFormatUtils.format(new Date(), "yyyyMMddhhmmss") : DateFormatUtils.format(new Date(), "yyyyMMddhhmmss");
		}
		String userAgent = request == null? "": request.getHeader("User-Agent");
		try {
			if(userAgent.contains("MSIE") || userAgent.contains("Trident")){	//解决IE乱码
				fileName = URLEncoder.encode(fileName,"UTF8");
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (UnsupportedEncodingException e1) {
			throw new ExcelException("文件名转码失败");
		}
		// 设置response头信息
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");

		// 创建工作簿并发送到浏览器
		try {
			OutputStream os = response.getOutputStream();
			exportToExcel(excelDatas, os);
		} catch (ExcelException e) {
			throw e;
		} catch (Exception e) {
			throw new ExcelException("导出Excel失败");
		}
	}

	/**
	 * @MethodName : exportToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
	 * @param excelDatas
	 *            组合数据源，包含多个sheet表
	 * @param os
	 *            导出流
	 * @throws ExcelException
	 */
	public static void exportToExcel(List<ExcelDataBean> excelDatas, OutputStream os) throws ExcelException {

		if (CollectionUtils.isEmpty(excelDatas)) {
			throw new ExcelException("数据源中没有任何数据");
		}

		WritableWorkbook wwb;
		try {
			// 创建工作簿并发送到OutputStream指定的地方
			wwb = Workbook.createWorkbook(os);
			int sheetCount = 0;
			for (ExcelDataBean excelData : excelDatas) {
				if (CollectionUtils.isEmpty(excelData.getExcelDataMaps()) && (excelData.getDataFieldMap() == null || excelData.getDataFieldMap().isEmpty())) {
					continue;
				}
				List<Map<String, Object>> excelDataMaps = excelData.getExcelDataMaps();
				LinkedHashMap<String, String> dataFieldMap = excelData.getDataFieldMap();
				String sheetName = excelData.getSheetName();
				sheetCount = fillAllSheet(wwb, excelDataMaps, dataFieldMap, sheetName, 65535, sheetCount);
				sheetCount++;
			}

			wwb.write();
			wwb.close();
		} catch (ExcelException e) {
			throw e;
		} catch (Exception e) {
			log.error("export data fail.", e);
			throw new ExcelException("导出Excel失败");
		}

	}

	/**
	 * @Description : 添加工作表及数据
	 * @param workbook
	 * @param list
	 * @param fieldMap
	 * @param sheetName
	 * @param sheetSize
	 * @throws Exception
	 */
	private static int fillAllSheet(WritableWorkbook workbook, List<Map<String, Object>> list, LinkedHashMap<String, String> fieldMap, String sheetName, int sheetSize, int sheetCount) throws Exception {
		if (sheetSize < 1 || sheetSize > 65535) {
			sheetSize = 65534;
		}

		// 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
		// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
		// 1.计算一共有多少个工作表
		double sheetNum = Math.ceil((list.size() + 1) / new Integer(sheetSize).doubleValue());
		// 2.创建相应的工作表,并向其中填充数据
		for (int i = 0; i < sheetNum; i++) {
			// 如果只有一个工作表的情况
			if (sheetNum == 1) {
				WritableSheet sheet = workbook.createSheet(sheetName, i + sheetCount);
				// 向工作表中填充数据
				fillSheet(sheet, list, fieldMap, 0, list.size() - 1);

				// 有多个工作表的情况
			} else {
				WritableSheet sheet = workbook.createSheet(sheetName + (i + 1), i + sheetCount);
				// 获取开始索引和结束索引
				int firstIndex = i * sheetSize;
				int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1 : (i + 1) * sheetSize - 1;
				// 填充工作表
				fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);
			}

			sheetCount += i;
		}
		return sheetCount;
	}

	/**
	 * @MethodName : fillSheet
	 * @Description : 向工作表中填充数据
	 * @param sheet
	 *            工作表
	 * @param list
	 *            数据源数据
	 * @param fieldMap
	 *            中英文属性对照关系map
	 * @param firstIndex
	 *            开始索引
	 * @param lastIndex
	 *            结束索引
	 * @throws Exception
	 */
	private static void fillSheet(WritableSheet sheet, List<Map<String, Object>> list, LinkedHashMap<String, String> fieldMap, int firstIndex, int lastIndex) throws Exception {
		// 定义存放英文字段名和中文字段名的数组
		int size = fieldMap.size();
		String[] enFields = new String[size];
		String[] cnFields = new String[size];

		// 填充数组
		int count = 0;
		for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
			enFields[count] = entry.getKey();
			cnFields[count] = entry.getValue();
			count++;
		}

		// 填充表头
		for (int i = 0; i < cnFields.length; i++) {
			Label label = new Label(i, 0, cnFields[i], getHeaderCellStyle());
			sheet.addCell(label);
		}

		// 填充内容
		int rowNo = 1;
		for (int index = firstIndex; index <= lastIndex; index++) {
			Map<String, Object> item = list.get(index);
			for (int i = 0; i < enFields.length; i++) {
				Object objValue = item.get(enFields[i]);
				String fieldValue = objValue == null ? "" : objValue.toString();
				Label label = new Label(i, rowNo, fieldValue);
				sheet.addCell(label);
			}
			rowNo++;
		}
		// 设置自动列宽
		setColumnAutoSize(sheet, 10);
	}

	/**
	 * @MethodName : setColumnAutoSize
	 * @Description : 设置工作表自动列宽和首行加粗
	 * @param ws
	 */
	private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
		// 获取本列的最宽单元格的宽度
		for (int i = 0; i < ws.getColumns(); i++) {
			int colWith = 0;
			for (int j = 0; j < ws.getRows(); j++) {
				String content = ws.getCell(i, j).getContents().toString();
				int cellWith = content.length();
				if (colWith < cellWith) {
					colWith = cellWith;
				}
			}
			// 设置单元格的宽度为最宽宽度+额外宽度
			ws.setColumnView(i, colWith + extraWith);
		}
	}

	/**
	 * 设置表头格式是
	 * 
	 * @return
	 */
	private static WritableCellFormat getHeaderCellStyle() {
		/*
		 * WritableFont.createFont("宋体")：设置字体为宋体 
		 * 10：设置字体大小
		 * WritableFont.BOLD:设置字体加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
		 * UnderlineStyle.NO_UNDERLINE：没有下划线
		 */
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
		headerFormat.setFont(font);
		return headerFormat;
	}
}