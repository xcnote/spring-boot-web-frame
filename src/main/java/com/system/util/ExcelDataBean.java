package com.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Excel导出数据类
 * @author WANGXIAOCHENG
 *
 */
@Data
public class ExcelDataBean {
	
	/**
	 * sheet表名
	 */
	private String sheetName;
	/**
	 * sheet表数据
	 * 注：表数据需与表头数据一一对应
	 */
	private List<Map<String, Object>> excelDataMaps = new ArrayList<Map<String,Object>>();
	/**
	 * sheet表头
	 */
	private LinkedHashMap<String, String> dataFieldMap = new LinkedHashMap<String, String>();
	
	public ExcelDataBean() {
		super();
	}
	
	public ExcelDataBean(String sheetName) {
		super();
		this.sheetName = sheetName;
	}
	
	public ExcelDataBean(String sheetName, List<Map<String, Object>> excelDataMaps, LinkedHashMap<String, String> dataFieldMap) {
		super();
		this.sheetName = sheetName;
		this.excelDataMaps = excelDataMaps;
		this.dataFieldMap = dataFieldMap;
	}
	
	public void addExcelDataMap(Map<String, Object> excelData){
		excelDataMaps.add(excelData);
	}
	public void addExcelDataMap(String[] keys, Object[] values){
		Map<String, Object> data = new HashMap<String, Object>();
		for(int i = 0 ; i < keys.length ; i ++){
			data.put(keys[i], values[i]);
		}
		excelDataMaps.add(data);
	}
	public void addDataFieldMap(String[] keys, String[] values) {
		for(int i = 0 ; i < keys.length ; i ++){
			dataFieldMap.put(keys[i], values[i]);
		}
	}
	
}
